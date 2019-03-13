package org.cyclops.evilcraft.entity.monster;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class VengeanceSpiritData {
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

    private String innerEntityName;
    private int remainingLife;
    private int frozenDuration;
    private boolean isSwarm;
    private int swarmTier;
    private int buildupDuration;
    private String playerId;
    private String playerName;

    public VengeanceSpiritData() {
        this("", 0);
    }

    public VengeanceSpiritData(String innerEntity) {
        this(innerEntity, 0);
    }

    public VengeanceSpiritData(String innerEntity, int swarmTier) {
        this.innerEntityName = innerEntity;
        this.swarmTier = swarmTier;
        this.playerId = "";
        this.playerName = "";
    }

    public boolean isFrozen() {
        return this.getFrozenDuration() > 0;
    }

    public void addFrozenDuration(int addFrozenTicks) {
        this.setFrozenDuration(this.getFrozenDuration() + addFrozenTicks);
    }

    public boolean containsPlayer() {
        return getPlayerId() != null && !getPlayerId().isEmpty();
    }

    public UUID getPlayerUUID() {
        try {
            return UUID.fromString(getPlayerId());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean hasInnerEntity() {
        String innerEntity = getInnerEntityName();
        return innerEntity != null && !innerEntity.isEmpty();
    }

    public void setRandomInnerEntity(Random random) {
        setInnerEntityName(getRandomInnerEntity(random));
    }

    public void setRandomSwarmTier(Random random) {
        setSwarmTier(getRandomSwarmTier(random));
    }

    public ResourceLocation getSpiritNameOrNull() {
        return getSpiritNameOrNullFromInnerEntity(getInnerEntityName());
    }

    public String getLocalizedSpiritName() {
        if(containsPlayer()) {
            return getPlayerName();
        }

        ResourceLocation key = hasInnerEntity() ? getSpiritNameOrNull() : VengeanceSpirit.DEFAULT_L10N_KEY;
        return L10NHelpers.getLocalizedEntityName(key.toString());
    }

    public void readNBT(NBTTagCompound tag) {
        setInnerEntityName(tag.getString(NBTKEY_INNER_ENTITY));
        setRemainingLife(tag.getInteger(NBTKEY_REMAINING_LIFE));
        setFrozenDuration(tag.getInteger(NBTKEY_FROZEN_DURATION));
        setSwarm(tag.getBoolean(NBTKEY_IS_SWARM));
        setSwarmTier(tag.getInteger(NBTKEY_SWARM_TIER));
        setBuildupDuration(tag.getInteger(NBTKEY_BUILDUP_DURATION));
        setPlayerId(tag.getString(NBTKEY_PLAYER_ID));
        setPlayerName(tag.getString(NBTKEY_PLAYER_NAME));
    }

    public NBTTagCompound writeNBT(NBTTagCompound tag) {
        if (getInnerEntityName() != null)
            tag.setString(NBTKEY_INNER_ENTITY, getInnerEntityName());
        tag.setInteger(NBTKEY_REMAINING_LIFE, getRemainingLife());
        tag.setInteger(NBTKEY_FROZEN_DURATION, getFrozenDuration());
        tag.setBoolean(NBTKEY_IS_SWARM, isSwarm());
        tag.setInteger(NBTKEY_SWARM_TIER, getSwarmTier());
        tag.setInteger(NBTKEY_BUILDUP_DURATION, getBuildupDuration());
        tag.setString(NBTKEY_PLAYER_ID, getPlayerId());
        tag.setString(NBTKEY_PLAYER_NAME, getPlayerName());
        return tag;
    }

    @SuppressWarnings("unchecked")
    public static String getRandomInnerEntity(Random rand) {
        Collection<EntityList.EntityEggInfo> eggs = EntityList.ENTITY_EGGS.values();
        ArrayList<EntityList.EntityEggInfo> eggList = Lists.newArrayList(eggs);
        if(eggList.size() > 0) {
            EntityList.EntityEggInfo egg = eggList.get(rand.nextInt(eggList.size()));
            if(egg != null) {
                Class<Entity> clazz = (Class<Entity>) EntityList.getClass(egg.spawnedID);
                if(clazz != null) {
                    return clazz.getName();
                }
            }
        }
        return VengeanceSpirit.class.getName();
    }

    public static int getRandomSwarmTier(Random rand) {
        return rand.nextInt(SWARM_TIERS);
    }

    public static ResourceLocation getSpiritNameOrNullFromNBTTag(NBTTagCompound tag) {
        if(tag != null && !tag.isEmpty()) {
            String innerEntity = tag.getString(NBTKEY_INNER_SPIRIT);
            return getSpiritNameOrNullFromInnerEntity(innerEntity);
        }
        return null;
    }

    private static ResourceLocation getSpiritNameOrNullFromInnerEntity(String innerEntity) {
        if (innerEntity == null || innerEntity.isEmpty())
            return VengeanceSpirit.DEFAULT_L10N_KEY;

        return getSpiritNameOrNullFromClassSafe(innerEntity);
    }

    private static ResourceLocation getSpiritNameOrNullFromClassSafe(String className) {
        ResourceLocation spiritName = null;
        try {
            spiritName = getSpiritNameOrNullFromClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return spiritName;
    }

    private static ResourceLocation getSpiritNameOrNullFromClass(String className) throws ClassNotFoundException {
        Class<? extends Entity> clazz = (Class<? extends Entity>) Class.forName(className);
        if(!VengeanceSpirit.canSustainClass(clazz)) return null;
        return EntityList.getKey(clazz);
    }

    public static VengeanceSpiritData fromNBT(NBTTagCompound tag) {
        VengeanceSpiritData data = new VengeanceSpiritData();
        data.readNBT(tag);
        return data;
    }
}
