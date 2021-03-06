package shagejack.lostgrace.foundation.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DiscoverGracePacket extends SimplePacketBase {

    // extra rendering packet for Lost Grace Discovered screen
    public DiscoverGracePacket() {

    }

    public DiscoverGracePacket(FriendlyByteBuf buffer) {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {

        });

        ctx.setPacketHandled(true);
    }
}
