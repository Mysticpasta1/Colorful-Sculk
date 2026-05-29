package com.mystic.colorfulsculk.init;

import com.mystic.colorfulsculk.ColorfulSculk;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ColorfulSculk.MODID);

    public static final RegistryObject<Item> SCULK_TORCH = register("sculk_torch", () -> new StandingAndWallBlockItem(BlockInit.SCULK_TORCH.get(), BlockInit.SCULK_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        var register = ITEMS.register(name, item);
        SculkTabs.addToMainTabItems(register);
        return register;
    }

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }
}
