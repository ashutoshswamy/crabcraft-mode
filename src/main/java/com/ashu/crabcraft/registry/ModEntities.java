package com.ashu.crabcraft.registry;

import com.ashu.crabcraft.CrabCraft;
import com.ashu.crabcraft.entity.CrabEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, CrabCraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<CrabEntity>> CRAB = ENTITIES.register("crab",
            () -> EntityType.Builder.of(CrabEntity::new, MobCategory.CREATURE)
                    .sized(0.9F, 0.55F)
                    .clientTrackingRange(8)
                    .build("crab"));

    private ModEntities() {
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
