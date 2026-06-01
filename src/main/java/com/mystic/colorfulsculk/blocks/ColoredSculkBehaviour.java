package com.mystic.colorfulsculk.blocks;

import com.mystic.colorfulsculk.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public interface ColoredSculkBehaviour {
    Map<DyeColor, ColoredSculkBehaviour> DEFAULTS = new EnumMap<>(DyeColor.class);

    static ColoredSculkBehaviour getDefaultForColor(DyeColor color) {
        return DEFAULTS.computeIfAbsent(color, (c) -> new ColoredSculkBehaviour() {
            @Override
            public DyeColor getColor() {
                return c;
            }

            @Override
            public int attemptUseCharge(ColoredSculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos catalystPos, RandomSource random, ColoredSculkSpreader spreader, boolean isWorldGen) {
                return cursor.getDecayDelay() > 0 ? cursor.getCharge() : 0;
            }

            @Override
            public boolean attemptSpreadVein(LevelAccessor p_222048_, BlockPos p_222049_, BlockState p_222050_, @Nullable Collection<Direction> p_222051_, boolean p_222052_, DyeColor spreaderColor) {
                if (p_222051_ == null) {
                    return ((ColoredSculkVein) BlockInit.COLORED_SCULK_VEIN.get(spreaderColor).get()).getSameSpaceSpreader().spreadAll(p_222048_.getBlockState(p_222049_), p_222048_, p_222049_, p_222052_) > 0L;
                } else if (!p_222051_.isEmpty()) {
                    if (p_222050_.getBlock() instanceof ColoredSculkVein sculkVein && sculkVein.getColor() != spreaderColor) {
                        BlockState newState = BlockInit.COLORED_SCULK_VEIN.get(spreaderColor).get().defaultBlockState();
                        for(Direction dir : p_222051_) {
                            newState = newState.setValue(MultifaceBlock.getFaceProperty(dir), true);
                        }
                        if (p_222050_.hasProperty(BlockStateProperties.WATERLOGGED) && p_222050_.getValue(BlockStateProperties.WATERLOGGED)) {
                            newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
                        }
                        p_222048_.setBlock(p_222049_, newState, 3);
                        return true;
                    }
                    return (p_222050_.isAir() || p_222050_.getFluidState().is(Fluids.WATER)) && ColoredSculkVein.regrow(p_222048_, p_222049_, p_222050_, p_222051_, spreaderColor);
                } else {
                    return ColoredSculkBehaviour.super.attemptSpreadVein(p_222048_, p_222049_, p_222050_, p_222051_, p_222052_, spreaderColor);
                }
            }

            @Override
            public int updateDecayDelay(int p_222061_) {
                return Math.max(p_222061_ - 1, 0);
            }
        });
    }

    DyeColor getColor();

    int attemptUseCharge(ColoredSculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos catalystPos, RandomSource random, ColoredSculkSpreader spreader, boolean isWorldGen);

    default boolean canChangeBlockStateOnSpread() {
        return true;
    }

    default byte getSculkSpreadDelay() {
        return 1;
    }

    default void onDischarged(LevelAccessor p_222026_, BlockState p_222027_, BlockPos p_222028_, RandomSource p_222029_) {
    }

    default boolean attemptSpreadVein(LevelAccessor p_222034_, BlockPos p_222035_, BlockState p_222036_, @Nullable Collection<Direction> p_222037_, boolean p_222038_, DyeColor spreaderColor) {
        return ((MultifaceBlock) BlockInit.COLORED_SCULK_VEIN.get(spreaderColor).get()).getSpreader().spreadAll(p_222036_, p_222034_, p_222035_, p_222038_) > 0L;
    }

    default int updateDecayDelay(int p_222045_) {
        return 1;
    }
}