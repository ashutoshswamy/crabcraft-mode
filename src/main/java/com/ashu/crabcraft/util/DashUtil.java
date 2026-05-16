package com.ashu.crabcraft.util;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public final class DashUtil {
    public static final String DASH_TICKS_TAG = "crabcraft_dash_ticks";
    public static final String KB_TICKS_TAG = "crabcraft_kb_ticks";

    private DashUtil() {
    }

    public static void applySidewaysDash(ServerPlayer player) {
        float side = Mth.abs(player.xxa) > 0.05F ? player.xxa : (player.isShiftKeyDown() ? -1.0F : 1.0F);
        float yawRad = player.getYRot() * ((float) Math.PI / 180.0F);

        double dx = -Mth.sin(yawRad) * side * 1.35D;
        double dz = Mth.cos(yawRad) * side * 1.35D;

        player.push(dx, 0.22D, dz);
        player.hurtMarked = true;
        player.getPersistentData().putInt(DASH_TICKS_TAG, 40);
    }

    public static void spawnDashSplash(ServerLevel level, Player player) {
        level.sendParticles(ParticleTypes.SPLASH, player.getX(), player.getY() + 0.1D, player.getZ(), 18, 0.4D, 0.15D, 0.4D, 0.06D);
    }

    public static void spawnDashTrail(ServerLevel level, Player player) {
        level.sendParticles(new DustParticleOptions(new Vector3f(0.15F, 0.45F, 0.95F), 1.0F),
                player.getX(), player.getY(0.55D), player.getZ(), 6,
                0.2D, 0.2D, 0.2D, 0.01D);
    }
}
