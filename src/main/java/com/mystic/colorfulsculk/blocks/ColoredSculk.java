package com.mystic.colorfulsculk.blocks;

import com.mystic.colorfulsculk.ColorfulSculkUtil;
import com.mystic.colorfulsculk.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class ColoredSculk extends SculkBlock {
    private final DyeColor color;

    public ColoredSculk(DyeColor color) {
        super(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.SCULK));
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    private BlockState getRandomGrowthState(LevelAccessor p_222068_, BlockPos p_222069_, RandomSource p_222070_, boolean p_222071_) {
        BlockState blockstate;
        if (p_222070_.nextInt(11) == 0) {
            blockstate = BlockInit.COLORED_SCULK_SHRIEKER.get(this.color).get().defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(p_222071_));
        } else {
            blockstate = BlockInit.COLORED_SCULK_SENSOR.get(this.color).get().defaultBlockState();
        }

        return blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && !p_222068_.getFluidState(p_222069_).isEmpty() ? blockstate.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)) : blockstate;
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor p_222073_, LevelAccessor p_222074_, BlockPos p_222075_, RandomSource p_222076_, SculkSpreader p_222077_, boolean p_222078_) {
        int i = p_222073_.getCharge();
        if (i != 0 && p_222076_.nextInt(p_222077_.chargeDecayRate()) == 0) {
            BlockPos blockpos = p_222073_.getPos();
            boolean flag = blockpos.closerThan(p_222075_, p_222077_.noGrowthRadius());
            if (!flag && canPlaceGrowth(p_222074_, blockpos)) {
                int j = p_222077_.growthSpawnCost();
                if (p_222076_.nextInt(j) < i) {
                    BlockPos blockpos1 = blockpos.above();
                    BlockState blockstate = this.getRandomGrowthState(p_222074_, blockpos1, p_222076_, p_222077_.isWorldGeneration());
                    p_222074_.setBlock(blockpos1, blockstate, 3);
                    p_222074_.playSound(null, blockpos, blockstate.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return p_222076_.nextInt(p_222077_.additionalDecayRate()) != 0 ? i : i - (flag ? 1 : getDecayPenalty(p_222077_, blockpos, p_222075_, i));
            }
        } else {
            return i;
        }
    }

    private static int getDecayPenalty(SculkSpreader p_222080_, BlockPos p_222081_, BlockPos p_222082_, int p_222083_) {
        int i = p_222080_.noGrowthRadius();
        float f = Mth.square((float)Math.sqrt(p_222081_.distSqr(p_222082_)) - (float)i);
        int j = Mth.square(24 - i);
        float f1 = Math.min(1.0F, f / (float)j);
        return Math.max(1, (int)((float)p_222083_ * f1 * 0.5F));
    }


    private static boolean canPlaceGrowth(LevelAccessor p_222065_, BlockPos p_222066_) {
        BlockState blockstate = p_222065_.getBlockState(p_222066_.above());
        if (blockstate.isAir() || blockstate.is(Blocks.WATER) && blockstate.getFluidState().is(Fluids.WATER)) {
            int i = 0;

            for(BlockPos blockpos : BlockPos.betweenClosed(p_222066_.offset(-4, 0, -4), p_222066_.offset(4, 2, 4))) {
                BlockState blockstate1 = p_222065_.getBlockState(blockpos);
                if (ColorfulSculkUtil.isColoredSculk(blockstate1.getBlock()) &&  ColorfulSculkUtil.isColoredSculkTheSameColor(blockstate1.getBlock(), ((ColoredSculk)blockstate1.getBlock()).getColor())) {
                    ++i;
                }

                if (i > 2) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canChangeBlockStateOnSpread() {
        return true;
    }
}
