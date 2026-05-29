package com.mystic.colorfulsculk;

import com.mystic.colorfulsculk.blocks.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.mystic.colorfulsculk.init.BlockInit.*;

public class ColorfulSculkUtil {

    public static Block getColoredVariant(Block block, DyeColor color) {
        if (block == Blocks.SCULK) return COLORED_SCULK.get(color).get();
        if (block == Blocks.SCULK_VEIN) return COLORED_SCULK_VEIN.get(color).get();
        if (block == Blocks.SCULK_SENSOR) return COLORED_SCULK_SENSOR.get(color).get();
        if (block == Blocks.SCULK_SHRIEKER) return COLORED_SCULK_SHRIEKER.get(color).get();
        return null;
    }

    public static Block getVanillaBlock(Block block) {
        if (block instanceof ColoredSculk) return Blocks.SCULK;
        if (block instanceof ColoredSculkVein) return Blocks.SCULK_VEIN;
        if (block instanceof ColoredSculkSensorBlock) return Blocks.SCULK_SENSOR;
        if (block instanceof ColoredSculkShrieker) return Blocks.SCULK_SHRIEKER;
        return null;
    }

    public static boolean isColoredSculk(Block block) {
        return block instanceof ColoredSculk || block instanceof ColoredSculkVein || block instanceof ColoredSculkSensorBlock ||
               block instanceof ColoredSculkShrieker;
    }

    public static boolean isColoredSculkShriekerOrSensor(Block block) {
        return block instanceof ColoredSculk ||
                block instanceof ColoredSculkShrieker;
    }

    public static boolean isColoredSculkTheSameColor(Block block, DyeColor color) {
        if (block instanceof ColoredSculk colored) {
            return colored.getColor() == color;
        }
        if (block instanceof ColoredSculkVein colored) {
            return colored.getColor() == color;
        }
        if (block instanceof ColoredSculkSensorBlock colored) {
            return colored.getColor() == color;
        }
        if (block instanceof ColoredSculkShrieker colored) {
            return colored.getColor() == color;
        }
        return false;
    }
}