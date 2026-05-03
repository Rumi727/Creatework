package kr.kro.teamdodoco.creatework;

import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import kr.kro.teamdodoco.creatework.blocks.RedstoneResistorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CreateworkBlocks
{
    public static final BlockEntry<RedstoneResistorBlock> REDSTONE_RESISTOR = Creatework.REGISTRATE.block("redstone_resistor", RedstoneResistorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .item()
            .transform(customItemModel("redstone_resistor", "item"))
            .register();

    public static void register() { }
}
