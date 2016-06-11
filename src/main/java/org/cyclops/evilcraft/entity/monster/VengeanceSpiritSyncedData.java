package org.cyclops.evilcraft.entity.monster;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.apache.commons.lang3.StringUtils;

public class VengeanceSpiritSyncedData extends VengeanceSpiritData {
    private static final DataParameter<String> WATCHERID_INNER = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    private static final DataParameter<Integer> WATCHERID_REMAININGLIFE = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WATCHERID_FROZENDURATION = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WATCHERID_GLOBALVENGEANCE = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<String> WATCHERID_VENGEANCEPLAYERS = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    private static final DataParameter<Integer> WATCHERID_ISSWARM = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WATCHERID_SWARMTIER = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WATCHERID_BUILDUP = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    private static final DataParameter<String> WATCHERID_PLAYERID = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    private static final DataParameter<String> WATCHERID_PLAYERNAME = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);

    private EntityDataManager dataManager;

    public VengeanceSpiritSyncedData(EntityDataManager dataManager) {
        super();

        this.dataManager = dataManager;
        register(dataManager);
    }

    public void register(EntityDataManager dataManager) {
        dataManager.register(WATCHERID_INNER, super.getInnerEntityName());
        dataManager.register(WATCHERID_REMAININGLIFE, super.getRemainingLife());
        dataManager.register(WATCHERID_FROZENDURATION, super.getFrozenDuration());
        dataManager.register(WATCHERID_GLOBALVENGEANCE, 0);
        dataManager.register(WATCHERID_VENGEANCEPLAYERS, "");
        dataManager.register(WATCHERID_ISSWARM, super.isSwarm() ? 1 : 0);
        dataManager.register(WATCHERID_SWARMTIER, super.getSwarmTier());
        dataManager.register(WATCHERID_BUILDUP, super.getBuildupDuration());
        dataManager.register(WATCHERID_PLAYERID, super.getPlayerId());
        dataManager.register(WATCHERID_PLAYERNAME, super.getPlayerName());
    }

    @Override
    public String getInnerEntityName() {
        return dataManager.get(WATCHERID_INNER);
    }

    @Override
    public void setInnerEntityName(String innerEntityName) {
        dataManager.set(WATCHERID_INNER, innerEntityName);
    }

    @Override
    public int getRemainingLife() {
        return dataManager.get(WATCHERID_REMAININGLIFE);
    }

    @Override
    public void setRemainingLife(int remainingLife) {
        dataManager.set(WATCHERID_REMAININGLIFE, remainingLife);
    }

    @Override
    public int getFrozenDuration() {
        return dataManager.get(WATCHERID_FROZENDURATION);
    }

    @Override
    public void setFrozenDuration(int frozenDuration) {
        dataManager.set(WATCHERID_FROZENDURATION, frozenDuration);
    }

    @Override
    public String getPlayerId() {
        return dataManager.get(WATCHERID_PLAYERID);
    }

    @Override
    public void setPlayerId(String playerId) {
        this.dataManager.set(WATCHERID_PLAYERID, playerId);
    }

    @Override
    public String getPlayerName() {
        return dataManager.get(WATCHERID_PLAYERNAME);
    }

    @Override
    public void setPlayerName(String playerName) {
        this.dataManager.set(WATCHERID_PLAYERNAME, playerName);
    }

    @Override
    public boolean isSwarm() {
        return dataManager.get(WATCHERID_ISSWARM) == 1;
    }

    @Override
    public void setSwarm(boolean isSwarm) {
        this.dataManager.set(WATCHERID_ISSWARM, isSwarm ? 1 : 0);
    }

    @Override
    public int getSwarmTier() {
        return dataManager.get(WATCHERID_SWARMTIER);
    }

    @Override
    public void setSwarmTier(int swarmTier) {
        this.dataManager.set(WATCHERID_SWARMTIER, swarmTier);
    }

    public boolean isGlobalVengeance() {
        return dataManager.get(WATCHERID_GLOBALVENGEANCE) == 1;
    }

    public void setGlobalVengeance(boolean globalVengeance) {
        this.dataManager.set(WATCHERID_GLOBALVENGEANCE, globalVengeance ? 1 : 0);
    }

    public String[] getVengeancePlayers() {
        String encodedPlayers = dataManager.get(WATCHERID_VENGEANCEPLAYERS);
        if(encodedPlayers.length() == 0) {
            return new String[0];
        }
        return encodedPlayers.split("&");
    }

    public void setVengeancePlayers(String[] vengeancePlayers) {
        this.dataManager.set(WATCHERID_VENGEANCEPLAYERS, StringUtils.join(vengeancePlayers, "&"));
    }
}


