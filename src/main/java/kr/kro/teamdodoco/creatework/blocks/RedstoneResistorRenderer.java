package kr.kro.teamdodoco.creatework.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import kr.kro.teamdodoco.creatework.CreateworkPartials;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneResistorRenderer extends SplitShaftRenderer
{
    public RedstoneResistorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    protected void renderSafe(SplitShaftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay)
    {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        BlockState resistorState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        int color = Color.mixColors(0x2C0300, 0xCD0000, ((RedstoneResistorBlockEntity) be).clientSignal.getValue(partialTicks) / 15f);
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