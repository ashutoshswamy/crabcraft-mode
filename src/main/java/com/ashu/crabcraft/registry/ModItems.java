package com.ashu.crabcraft.registry;

import com.ashu.crabcraft.CrabCraft;
import com.ashu.crabcraft.item.CrabClawItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CrabCraft.MOD_ID);

    public static final DeferredHolder<Item, Item> RAW_CRAB_MEAT = ITEMS.registerSimpleItem("raw_crab_meat");

    public static final DeferredHolder<Item, Item> CRAB_CLAW = ITEMS.register("crab_claw",
            () -> new CrabClawItem(
                    new Item.Properties()
                            .stacksTo(16)
                            .food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible().build())
            ));
    public static final DeferredHolder<Item, Item> CRAB_SPAWN_EGG = ITEMS.register("crab_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CRAB, 0xB86F50, 0xE2C59D, new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
