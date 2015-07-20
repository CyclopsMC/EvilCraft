package org.cyclops.evilcraft.core.degradation;

import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.api.degradation.IDegradationRegistry;

/**
 * An executor component to be used by {@link IDegradable}.
 * @author rubensworks
 *
 */
public class DegradationExecutor {
    
    private static final String ROOT_TAG = "degradationExecutor";
    private static final int DEFAULT_TICK_INTERVAL = 1;
    
    private int tickInterval = DEFAULT_TICK_INTERVAL;
    private int currentTick = 0;
    
    private IDegradable degradable;
    
    /**
     * Make a new instance.
     * @param degradable The {@link IDegradable} this executor applies to.
     */
    public DegradationExecutor(IDegradable degradable) {
        this.degradable = degradable;
    }
    
    /**
     * Execute a random {@link IDegradationEffect} if the tick is at the correct value that
     * as defined in {@link DegradationExecutor#getTickInterval()}. Each time this method
     * is called, the tick is increased.
     * @param isRemote True for clients, false for servers.
     * @return If a random effect was executed, can be false if the tick is too small or the
     * randomly chosen effect can not be run.
     */
    public boolean runRandomEffect(boolean isRemote) {
        currentTick++;
        if(currentTick >= tickInterval) {
            currentTick = 0;
            IDegradationEffect effect = EvilCraft._instance.getRegistryManager().getRegistry(IDegradationRegistry.class).getRandomDegradationEffect();
            if(effect.canRun(degradable)) {
                if(isRemote) {
                    effect.runClientSide(degradable);
                } else {
                    effect.runServerSide(degradable);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Reads the data for this executor from NBT.
     * @param compound The tag to read from.
     */
    public void readFromNBT(NBTTagCompound compound) {
        this.tickInterval = compound.getCompoundTag(ROOT_TAG).getInteger("tickInterval");
        this.currentTick = compound.getCompoundTag(ROOT_TAG).getInteger("currentTick");
    }
    
    /**
     * Writes the data for this executor to NBT.
     * @param compound The tag to write to.
     */
    public void writeToNBT(NBTTagCompound compound) {
        NBTTagCompound content = new NBTTagCompound();
        content.setInteger("tickInterval", tickInterval);
        content.setInteger("currentTick", currentTick);
        compound.setTag(ROOT_TAG, content);
    }

    /**
     * @return the tickInterval
     */
    public int getTickInterval() {
        return tickInterval;
    }

    /**
     * @param tickInterval the tickInterval to set
     */
    public void setTickInterval(int tickInterval) {
        this.tickInterval = tickInterval;
    }
    
}
