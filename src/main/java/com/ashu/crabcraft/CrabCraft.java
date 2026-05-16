package com.ashu.crabcraft;

import com.ashu.crabcraft.client.CrabRenderer;
import com.ashu.crabcraft.registry.ModCreativeTabs;
import com.ashu.crabcraft.registry.ModEntities;
import com.ashu.crabcraft.registry.ModItems;
import com.ashu.crabcraft.registry.ModParticles;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(CrabCraft.MOD_ID)
public final class CrabCraft {
    public static final String MOD_ID = "crabcraft";

    public CrabCraft(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onClientSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.CRAB.get(), CrabRenderer::new);
    }
}
