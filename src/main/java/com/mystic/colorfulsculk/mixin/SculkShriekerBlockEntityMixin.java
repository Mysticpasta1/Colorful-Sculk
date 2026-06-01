package com.mystic.colorfulsculk.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;

import com.mystic.colorfulsculk.blocks.ColoredSculkSensorBlock;
import com.mystic.colorfulsculk.blocks.ColoredSculkShrieker;

@Mixin(SculkShriekerBlockEntity.class)
public abstract class SculkShriekerBlockEntityMixin {

    @Shadow
    private VibrationSystem.Data vibrationData;

    @Inject(method = "tryShriek", at = @At("HEAD"), cancellable = true)
    private void colorFilterTryShriek(ServerLevel level, @Nullable ServerPlayer player, CallbackInfo ci) {
        BlockState state = ((SculkShriekerBlockEntity)(Object)this).getBlockState();
        Block block = state.getBlock();
        if (block instanceof ColoredSculkShrieker shrieker) {
            VibrationInfo info = this.vibrationData.getCurrentVibration();
            if (info != null) {
                BlockPos sourcePos = BlockPos.containing(info.pos());
                DyeColor myColor = shrieker.getColor();
                if (!colorfulsculk$isMatchingColorSource(level, sourcePos, myColor)) {
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private static boolean colorfulsculk$isMatchingColorSource(Level level, BlockPos pos, DyeColor color) {
        BlockState sourceState = level.getBlockState(pos);
        Block sourceBlock = sourceState.getBlock();
        if (sourceBlock instanceof ColoredSculkSensorBlock sourceSensor) {
            return sourceSensor.getColor() == color;
        }
        if (sourceBlock instanceof SculkSensorBlock) {
            return true;
        }
        for (Direction dir : Direction.values()) {
            BlockPos adjacentPos = pos.relative(dir);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            Block adjacentBlock = adjacentState.getBlock();
            if (adjacentBlock instanceof SculkSensorBlock) {
                return true;
            }
        }
        return false;
    }
}
