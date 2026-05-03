package kr.kro.teamdodoco.creatework.blocks;

import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import kr.kro.teamdodoco.creatework.CreateworkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RedstoneResistorBlock extends AbstractEncasedShaftBlock implements IBE<RedstoneResistorBlockEntity>
{
    public RedstoneResistorBlock(Properties properties) { super(properties); }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        withBlockEntityDo(worldIn, pos, RedstoneResistorBlockEntity::onNeighbourChanged);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        withBlockEntityDo(worldIn, pos, RedstoneResistorBlockEntity::onNeighbourChanged);
    }

    @Override
    public Class<RedstoneResistorBlockEntity> getBlockEntityClass()
    {
        return RedstoneResistorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RedstoneResistorBlockEntity> getBlockEntityType()
    {
        return CreateworkBlockEntities.REDSTONE_RESISTOR.get();
    }
}
