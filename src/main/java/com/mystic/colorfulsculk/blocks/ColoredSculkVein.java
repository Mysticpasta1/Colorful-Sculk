package com.mystic.colorfulsculk.blocks;

import com.mystic.colorfulsculk.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class ColoredSculkVein extends SculkVeinBlock implements SculkBehaviour{
    private final DyeColor color;

    private final MultifaceSpreader veinSpreader = new MultifaceSpreader(new ColoredSculkVein.ColoredSculkVeinSpreaderConfig(MultifaceSpreader.DEFAULT_SPREAD_ORDER));

    public ColoredSculkVein(DyeColor color) {
        super(BlockBehaviour.Properties.of().noCollission().noOcclusion().strength(0.2F).sound(SoundType.SCULK));
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public void onDischarged(LevelAccessor p_222359_, BlockState p_222360_, BlockPos p_222361_, RandomSource p_222362_) {
        if (p_222360_.is(this)) {
            for(Direction direction : DIRECTIONS) {
                BooleanProperty booleanproperty = getFaceProperty(direction);
                if (p_222360_.getValue(booleanproperty) && p_222359_.getBlockState(p_222361_.relative(direction)).is(BlockInit.COLORED_SCULK.get(this.color).get())) {
                    p_222360_ = p_222360_.setValue(booleanproperty, Boolean.valueOf(false));
                }
            }

            if (!hasAnyFace(p_222360_)) {
                FluidState fluidstate = p_222359_.getFluidState(p_222361_);
                p_222360_ = (fluidstate.isEmpty() ? Blocks.AIR : Blocks.WATER).defaultBlockState();
            }

            p_222359_.setBlock(p_222361_, p_222360_, 3);
            super.onDischarged(p_222359_, p_222360_, p_222361_, p_222362_);
        }
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor p_222369_, LevelAccessor p_222370_, BlockPos p_222371_, RandomSource p_222372_, SculkSpreader p_222373_, boolean p_222374_) {
        if (p_222374_ && this.attemptPlaceSculk(p_222373_, p_222370_, p_222369_.getPos(), p_222372_)) {
            return p_222369_.getCharge() - 1;
        } else {
            return p_222372_.nextInt(p_222373_.chargeDecayRate()) == 0 ? Mth.floor((float)p_222369_.getCharge() * 0.5F) : p_222369_.getCharge();
        }
    }

    private boolean attemptPlaceSculk(SculkSpreader p_222376_, LevelAccessor p_222377_, BlockPos p_222378_, RandomSource p_222379_) {
        BlockState blockstate = p_222377_.getBlockState(p_222378_);
        TagKey<Block> tagkey = p_222376_.replaceableBlocks();

        for(Direction direction : Direction.allShuffled(p_222379_)) {
            if (hasFace(blockstate, direction)) {
                BlockPos blockpos = p_222378_.relative(direction);
                BlockState blockstate1 = p_222377_.getBlockState(blockpos);
                if (blockstate1.is(tagkey)) {
                    BlockState blockstate2 = BlockInit.COLORED_SCULK.get(this.color).get().defaultBlockState();
                    p_222377_.setBlock(blockpos, blockstate2, 3);
                    Block.pushEntitiesUp(blockstate1, blockstate2, p_222377_, blockpos);
                    p_222377_.playSound(null, blockpos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.veinSpreader.spreadAll(blockstate2, p_222377_, blockpos, false);
                    Direction direction1 = direction.getOpposite();

                    for(Direction direction2 : DIRECTIONS) {
                        if (direction2 != direction1) {
                            BlockPos blockpos1 = blockpos.relative(direction2);
                            BlockState blockstate3 = p_222377_.getBlockState(blockpos1);
                            if (blockstate3.is(this)) {
                                this.onDischarged(p_222377_, blockstate3, blockpos1, p_222379_);
                            }
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    class ColoredSculkVeinSpreaderConfig extends MultifaceSpreader.DefaultSpreaderConfig {
        private final MultifaceSpreader.SpreadType[] spreadTypes;

        public ColoredSculkVeinSpreaderConfig(MultifaceSpreader.SpreadType... p_222402_) {
            super(ColoredSculkVein.this);
            this.spreadTypes = p_222402_;
        }

        public boolean stateCanBeReplaced(BlockGetter p_222405_, BlockPos p_222406_, BlockPos p_222407_, Direction p_222408_, BlockState p_222409_) {
            BlockState blockstate = p_222405_.getBlockState(p_222407_.relative(p_222408_));
            if (!blockstate.is(BlockInit.COLORED_SCULK.get(ColoredSculkVein.this.color).get()) && !blockstate.is(BlockInit.COLORED_SCULK_CATALYST.get(ColoredSculkVein.this.color).get()) && !blockstate.is(Blocks.MOVING_PISTON)) {
                if (p_222406_.distManhattan(p_222407_) == 2) {
                    BlockPos blockpos = p_222406_.relative(p_222408_.getOpposite());
                    if (p_222405_.getBlockState(blockpos).isFaceSturdy(p_222405_, blockpos, p_222408_)) {
                        return false;
                    }
                }

                FluidState fluidstate = p_222409_.getFluidState();
                if (!fluidstate.isEmpty() && !fluidstate.is(Fluids.WATER)) {
                    return false;
                } else if (p_222409_.is(BlockTags.FIRE)) {
                    return false;
                } else {
                    return p_222409_.canBeReplaced() || super.stateCanBeReplaced(p_222405_, p_222406_, p_222407_, p_222408_, p_222409_);
                }
            } else {
                return false;
            }
        }

        public MultifaceSpreader.SpreadType @NotNull [] getSpreadTypes() {
            return this.spreadTypes;
        }

        public boolean isOtherBlockValidAsSource(BlockState p_222411_) {
            return !p_222411_.is(BlockInit.COLORED_SCULK_VEIN.get(ColoredSculkVein.this.color).get());
        }
    }
}
