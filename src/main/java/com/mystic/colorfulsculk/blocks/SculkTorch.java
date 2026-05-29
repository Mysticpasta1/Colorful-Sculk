package com.mystic.colorfulsculk.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class SculkTorch extends RedstoneTorchBlock {
    public SculkTorch() {
        super(Properties.of().noCollission().instabreak().lightLevel((light) -> 15).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public void animateTick(BlockState p_221954_, @NotNull Level p_221955_, @NotNull BlockPos p_221956_, @NotNull RandomSource p_221957_) {
        if (p_221954_.getValue(LIT)) {
            double $$4 = (double)p_221956_.getX() + 0.5 + (p_221957_.nextDouble() - 0.5) * 0.2;
            double $$5 = (double)p_221956_.getY() + 0.7 + (p_221957_.nextDouble() - 0.5) * 0.2;
            double $$6 = (double)p_221956_.getZ() + 0.5 + (p_221957_.nextDouble() - 0.5) * 0.2;
            p_221955_.addParticle(ParticleTypes.SCULK_SOUL, $$4, $$5, $$6, 0.0, 0.0, 0.0);
        }
    }
}
