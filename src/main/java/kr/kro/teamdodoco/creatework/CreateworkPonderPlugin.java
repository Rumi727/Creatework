package kr.kro.teamdodoco.creatework;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import kr.kro.teamdodoco.creatework.ponders.kinetics.RedstoneResistorPonder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CreateworkPonderPlugin implements PonderPlugin
{
    @Override
    public @NotNull String getModId()
    {
        return Creatework.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper)
    {
        helper.forComponents(CreateworkBlocks.REDSTONE_RESISTOR.getId())
                .addStoryBoard("resistor", RedstoneResistorPonder::new, AllCreatePonderTags.KINETIC_RELAYS);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper)
    {
        helper.addToTag(AllCreatePonderTags.KINETIC_RELAYS)
                .add(CreateworkBlocks.REDSTONE_RESISTOR.getId());
    }
}