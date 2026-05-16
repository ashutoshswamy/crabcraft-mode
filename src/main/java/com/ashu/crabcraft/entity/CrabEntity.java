package com.ashu.crabcraft.entity;

import com.ashu.crabcraft.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.UUID;

public class CrabEntity extends PathfinderMob implements NeutralMob, GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.crab.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.crab.walk");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.crab.attack");

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    public CrabEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Zombie.class, 6.0F, 1.2D, 1.4D));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D, 50));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity entity) {
        this.triggerAnim("main_controller", "attack");
        return super.doHurtTarget(entity);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Keep crab body rotation stable so GeckoLib model does not snap/jitter.
        Vec3 velocity = this.getDeltaMovement();
        if (velocity.horizontalDistanceSqr() > 0.0008D) {
            float moveYaw = (float) (Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 90.0F);
            // Crabs move sideways relative to their facing direction.
            float crabYaw = moveYaw + 90.0F;
            this.setYRot(crabYaw);
            this.setYBodyRot(crabYaw);
            this.setYHeadRot(crabYaw);
        }

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            this.updatePersistentAnger(serverLevel, true);
        }
    }

    public static boolean canSpawn(EntityType<CrabEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos.below()).isSource() || level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(20 * (20 + this.random.nextInt(20)));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            this.setTarget(attacker);
            this.setPersistentAngerTarget(attacker.getUUID());
            this.startPersistentAngerTimer();
        }
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readPersistentAngerSaveData(this.level(), tag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "main_controller", 5, state -> {
            if (state.isMoving()) {
                state.setAnimation(WALK);
                return PlayState.CONTINUE;
            }
            state.setAnimation(IDLE);
            return PlayState.CONTINUE;
        }).triggerableAnim("attack", ATTACK));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
