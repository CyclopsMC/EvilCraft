package org.cyclops.evilcraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import org.apache.commons.lang3.StringUtils;

public class VengeanceSpiritSyncedData extends VengeanceSpiritData {

    private EntityDataManager dataManager;

    public VengeanceSpiritSyncedData(EntityDataManager dataManager) {
        super();
        register(dataManager);
    }

    public VengeanceSpiritSyncedData(EntityDataManager dataManager, String innerEntity) {
        super(innerEntity);
        register(dataManager);
    }

    public void register(EntityDataManager dataManager) {
        this.dataManager = dataManager;
        dataManager.register(VengeanceSpirit.WATCHERID_INNER, super.getInnerEntityName());
        dataManager.register(VengeanceSpirit.WATCHERID_REMAININGLIFE, super.getRemainingLife());
        dataManager.register(VengeanceSpirit.WATCHERID_FROZENDURATION, super.getFrozenDuration());
        dataManager.register(VengeanceSpirit.WATCHERID_GLOBALVENGEANCE, 0);
        dataManager.register(VengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, "");
        dataManager.register(VengeanceSpirit.WATCHERID_ISSWARM, super.isSwarm() ? 1 : 0);
        dataManager.register(VengeanceSpirit.WATCHERID_SWARMTIER, super.getSwarmTier());
        dataManager.register(VengeanceSpirit.WATCHERID_BUILDUP, super.getBuildupDuration());
        dataManager.register(VengeanceSpirit.WATCHERID_PLAYERID, super.getPlayerId());
        dataManager.register(VengeanceSpirit.WATCHERID_PLAYERNAME, super.getPlayerName());
    }

    @Override
    public String getInnerEntityName() {
        return dataManager.get(VengeanceSpirit.WATCHERID_INNER);
    }

    @Override
    public void setInnerEntityName(String innerEntityName) {
        dataManager.set(VengeanceSpirit.WATCHERID_INNER, innerEntityName);
    }

    @Override
    public int getRemainingLife() {
        return dataManager.get(VengeanceSpirit.WATCHERID_REMAININGLIFE);
    }

    @Override
    public void setRemainingLife(int remainingLife) {
        dataManager.set(VengeanceSpirit.WATCHERID_REMAININGLIFE, remainingLife);
    }

    @Override
    public int getFrozenDuration() {
        return dataManager.get(VengeanceSpirit.WATCHERID_FROZENDURATION);
    }

    @Override
    public void setFrozenDuration(int frozenDuration) {
        dataManager.set(VengeanceSpirit.WATCHERID_FROZENDURATION, frozenDuration);
    }

    @Override
    public String getPlayerId() {
        return dataManager.get(VengeanceSpirit.WATCHERID_PLAYERID);
    }

    @Override
    public void setPlayerId(String playerId) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_PLAYERID, playerId);
    }

    @Override
    public String getPlayerName() {
        return dataManager.get(VengeanceSpirit.WATCHERID_PLAYERNAME);
    }

    @Override
    public void setPlayerName(String playerName) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_PLAYERNAME, playerName);
    }

    @Override
    public boolean isSwarm() {
        return dataManager.get(VengeanceSpirit.WATCHERID_ISSWARM) == 1;
    }

    @Override
    public void setSwarm(boolean isSwarm) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_ISSWARM, isSwarm ? 1 : 0);
    }

    @Override
    public int getSwarmTier() {
        return dataManager.get(VengeanceSpirit.WATCHERID_SWARMTIER);
    }

    @Override
    public void setSwarmTier(int swarmTier) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_SWARMTIER, swarmTier);
    }

    public boolean isGlobalVengeance() {
        return dataManager.get(VengeanceSpirit.WATCHERID_GLOBALVENGEANCE) == 1;
    }

    public void setGlobalVengeance(boolean globalVengeance) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_GLOBALVENGEANCE, globalVengeance ? 1 : 0);
    }

    public String[] getVengeancePlayers() {
        String encodedPlayers = dataManager.get(VengeanceSpirit.WATCHERID_VENGEANCEPLAYERS);
        if(encodedPlayers.length() == 0) {
            return new String[0];
        }
        return encodedPlayers.split("&");
    }

    public void setVengeancePlayers(String[] vengeancePlayers) {
        this.dataManager.set(VengeanceSpirit.WATCHERID_VENGEANCEPLAYERS, StringUtils.join(vengeancePlayers, "&"));
    }
}


