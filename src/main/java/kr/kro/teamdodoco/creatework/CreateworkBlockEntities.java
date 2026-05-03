package kr.kro.teamdodoco.creatework;

import com.simibubi.create.content.kinetics.transmission.SplitShaftVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import kr.kro.teamdodoco.creatework.blocks.RedstoneResistorBlockEntity;
import kr.kro.teamdodoco.creatework.blocks.RedstoneResistorRenderer;

public class CreateworkBlockEntities
{
    public static final BlockEntityEntry<RedstoneResistorBlockEntity> REDSTONE_RESISTOR = Creatework.REGISTRATE
            .blockEntity("redstone_resistor", RedstoneResistorBlockEntity::new)
            //.visual(() -> SplitShaftVisual::new)
            .validBlocks(CreateworkBlocks.REDSTONE_RESISTOR)
            .renderer(() -> RedstoneResistorRenderer::new)
            .register();

    public static void register() { }
}
