package evilcraft.events;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.VersionStats;

/**
 * Event hook for Player Tick events.
 * @author rubensworks
 *
 */
public class PlayerTickEventHook implements ITickHandler {
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        VersionStats.check(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public String getLabel() {
        return Reference.MOD_ID;
    }
    
}
