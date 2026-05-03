package kr.kro.teamdodoco.creatework;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class CreateworkPartials
{
    public static final PartialModel RESISTOR_INDICATOR = block("redstone_resistor/indicator");

    @SuppressWarnings("SameParameterValue")
    static PartialModel block(String path)
    {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(Creatework.ID, "block/" + path));
    }

    public static void init() { }
}