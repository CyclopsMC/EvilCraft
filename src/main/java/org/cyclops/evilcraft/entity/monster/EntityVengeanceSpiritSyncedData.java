package org.cyclops.evilcraft.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class EntityVengeanceSpiritSyncedData extends EntityVengeanceSpiritData {

    private SynchedEntityData dataManager;

    public EntityVengeanceSpiritSyncedData(SynchedEntityData dataManager) {
        super();
        register(dataManager);
    }

    public EntityVengeanceSpiritSyncedData(SynchedEntityData dataManager, EntityType<?> innerEntity) {
        super(innerEntity);
        register(dataManager);
    }

    public void register(SynchedEntityData dataManager) {
        this.dataManager = dataManager;
        EntityType<?> innerEntity = super.getInnerEntityType();
        dataManager.define(EntityVengeanceSpirit.WATCHERID_INNER, innerEntity == null ? "" : ForgeRegistries.ENTITY_TYPES.getKey(innerEntity).toString());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE, super.getRemainingLife());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_FROZENDURATION, super.getFrozenDuration());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE, 0);
        dataManager.define(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, "");
        dataManager.define(EntityVengeanceSpirit.WATCHERID_ISSWARM, super.isSwarm() ? 1 : 0);
        dataManager.define(EntityVengeanceSpirit.WATCHERID_SWARMTIER, super.getSwarmTier());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_BUILDUP, super.getBuildupDuration());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_PLAYERID, super.getPlayerId());
        dataManager.define(EntityVengeanceSpirit.WATCHERID_PLAYERNAME, super.getPlayerName());
    }

    @Nullable
    @Override
    public EntityType<?> getInnerEntityType() {
        String entityName = dataManager.get(EntityVengeanceSpirit.WATCHERID_INNER);
        return entityName.isEmpty() ? null : ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityName));
    }

    @Override
    public void setInnerEntityType(EntityType<?> innerEntityType) {
        dataManager.set(EntityVengeanceSpirit.WATCHERID_INNER, innerEntityType == null ? "" : ForgeRegistries.ENTITY_TYPES.getKey(innerEntityType).toString());
    }

    @Override
    public int getRemainingLife() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE);
    }

    @Override
    public void setRemainingLife(int remainingLife) {
        dataManager.set(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE, remainingLife);
    }

    @Override
    public int getFrozenDuration() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_FROZENDURATION);
    }

    @Override
    public void setFrozenDuration(int frozenDuration) {
        dataManager.set(EntityVengeanceSpirit.WATCHERID_FROZENDURATION, frozenDuration);
    }

    @Override
    public String getPlayerId() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_PLAYERID);
    }

    @Override
    public void setPlayerId(String playerId) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_PLAYERID, playerId);
    }

    @Override
    public String getPlayerName() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_PLAYERNAME);
    }

    @Override
    public void setPlayerName(String playerName) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_PLAYERNAME, playerName);
    }

    @Override
    public boolean isSwarm() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_ISSWARM) == 1;
    }

    @Override
    public void setSwarm(boolean isSwarm) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_ISSWARM, isSwarm ? 1 : 0);
    }

    @Override
    public int getSwarmTier() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_SWARMTIER);
    }

    @Override
    public void setSwarmTier(int swarmTier) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_SWARMTIER, swarmTier);
    }

    public boolean isGlobalVengeance() {
        return dataManager.get(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE) == 1;
    }

    public void setGlobalVengeance(boolean globalVengeance) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE, globalVengeance ? 1 : 0);
    }

    public String[] getVengeancePlayers() {
        String encodedPlayers = dataManager.get(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS);
        if(encodedPlayers.length() == 0) {
            return new String[0];
        }
        return encodedPlayers.split("&");
    }

    public void setVengeancePlayers(String[] vengeancePlayers) {
        this.dataManager.set(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, StringUtils.join(vengeancePlayers, "&"));
    }
}
