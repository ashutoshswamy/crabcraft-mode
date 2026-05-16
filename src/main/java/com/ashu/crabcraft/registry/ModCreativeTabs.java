package com.ashu.crabcraft.registry;

import com.ashu.crabcraft.CrabCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrabCraft.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.crabcraft.main"))
                    .icon(() -> ModItems.CRAB_CLAW.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.RAW_CRAB_MEAT.get());
                        output.accept(ModItems.CRAB_CLAW.get());
                        output.accept(ModItems.CRAB_SPAWN_EGG.get());
                    })
                    .build()
    );

    private ModCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }
}
