package com.mystic.colorfulsculk.init;

import com.mystic.colorfulsculk.ColorfulSculk;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SculkTabs {
    public static final List<Supplier<? extends ItemLike>> MAIN_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends ItemLike>> MAIN_BLOCKS = new ArrayList<>();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ColorfulSculk.MODID);
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.literal("Colorful Sculk"))
            .icon(() -> {
                List<Supplier<? extends ItemLike>> all = new ArrayList<>();
                all.addAll(MAIN_BLOCKS);
                all.addAll(MAIN_ITEMS);
                if (all.isEmpty()) return ItemStack.EMPTY;
                return new ItemStack(all.get((int)((System.currentTimeMillis() / 1000) % all.size())).get());
            })
            .displayItems((parameters, pOutput) -> {
                MAIN_BLOCKS.forEach(itemLike -> pOutput.accept(itemLike.get()));
                MAIN_ITEMS.forEach(itemLike -> pOutput.accept(itemLike.get()));
            }).build());

    public static <T extends Item> RegistryObject<T> addToMainTab (RegistryObject<T> itemLike) {
        MAIN_BLOCKS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToMainTabItems (RegistryObject<T> itemLike) {
        MAIN_ITEMS.add(itemLike);
        return itemLike;
    }

    public static void init(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
