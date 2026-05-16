package com.ashu.crabcraft.client;

import com.ashu.crabcraft.CrabCraft;
import com.ashu.crabcraft.entity.CrabEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CrabModel extends GeoModel<CrabEntity> {
    @Override
    public ResourceLocation getModelResource(CrabEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(CrabCraft.MOD_ID, "geo/crab.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CrabEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(CrabCraft.MOD_ID, "textures/entity/crab.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CrabEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(CrabCraft.MOD_ID, "animations/crab.animation.json");
    }
}
