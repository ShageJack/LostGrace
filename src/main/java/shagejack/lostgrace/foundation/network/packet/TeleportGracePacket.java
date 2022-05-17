package shagejack.lostgrace.foundation.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import shagejack.lostgrace.contents.grace.GlobalGraceSet;
import shagejack.lostgrace.contents.grace.Grace;
import shagejack.lostgrace.contents.grace.GraceProvider;

import java.util.UUID;
import java.util.function.Supplier;

public class TeleportGracePacket extends SimplePacketBase {

    UUID playerUUID;
    Grace grace;

    public TeleportGracePacket(UUID playerUUID, Grace grace) {
        this.playerUUID = playerUUID;
        this.grace = grace;
    }

    public TeleportGracePacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
        CompoundTag tag = buffer.readNbt();
        if (tag != null) {
            this.grace = new Grace(tag);
        } else {
            this.grace = Grace.NULL;
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(playerUUID);
        buffer.writeNbt(grace.serializeNBT());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (this.grace == null || this.grace.equals(Grace.NULL))
                return;

            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(this.playerUUID);

            if (player == null)
                return;

            player.getCapability(GraceProvider.GRACE_HANDLER_CAPABILITY).ifPresent(graceHandler -> {
                if (graceHandler.isGraceActivated() && graceHandler.contains(this.grace) && GlobalGraceSet.contains(this.grace)) {
                    Level targetLevel = grace.getLevel();
                    Vec3 pos = Vec3.atCenterOf(grace.getPos());
                    if (targetLevel != null) {
                        if (!targetLevel.dimension().location().equals(player.getLevel().dimension().location())) {
                            player.changeDimension((ServerLevel) targetLevel);
                        }
                        player.teleportTo(pos.x(), pos.y(), pos.z());
                    }
                }
            });

        });

        ctx.setPacketHandled(true);
    }
}