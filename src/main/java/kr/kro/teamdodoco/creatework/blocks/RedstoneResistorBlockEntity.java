package kr.kro.teamdodoco.creatework.blocks;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import kr.kro.teamdodoco.creatework.CreateworkConstants;
import kr.kro.teamdodoco.creatework.CreateworkLang;
import kr.kro.teamdodoco.creatework.gui.ExtraIcons;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RedstoneResistorBlockEntity extends SplitShaftBlockEntity implements IHaveGoggleInformation
{
    public RedstoneResistorBlockEntity(BlockEntityType type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        setLazyTickRate(40);
    }

    int signal = 0;
    LerpedFloat clientSignal = LerpedFloat.linear();

    ScrollOptionBehaviour<ResistDirection> resistDirection;

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket)
    {
        compound.putInt(CreateworkConstants.Nbt.SIGNAL, signal);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket)
    {
        signal = compound.getInt(CreateworkConstants.Nbt.SIGNAL);
        clientSignal.chase(signal, 0.2f, LerpedFloat.Chaser.EXP); // 월드 인디케이터 정상화

        super.read(compound, registries, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours)
    {
        resistDirection = new ScrollOptionBehaviour<>
        (
                ResistDirection.class,
                CreateworkLang.translateDirect("contraptions.resistor.resist_direction"),
                this,
                new ResistorValueBox()
        );
        resistDirection.withCallback($ -> onAnalogSignalChanged());

        behaviours.add(resistDirection);
    }

    @Override
    public void lazyTick()
    {
        onNeighbourChanged();
    }

    @Override
    public void tick()
    {
        super.tick();
        if (level != null && level.isClientSide)
            clientSignal.tickChaser();
    }

    @Override
    public float getRotationSpeedModifier(Direction face)
    {
        if (hasSource() && face != getSourceFacing())
            return getModifierForSignal(signal, resistDirection.get());

        return 1;
    }

    public void onNeighbourChanged()
    {
        if (level == null)
            return;

        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            onAnalogSignalChanged();
    }

    public void onAnalogSignalChanged()
    {
        if (level == null)
            return;

        signal = level.getBestNeighborSignal(worldPosition);

        // Ponder 인디케이터 정상화
        if (level.isClientSide)
            clientSignal.chase(signal, 0.2f, LerpedFloat.Chaser.EXP);
        else
        {
            detachKinetics();
            removeSource();

            attachKinetics();
        }
    }

    public static float getModifierForSignal(int signal, ResistDirection direction)
    {
        if (direction == ResistDirection.NORMAL)
            signal = 15 - signal;

        // Create의 0 ~ 15 구현 법은 (signal + 1) / 16이지만, 이 블록은 0.5 ~ 1배가 아닌 0 ~ 1배이기 때문에 더 자연스러운 보간이 필요했습니다.
        // signal = Math.round(signal * 16f / 15f)와 같습니다.
        signal = (signal * 16 + 7) / 15;
        return signal / 16f;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking)
    {
        CreateworkLang.translate("contraptions.resistor.info_header").forGoggles(tooltip);
        CreateworkLang.translate("contraptions.resistor.analog_signal.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        CreateLang.builder()
                .add(CreateLang.number(signal)
                        .style(ChatFormatting.GOLD))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateLang.number(15)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        CreateworkLang.translate("contraptions.resistor.modifier.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        CreateLang.builder()
                .add(CreateworkLang.translate("contraptions.resistor.modifier.value", getModifierForSignal(signal, resistDirection.get()))
                        .style(ChatFormatting.GOLD))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateworkLang.translate("contraptions.resistor.modifier.max_value", 1f)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        return true;
    }

    public enum ResistDirection implements INamedIconOptions
    {
        NORMAL(ExtraIcons.I_RESISTOR),
        @SuppressWarnings("unused") REVERSAL(ExtraIcons.I_RESISTOR_REVERSAL);

        final ExtraIcons icon;
        final String translationKey;

        ResistDirection(ExtraIcons icon)
        {
            this.icon = icon;
            this.translationKey = "creatework.contraptions.resistor.resist_direction." + Lang.asId(name());
        }

        @Override
        public ExtraIcons getIcon()
        {
            return this.icon;
        }

        @Override
        public String getTranslationKey()
        {
            return this.translationKey;
        }
    }

    public static class ResistorValueBox extends ValueBoxTransform.Sided
    {
        @Override
        protected Vec3 getSouthLocation()
        {
            return VecHelper.voxelSpace(8.0, 8.0, 16.0);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction)
        {
            Direction.Axis axis = state.getValue(AbstractEncasedShaftBlock.AXIS);
            return direction.getAxis() != axis;
        }
    }
}
