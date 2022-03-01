package org.cyclops.evilcraft.core.monster;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;

import java.util.Random;
import java.util.function.Predicate;

/**
 * A mob without the {@link net.minecraft.world.entity.Mob} interface.
 * @author rubensworks
 */
public class EntityNoMob extends PathfinderMob {

    // Contents copied from {@link MonsterEntity}

    public EntityNoMob(EntityType<? extends EntityNoMob> type, Level world) {
        super(type, world);
        this.xpReward = 5;
    }

    /* DIRECT COPY OF MonsterEntity contents below. */

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public void aiStep() {
        this.updateSwingTime();
        this.idle();
        super.aiStep();
    }

    protected void idle() {
        float f = this.getBrightness();
        if (f > 0.5F) {
            this.noActionTime += 2;
        }

    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    public boolean hurt(DamageSource source, float amount) {
        return this.isInvulnerableTo(source) ? false : super.hurt(source, amount);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.HOSTILE_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    protected SoundEvent getFallDamageSound(int heightIn) {
        return heightIn > 4 ? SoundEvents.HOSTILE_BIG_FALL : SoundEvents.HOSTILE_SMALL_FALL;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return 0.5F - worldIn.getBrightness(pos);
    }

    public static boolean isValidLightLevel(ServerLevelAccessor worldIn, BlockPos pos, Random randomIn) {
        if (worldIn.getBrightness(LightLayer.SKY, pos) > randomIn.nextInt(32)) {
            return false;
        } else {
            int i = worldIn.getLevel().isThundering() ? worldIn.getMaxLocalRawBrightness(pos, 10) : worldIn.getMaxLocalRawBrightness(pos);
            return i <= randomIn.nextInt(8);
        }
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends Monster> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(worldIn, pos, randomIn) && checkMobSpawnRules(type, worldIn, reason, pos, randomIn);
    }

    public static boolean canMonsterSpawn(EntityType<? extends Monster> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, worldIn, reason, pos, randomIn);
    }

    public static AttributeSupplier.Builder createMonsterAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    protected boolean shouldDropExperience() {
        return true;
    }

    protected boolean shouldDropLoot() {
        return true;
    }

    public boolean isPreventingPlayerRest(Player p_230292_1_) {
        return true;
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

}
