package com.mystic.colorfulsculk.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
public class ColoredSculkCatalyst extends SculkCatalystBlock {
    private final DyeColor color;

    public ColoredSculkCatalyst(DyeColor color) {
        super(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.SCULK));
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ColoredSculkCatalystBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? createTickerHelper(type, BlockEntityType.SCULK_CATALYST, ColoredSculkCatalystBlockEntity::serverTick) : null;
    }
}
