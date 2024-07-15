package org.cyclops.evilcraft.entity.monster;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EntityVengeanceSpiritSyncedData extends EntityVengeanceSpiritData {

    private final Supplier<SynchedEntityData> dataManager;

    public EntityVengeanceSpiritSyncedData(SynchedEntityData.Builder builder, Supplier<SynchedEntityData> dataManager) {
        super();
        register(builder);
        this.dataManager = dataManager;
    }

    public EntityVengeanceSpiritSyncedData(SynchedEntityData.Builder builder, Supplier<SynchedEntityData> dataManager, EntityType<?> innerEntity) {
        super(innerEntity);
        register(builder);
        this.dataManager = dataManager;
    }

    public void register(SynchedEntityData.Builder builder) {
        EntityType<?> innerEntity = super.getInnerEntityType();
        builder.define(EntityVengeanceSpirit.WATCHERID_INNER, innerEntity == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(innerEntity).toString());
        builder.define(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE, super.getRemainingLife());
        builder.define(EntityVengeanceSpirit.WATCHERID_FROZENDURATION, super.getFrozenDuration());
        builder.define(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE, 0);
        builder.define(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, "");
        builder.define(EntityVengeanceSpirit.WATCHERID_ISSWARM, super.isSwarm() ? 1 : 0);
        builder.define(EntityVengeanceSpirit.WATCHERID_SWARMTIER, super.getSwarmTier());
        builder.define(EntityVengeanceSpirit.WATCHERID_BUILDUP, super.getBuildupDuration());
        builder.define(EntityVengeanceSpirit.WATCHERID_PLAYERID, super.getPlayerId());
        builder.define(EntityVengeanceSpirit.WATCHERID_PLAYERNAME, super.getPlayerName());
    }

    @Nullable
    @Override
    public EntityType<?> getInnerEntityType() {
        String entityName = dataManager.get().get(EntityVengeanceSpirit.WATCHERID_INNER);
        return entityName.isEmpty() ? null : BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entityName));
    }

    @Override
    public void setInnerEntityType(EntityType<?> innerEntityType) {
        dataManager.get().set(EntityVengeanceSpirit.WATCHERID_INNER, innerEntityType == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(innerEntityType).toString());
    }

    @Override
    public int getRemainingLife() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE);
    }

    @Override
    public void setRemainingLife(int remainingLife) {
        dataManager.get().set(EntityVengeanceSpirit.WATCHERID_REMAININGLIFE, remainingLife);
    }

    @Override
    public int getFrozenDuration() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_FROZENDURATION);
    }

    @Override
    public void setFrozenDuration(int frozenDuration) {
        dataManager.get().set(EntityVengeanceSpirit.WATCHERID_FROZENDURATION, frozenDuration);
    }

    @Override
    public String getPlayerId() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_PLAYERID);
    }

    @Override
    public void setPlayerId(String playerId) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_PLAYERID, playerId);
    }

    @Override
    public String getPlayerName() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_PLAYERNAME);
    }

    @Override
    public void setPlayerName(String playerName) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_PLAYERNAME, playerName);
    }

    @Override
    public boolean isSwarm() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_ISSWARM) == 1;
    }

    @Override
    public void setSwarm(boolean isSwarm) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_ISSWARM, isSwarm ? 1 : 0);
    }

    @Override
    public int getSwarmTier() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_SWARMTIER);
    }

    @Override
    public void setSwarmTier(int swarmTier) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_SWARMTIER, swarmTier);
    }

    public boolean isGlobalVengeance() {
        return dataManager.get().get(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE) == 1;
    }

    public void setGlobalVengeance(boolean globalVengeance) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_GLOBALVENGEANCE, globalVengeance ? 1 : 0);
    }

    public String[] getVengeancePlayers() {
        String encodedPlayers = dataManager.get().get(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS);
        if(encodedPlayers.length() == 0) {
            return new String[0];
        }
        return encodedPlayers.split("&");
    }

    public void setVengeancePlayers(String[] vengeancePlayers) {
        this.dataManager.get().set(EntityVengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, StringUtils.join(vengeancePlayers, "&"));
    }
}
