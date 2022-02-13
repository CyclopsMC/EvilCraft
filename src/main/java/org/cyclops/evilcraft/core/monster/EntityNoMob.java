package org.cyclops.evilcraft.core.monster;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Predicate;

/**
 * A mob without the {@link net.minecraft.entity.monster.IMob} interface.
 * @author rubensworks
 */
public class EntityNoMob extends CreatureEntity {

    // Contents copied from {@link MonsterEntity}

    public EntityNoMob(EntityType<? extends EntityNoMob> type, World world) {
        super(type, world);
        this.xpReward = 5;
    }

    /* DIRECT COPY OF MonsterEntity contents below. */

    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
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

    public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn) {
        return 0.5F - worldIn.getBrightness(pos);
    }

    public static boolean isValidLightLevel(IServerWorld worldIn, BlockPos pos, Random randomIn) {
        if (worldIn.getBrightness(LightType.SKY, pos) > randomIn.nextInt(32)) {
            return false;
        } else {
            int i = worldIn.getLevel().isThundering() ? worldIn.getMaxLocalRawBrightness(pos, 10) : worldIn.getMaxLocalRawBrightness(pos);
            return i <= randomIn.nextInt(8);
        }
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends MonsterEntity> type, IServerWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(worldIn, pos, randomIn) && checkMobSpawnRules(type, worldIn, reason, pos, randomIn);
    }

    public static boolean canMonsterSpawn(EntityType<? extends MonsterEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, worldIn, reason, pos, randomIn);
    }

    public static AttributeModifierMap.MutableAttribute createMonsterAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    protected boolean shouldDropExperience() {
        return true;
    }

    protected boolean shouldDropLoot() {
        return true;
    }

    public boolean isPreventingPlayerRest(PlayerEntity p_230292_1_) {
        return true;
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

}
