package org.cyclops.evilcraft.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A large werewolf, only appears at night by transforming from a werewolf villager.
 * @author rubensworks
 *
 */
public class EntityWerewolf extends Monster {

    static {
        NeoForge.EVENT_BUS.register(EntityWerewolf.class);
    }

    private CompoundTag villagerNBTTagCompound = new CompoundTag();
    private boolean fromVillager = false;

    private static final int BARKCHANCE = 1000;
    private static final int BARKLENGTH = 40;
    private static int barkprogress = -1;

    public EntityWerewolf(EntityType<? extends EntityWerewolf> type,  Level world) {
        super(type, world);
    }

    public EntityWerewolf(Level world) {
        super(RegistryEntries.ENTITY_WEREWOLF.get(), world);

        // This sets the default villager profession ID.
        this.villagerNBTTagCompound.putString("ProfessionName", BuiltInRegistries.VILLAGER_PROFESSION.getKey(RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get()).toString());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void transformWerewolfVillager(EntityTickEvent.Post event) {
        if(event.getEntity() instanceof Villager && !event.getEntity().level().isClientSide()) {
            Villager villager = (Villager) event.getEntity();
            if(EntityWerewolf.isWerewolfTime(event.getEntity().level())
                    && villager.getVillagerData().profession().value() == RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get()
                    && villager.level().getBrightness(LightLayer.SKY, villager.blockPosition()) > 0) {
                EntityWerewolf.replaceVillager(villager);
            }
        }
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75F;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("villager", ExtraCodecs.NBT, villagerNBTTagCompound);
        valueOutput.putBoolean("fromVillager", fromVillager);
    }

    @Override
    public void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.villagerNBTTagCompound = (CompoundTag) valueInput.read("villager", ExtraCodecs.NBT).orElseThrow();
        this.fromVillager = valueInput.getBooleanOr("fromVillager", false);
    }

    /**
     * If at the current time in the given world werewolves can appear.
     * @param world The world.
     * @return If it is werewolf party time.
     */
    public static boolean isWerewolfTime(Level world) {
        return world.getMoonBrightness() == 1.0
                && world.isDarkOutside()
                && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    private static void replaceEntity(Mob old, Mob neww, Level world) {
        neww.copyPosition(old);
        old.remove(RemovalReason.DISCARDED);

        world.addFreshEntity(neww);
        world.levelEvent(null, 1016, old.blockPosition(), 0);
    }

    /**
     * Replace this entity with the stored villager.
     */
    public void replaceWithVillager() {
        // MCP: byBiome
        Villager villager = new Villager(EntityType.VILLAGER, this.level(), VillagerType.byBiome(level().getBiome(this.blockPosition())));
        initializeWerewolfVillagerData(villager);
        replaceEntity(this, villager, this.level());
        try {
            IModHelpers.get().getMinecraftHelpers().valueInputFromNbtVoid(villagerNBTTagCompound, level().registryAccess(), villager::readAdditionalSaveData);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static void initializeWerewolfVillagerData(Villager villager) {
        villager.setVillagerData(villager
                .getVillagerData()
                .withLevel(2)
                .withProfession(RegistryEntries.VILLAGER_PROFESSION_WEREWOLF));
    }

    /**
     * Replace the given villager with a werewolf and store the data of that villager.
     * @param villager The villager to replace.
     */
    public static void replaceVillager(Villager villager) {
        EntityWerewolf werewolf = new EntityWerewolf(villager.level());
        werewolf.setVillagerNBTTagCompound(IModHelpers.get().getMinecraftHelpers().valueOutputToNbt(villager::addAdditionalSaveData, villager.level().registryAccess()));
        werewolf.setFromVillager(true);
        replaceEntity(villager, werewolf, villager.level());
    }

    private void setVillagerNBTTagCompound(CompoundTag compoundTag) {
        this.villagerNBTTagCompound = compoundTag;
    }

    @Override
    public void aiStep() {
        if(!level().isClientSide() && (!isWerewolfTime(level()) || level().getDifficulty() == Difficulty.PEACEFUL)) {
            replaceWithVillager();
        } else {
            super.aiStep();
        }

        // Random barking
        RandomSource random = level().random;
        if(random.nextInt(BARKCHANCE) == 0 && barkprogress == -1) {
            barkprogress++;
        } else if(barkprogress > -1) {
            playSound(SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.ANGRY).ambientSound().value(), 0.15F, 1.0F);
            barkprogress++;
            if(barkprogress > BARKLENGTH) {
                barkprogress = -1;
            }
        }
    }

    /**
     * Get the bark progress scaled to the given parameter.
     * @param scale The scale.
     * @return The scaled progress.
     */
    public float getBarkProgressScaled(float scale) {
        if(barkprogress == -1)
            return 0;
        else
            return (float)barkprogress / (float)BARKLENGTH * scale;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.GRUMPY).ambientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.ANGRY).hurtSound().value();
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.GRUMPY).deathSound().value();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState block) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return super.removeWhenFarAway(distanceToClosestPlayer) && !isFromVillager();
    }

    /**
     * Get the villager data.
     * @return Villager data.
     */
    public CompoundTag getVillagerNBTTagCompound() {
        return villagerNBTTagCompound;
    }

    /**
     * If this werewolf was created from a transforming villager.
     * @return If it was a villager.
     */
    public boolean isFromVillager() {
        return fromVillager;
    }

    /**
     * Set is from villager.
     * @param fromVillager If this werewolf is a transformed villager.
     */
    public void setFromVillager(boolean fromVillager) {
        this.fromVillager = fromVillager;
    }

}
