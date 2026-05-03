package kr.kro.teamdodoco.creatework.ponders.kinetics;

import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import kr.kro.teamdodoco.creatework.CreateworkConstants;
import kr.kro.teamdodoco.creatework.blocks.RedstoneResistorBlockEntity;
import kr.kro.teamdodoco.creatework.gui.ExtraIcons;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class RedstoneResistorPonder
{
    public RedstoneResistorPonder(SceneBuilder sceneBuilder, SceneBuildingUtil util)
    {
        var scene = new CreateSceneBuilder(sceneBuilder);
        scene.title("resistor", "Controlling rotational speed with Redstone Resistor");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);

        BlockPos leverPos = util.grid().at(3, 1, 0);
        scene.world().showSection(util.select().fromTo(leverPos, leverPos.south()), Direction.UP);

        BlockPos gaugePos = util.grid().at(0, 1, 2);
        Selection gauge = util.select().position(gaugePos);
        scene.world().showSection(gauge, Direction.UP);
        scene.world().setKineticSpeed(gauge, 0);

        scene.idle(5);
        scene.world().showSection(util.select().position(5, 1, 2), Direction.DOWN);
        scene.idle(10);

        for (int i = 4; i >= 1; i--)
        {
            scene.idle(5);
            scene.world().showSection(util.select().position(i, 1, 2), Direction.DOWN);
        }

        BlockPos resistorPos = util.grid().at(3, 1, 2);

        scene.world().setKineticSpeed(gauge, 32);
        scene.effects().indicateSuccess(gaugePos);
        scene.idle(10);

        scene.overlay().showText(50)
                .text("Redstone Resistor will relay rotation in a straight line")
                .placeNearTarget()
                .pointAt(util.vector().topOf(resistorPos));

        scene.idle(60);

        scene.overlay().showText(150)
                .colored(PonderPalette.SLOW)
                .text("When receiving by Redstone, it rotation speed decreases.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(resistorPos));

        for (int i = 1; i <= 15; i++)
        {
            scene.overlay().showText(7)
                    .colored(PonderPalette.SLOW)
                    .text("%1$s RPM", 32 * RedstoneResistorBlockEntity.getModifierForSignal(i, RedstoneResistorBlockEntity.ResistDirection.NORMAL))
                    .placeNearTarget()
                    .pointAt(util.vector().topOf(gaugePos));

            scene.idle(5);

            setRedstonePower(scene, util, i, RedstoneResistorBlockEntity.ResistDirection.NORMAL);
            scene.effects().indicateRedstone(leverPos);

            scene.idle(5);
        }

        scene.idle(10);
        scene.addKeyframe();
        scene.idle(10);

        Vec3 blockSurface = util.vector().blockSurface(resistorPos, Direction.NORTH);

        scene.overlay().showFilterSlotInput(blockSurface, Direction.NORTH, 137);
        scene.overlay().showControls(blockSurface, Pointing.DOWN, 10).rightClick();
        scene.idle(17);

        scene.overlay().showControls(blockSurface, Pointing.DOWN, 117).showing(ExtraIcons.I_RESISTOR_REVERSAL);
        scene.idle(4);

        setRedstonePower(scene, util, 15, RedstoneResistorBlockEntity.ResistDirection.REVERSAL);
        scene.effects().indicateSuccess(gaugePos);

        scene.idle(16);

        scene.overlay().showText(100)
                .text("You can invert the Redstone signal by setting it to [Reversal] mode.")
                .placeNearTarget()
                .pointAt(blockSurface);

        scene.idle(100);
    }

    static void setRedstonePower(CreateSceneBuilder scene, SceneBuildingUtil util, int power, RedstoneResistorBlockEntity.ResistDirection direction)
    {
        BlockPos resistorPos = util.grid().at(3, 1, 2);
        Selection resistorSelection = util.select().position(resistorPos);

        BlockPos leverPos = util.grid().at(3, 1, 0);
        Selection leverSelection = util.select().position(leverPos);

        scene.world().modifyBlockEntityNBT(leverSelection, AnalogLeverBlockEntity.class, nbt -> nbt.putInt("State", power));
        scene.world().modifyBlockEntityNBT(resistorSelection, RedstoneResistorBlockEntity.class, nbt -> nbt.putInt(CreateworkConstants.Nbt.SIGNAL, power));
        scene.world().modifyBlocks(util.select().position(leverPos.south(1)), s -> s.setValue(BlockStateProperties.POWER, power), false);

        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 2, 3, 1, 2), 32 * RedstoneResistorBlockEntity.getModifierForSignal(power, direction));
    }
}