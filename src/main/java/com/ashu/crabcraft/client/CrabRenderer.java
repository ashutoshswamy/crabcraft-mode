package com.ashu.crabcraft.client;

import com.ashu.crabcraft.entity.CrabEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrabRenderer extends GeoEntityRenderer<CrabEntity> {
    public CrabRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrabModel());
        this.shadowRadius = 0.35F;
    }

    @Override
    public Vec3 getRenderOffset(CrabEntity entity, float partialTick) {
        return super.getRenderOffset(entity, partialTick).add(0.0D, -1.35D, 0.0D);
    }
}
