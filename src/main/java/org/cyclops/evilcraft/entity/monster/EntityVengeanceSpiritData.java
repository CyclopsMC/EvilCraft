package org.cyclops.evilcraft.entity.monster;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class EntityVengeanceSpiritData {
    private static final int SWARM_TIERS = 5;

    private static String NBTKEY_INNER_ENTITY = "innerEntity";
    private static String NBTKEY_REMAINING_LIFE = "remainingLife";
    private static String NBTKEY_FROZEN_DURATION = "frozenDuration";
    private static String NBTKEY_IS_SWARM = "isSwarm";
    private static String NBTKEY_SWARM_TIER = "swarmTier";
    private static String NBTKEY_BUILDUP_DURATION = "buildupDuration";
    private static String NBTKEY_PLAYER_ID = "playerId";
    private static String NBTKEY_PLAYER_NAME = "playerName";

    public static String NBTKEY_INNER_SPIRIT = NBTKEY_INNER_ENTITY;

    @Nullable
    private EntityType<?> innerEntityType;
    private int remainingLife;
    private int frozenDuration;
    private boolean isSwarm;
    private int swarmTier;
    private int buildupDuration;
    private String playerId;
    private String playerName;

    public EntityVengeanceSpiritData() {
        this(null, 0);
    }

    public EntityVengeanceSpiritData(EntityType<?> innerEntityType) {
        this(innerEntityType, 0);
    }

    public EntityVengeanceSpiritData(EntityType<?> innerEntityType, int swarmTier) {
        this.innerEntityType = innerEntityType;
        this.swarmTier = swarmTier;
        this.playerId = "";
        this.playerName = "";
    }

    public boolean isFrozen() {
        return this.getFrozenDuration() > 0;
    }

    public void addFrozenDuration(int amount) {
        setFrozenDuration(frozenDuration + amount);
    }

    public boolean containsPlayer() {
        return getPlayerId() != null && !getPlayerId().isEmpty();
    }

    public boolean hasInnerEntity() {
        return getInnerEntityType() != null;
    }

    public void setRandomSwarmTier(Random random) {
        setSwarmTier(getRandomSwarmTier(random));
    }

    public void readNBT(CompoundNBT tag) {
        setInnerEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(NBTKEY_INNER_ENTITY))));
        setRemainingLife(tag.getInt(NBTKEY_REMAINING_LIFE));
        setFrozenDuration(tag.getInt(NBTKEY_FROZEN_DURATION));
        setSwarm(tag.getBoolean(NBTKEY_IS_SWARM));
        setSwarmTier(tag.getInt(NBTKEY_SWARM_TIER));
        setBuildupDuration(tag.getInt(NBTKEY_BUILDUP_DURATION));
        setPlayerId(tag.getString(NBTKEY_PLAYER_ID));
        setPlayerName(tag.getString(NBTKEY_PLAYER_NAME));
    }

    public CompoundNBT writeNBT(CompoundNBT tag) {
        if (getInnerEntityType() != null)
            tag.putString(NBTKEY_INNER_ENTITY, getInnerEntityType().getRegistryName().toString());
        tag.putInt(NBTKEY_REMAINING_LIFE, getRemainingLife());
        tag.putInt(NBTKEY_FROZEN_DURATION, getFrozenDuration());
        tag.putBoolean(NBTKEY_IS_SWARM, isSwarm());
        tag.putInt(NBTKEY_SWARM_TIER, getSwarmTier());
        tag.putInt(NBTKEY_BUILDUP_DURATION, getBuildupDuration());
        tag.putString(NBTKEY_PLAYER_ID, getPlayerId());
        tag.putString(NBTKEY_PLAYER_NAME, getPlayerName());
        return tag;
    }

    public UUID getPlayerUUID() {
        try {
            return UUID.fromString(getPlayerId());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static EntityType<?> getRandomInnerEntity(Random rand) {
        List<EntityType<?>> entities = Lists.newArrayList(ForgeRegistries.ENTITIES.getValues());
        return entities.get(rand.nextInt(entities.size()));
    }

    public static int getRandomSwarmTier(Random rand) {
        return rand.nextInt(SWARM_TIERS);
    }

    public static EntityType<?> getSpiritType(@Nullable CompoundNBT tag) {
        if(tag != null && !tag.isEmpty()) {
            String innerEntity = tag.getString(NBTKEY_INNER_SPIRIT);
            return ForgeRegistries.ENTITIES.getValue(new ResourceLocation(innerEntity));
        }
        return null;
    }

    public static EntityVengeanceSpiritData fromNBT(CompoundNBT tag) {
        EntityVengeanceSpiritData data = new EntityVengeanceSpiritData();
        data.readNBT(tag);
        return data;
    }
}
