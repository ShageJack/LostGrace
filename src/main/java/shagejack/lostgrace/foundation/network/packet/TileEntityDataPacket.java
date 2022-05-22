package shagejack.lostgrace.foundation.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import shagejack.lostgrace.foundation.tile.SyncedTileEntity;

import java.util.function.Supplier;

public abstract class TileEntityDataPacket<TE extends SyncedTileEntity> extends SimplePacketBase {

	protected BlockPos tilePos;

	public TileEntityDataPacket(FriendlyByteBuf buffer) {
		tilePos = buffer.readBlockPos();
	}

	public TileEntityDataPacket(BlockPos pos) {
		this.tilePos = pos;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(tilePos);
		writeData(buffer);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			ClientLevel world = Minecraft.getInstance().level;

			if (world == null)
				return;

			BlockEntity tile = world.getBlockEntity(tilePos);

			if (tile instanceof SyncedTileEntity) {
				handlePacket((TE) tile);
			}
		});
		ctx.setPacketHandled(true);
	}

	protected abstract void writeData(FriendlyByteBuf buffer);

	protected abstract void handlePacket(TE tile);
}
