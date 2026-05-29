package com.mystic.colorfulsculk.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ColoredSculkCatalystBlockEntity extends SculkCatalystBlockEntity {
    private final DyeColor color;

    public ColoredSculkCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        Block block = state.getBlock();
        this.color = block instanceof ColoredSculkCatalyst c ? c.getColor() : null;
    }

    public DyeColor getColor() {
        return color;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SculkCatalystBlockEntity be) {
        SculkCatalystBlockEntity.serverTick(level, pos, state, be);
    }
}
