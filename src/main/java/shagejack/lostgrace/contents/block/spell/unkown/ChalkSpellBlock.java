package shagejack.lostgrace.contents.block.spell.unkown;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import shagejack.lostgrace.contents.entity.blackKnifeAssassin.BlackKnifeAssassin;
import shagejack.lostgrace.foundation.utility.ColorUtils;

public class ChalkSpellBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public ChalkSpellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if (player.getItemInHand(hand).isEmpty()) {
            switch (getSpellType(level, pos)) {
                case SUMMON_BLACK_KNIFE_ASSASSIN -> {
                    summonBlackKnife(level, pos);
                    return InteractionResult.PASS;
                }
                case NULL -> {}
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing == Direction.DOWN && !this.canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public ChalkSpellType getSpellType(Level level, BlockPos pos) {
        for(ChalkSpellType type : ChalkSpellType.values()) {
            if (type.check(level, pos))
                return type;
        }

        return ChalkSpellType.NULL;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return ItemStack.EMPTY;
    }

    public void summonBlackKnife(Level level, BlockPos pos) {
        if (level.isClientSide())
            return;

        BlockPos summonPos = pos.offset(0, 8, 0);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 16, Explosion.BlockInteraction.DESTROY);
        double radius = 3 + level.getRandom().nextDouble() * 5;
        BlackKnifeAssassin blackKnifeAssassin = new BlackKnifeAssassin(level, radius, radius, true);
        blackKnifeAssassin.setPos(Vec3.atCenterOf(summonPos));
        blackKnifeAssassin.setColor(ColorUtils.getRandomColor(level.getRandom()));
        level.addFreshEntity(blackKnifeAssassin);
    }
}
