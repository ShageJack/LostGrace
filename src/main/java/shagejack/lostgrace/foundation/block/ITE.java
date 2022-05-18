package shagejack.lostgrace.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.lostgrace.foundation.tile.BaseTileEntity;
import shagejack.lostgrace.foundation.tile.BaseTileEntityTicker;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ITE<T extends BlockEntity> extends EntityBlock {

    Class<T> getTileEntityClass();

    BlockEntityType<? extends T> getTileEntityType();

    default void withTileEntityDo(BlockGetter world, BlockPos pos, Consumer<T> action) {
        getTileEntityOptional(world, pos).ifPresent(action);
    }

    default InteractionResult onTileEntityUse(BlockGetter world, BlockPos pos, Function<T, InteractionResult> action) {
        return getTileEntityOptional(world, pos).map(action)
                .orElse(InteractionResult.PASS);
    }

    default Optional<T> getTileEntityOptional(BlockGetter world, BlockPos pos) {
        return Optional.ofNullable(getTileEntity(world, pos));
    }

    @Override
    default BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return getTileEntityType().create(p_153215_, p_153216_);
    }

    @Override
    default <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        if (BaseTileEntity.class.isAssignableFrom(getTileEntityClass()))
            return new BaseTileEntityTicker<>();
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    default T getTileEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        Class<T> expectedClass = getTileEntityClass();

        if (tileEntity == null)
            return null;
        if (!expectedClass.isInstance(tileEntity))
            return null;

        return (T) tileEntity;
    }

}
