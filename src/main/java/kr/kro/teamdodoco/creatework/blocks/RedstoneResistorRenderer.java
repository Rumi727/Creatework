package kr.kro.teamdodoco.creatework.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import kr.kro.teamdodoco.creatework.CreateworkPartials;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneResistorRenderer extends KineticBlockEntityRenderer<RedstoneResistorBlockEntity>
{
    public RedstoneResistorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    protected void renderSafe(RedstoneResistorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
    {
        // SplitShaftRenderer를 사용하면 ponder에서 축 오프셋 버그가 생김.

        Block block = be.getBlockState().getBlock();
        Direction.Axis boxAxis = ((IRotate)block).getRotationAxis(be.getBlockState());
        BlockPos pos = be.getBlockPos();
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        for (int i = 0; i < Iterate.directions.length; i++)
        {
            Direction direction = Iterate.directions[i];
            Direction.Axis axis = direction.getAxis();
            if (boxAxis != axis)
                continue;

            float offset = getRotationOffsetForPosition(be, pos, axis);
            float angle = time * be.getSpeed() * 3f / 10;

            angle *= be.getRotationSpeedModifier(direction);
            angle += offset;
            angle %= 360;
            angle = angle / 180f * (float)Math.PI;

            SuperByteBuffer superByteBuffer = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction);
            kineticRotationTransform(superByteBuffer, be, axis, angle, light);

            superByteBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }

        BlockState resistorState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        int color = Color.mixColors(0x2C0300, 0xCD0000, be.clientSignal.getValue(partialTicks) / 15f);
        SuperByteBuffer indicator = transform(CachedBuffers.partial(CreateworkPartials.RESISTOR_INDICATOR, resistorState), resistorState);
        indicator.light(light).color(color).renderInto(ms, vb);
    }

    SuperByteBuffer transform(SuperByteBuffer buffer, BlockState resistorState)
    {
        var axis = resistorState.getValue(BlockStateProperties.AXIS);
        return switch (axis)
        {
            case X -> buffer.rotateCentered((float) Math.toRadians(90.0), Direction.NORTH);
            case Y -> buffer;
            case Z -> buffer.rotateCentered((float) Math.toRadians(90.0), Direction.EAST);
        };
    }
}