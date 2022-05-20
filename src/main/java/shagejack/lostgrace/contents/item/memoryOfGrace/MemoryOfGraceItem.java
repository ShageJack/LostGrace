package shagejack.lostgrace.contents.item.memoryOfGrace;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.server.ServerLifecycleHooks;
import shagejack.lostgrace.LostGrace;
import shagejack.lostgrace.contents.grace.GlobalGraceSet;
import shagejack.lostgrace.contents.grace.Grace;
import shagejack.lostgrace.contents.grace.GraceProvider;
import shagejack.lostgrace.contents.grace.IGraceHandler;
import shagejack.lostgrace.foundation.utility.Vector3;

import java.util.concurrent.atomic.AtomicReference;

public class MemoryOfGraceItem extends Item {
    public MemoryOfGraceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide()) {
                LazyOptional<IGraceHandler> handler = player.getCapability(GraceProvider.GRACE_HANDLER_CAPABILITY);

                handler.ifPresent(graceData -> {
                    Grace grace = graceData.getLastGrace();
                    if (grace != Grace.NULL && GlobalGraceSet.contains(grace)) {
                        Level graceLevel = grace.getLevel();
                        Vector3 pos = Vector3.atCenterOf(grace.getPos());
                        if (graceLevel instanceof ServerLevel targetLevel) {
                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.teleportTo(targetLevel, pos.x(), pos.y(), pos.z(), Mth.wrapDegrees(serverPlayer.getYRot()), Mth.wrapDegrees(serverPlayer.getXRot()));
                                graceData.visitGrace(grace, false);
                            }
                        }
                    } else {
                        player.stopUsingItem();
                    }
                });
            }

            if (player.isCreative()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
}
