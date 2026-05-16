package com.ashu.crabcraft.event;

import com.ashu.crabcraft.CrabCraft;
import com.ashu.crabcraft.util.DashUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CrabCraft.MOD_ID, bus = Bus.GAME)
public final class ForgeEvents {
    private static final ResourceLocation DASH_KB_ID = ResourceLocation.fromNamespaceAndPath(CrabCraft.MOD_ID, "dash_knockback_resistance");

    private ForgeEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }

        int dashTicks = player.getPersistentData().getInt(DashUtil.DASH_TICKS_TAG);
        if (dashTicks > 0) {
            if (player.level() instanceof ServerLevel serverLevel) {
                DashUtil.spawnDashTrail(serverLevel, player);
            }
            player.getPersistentData().putInt(DashUtil.DASH_TICKS_TAG, dashTicks - 1);
        }

        int kbTicks = player.getPersistentData().getInt(DashUtil.KB_TICKS_TAG);
        var knockback = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (knockback == null) {
            return;
        }

        if (kbTicks > 0) {
            if (knockback.getModifier(DASH_KB_ID) == null) {
                knockback.addOrReplacePermanentModifier(new AttributeModifier(DASH_KB_ID, 1.0D, AttributeModifier.Operation.ADD_VALUE));
            }
            player.getPersistentData().putInt(DashUtil.KB_TICKS_TAG, kbTicks - 1);
        } else if (knockback.getModifier(DASH_KB_ID) != null) {
            knockback.removeModifier(DASH_KB_ID);
        }
    }
}
