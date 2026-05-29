package com.mystic.colorfulsculk.init;

import com.mystic.colorfulsculk.ColorfulSculk;
import com.mystic.colorfulsculk.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ColorfulSculk.MODID);

    public static final Map<DyeColor, RegistryObject<Block>> COLORED_SCULK = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> COLORED_SCULK_VEIN = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> COLORED_SCULK_CATALYST = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> COLORED_SCULK_SHRIEKER = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> COLORED_SCULK_SENSOR = new HashMap<>();

    public static final RegistryObject<SculkTorch> SCULK_TORCH = registerOnlyBlock("sculk_torch", SculkTorch::new);
    public static final RegistryObject<SculkWallTorch> SCULK_WALL_TORCH = registerOnlyBlock("sculk_wall_torch", SculkWallTorch::new);

    private static <B extends Block, I extends BlockItem> RegistryObject<B> registerMainTabBlock(String name, Supplier<B> block, Function<RegistryObject<B>, Supplier<I>> item) {
        var reg = BLOCKS.register(name, block);
        SculkTabs.addToMainTab(ItemInit.ITEMS.register(name, () -> item.apply(reg).get()));
        return reg;
    }

    private static <B extends Block, C extends Block, I extends BlockItem> RegistryObject<C> registerMainTabBlock(String name, Supplier<B> block, Function<B, C> blockFunction, Function<C, I> item) {
        var reg = BLOCKS.register(name, () -> blockFunction.apply(block.get()));
        SculkTabs.addToMainTab(ItemInit
                .ITEMS.register(name, () ->
                        item.apply(reg.get())));
        return reg;
    }

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
    }

    private static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<B> block) {
        return registerMainTabBlock(name, block, b -> () -> new BlockItem(b.get(), new Item.Properties()));
    }

    public static <B extends Block> RegistryObject<B> registerOnlyBlock(String name, Supplier<B> block) {
        return BLOCKS.register(name, block);
    }

    public static Block[] getShriekerBlocks() {
        return COLORED_SCULK_SHRIEKER.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static Block[] getCatalystBlocks() {
        return COLORED_SCULK_CATALYST.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static Block[] getSensorBlocks() {
        return COLORED_SCULK_SENSOR.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            COLORED_SCULK.put(color, registerBlock(color.getSerializedName() + "_sculk", () -> new ColoredSculk(color)));
            COLORED_SCULK_VEIN.put(color, registerBlock(color.getSerializedName() + "_sculk_vein", () -> new ColoredSculkVein(color)));
            COLORED_SCULK_CATALYST.put(color, registerBlock(color.getSerializedName() + "_sculk_catalyst", () -> new ColoredSculkCatalyst(color)));
            COLORED_SCULK_SHRIEKER.put(color, registerBlock(color.getSerializedName() + "_sculk_shrieker", () -> new ColoredSculkShrieker(color)));
            COLORED_SCULK_SENSOR.put(color, registerBlock(color.getSerializedName() + "_sculk_sensor", () -> new ColoredSculkSensorBlock(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.SCULK), color)));
        }
    }
}
