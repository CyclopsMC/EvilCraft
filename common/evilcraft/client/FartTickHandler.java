package evilcraft.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import evilcraft.Reference;

/**
 * A tick handler that is used to check the fart toggle keybinding and act accordingly.
 * @author rubensworks
 *
 */
public class FartTickHandler implements ITickHandler {
    private boolean fartEnabled = false;
    private boolean lastPressed = false;
    
    private static final int FART_SEND_DELAY = 20;  // Number of ticks before we can send the next fart packet
    private int cooldown = 0;
    
    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        playerTick((EntityPlayer)tickData[0]);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }
    
    @Override
    public String getLabel() {
        return "fartTickHandler";
    }
    
    private boolean isToggled() {
        boolean isPressed = CustomClientKeyHandler.isKeyPressed(Reference.KEY_FART);
        
        if (isPressed != lastPressed) {
            lastPressed = isPressed;
            return lastPressed;
        }
        
        return false;
    }
    
    private void playerTick(EntityPlayer player) {
        if (player != Minecraft.getMinecraft().thePlayer)
            return;
        
        if (isToggled()) {
            fartEnabled = !fartEnabled;
            
            if (fartEnabled)
                player.addChatMessage("Farting enabled");
            else
                player.addChatMessage("Farting disabled");
        }
        
        if (!isCooldown() && fartEnabled && player.isSneaking()) {
            // Note: we can get away with sending an empty packet because we only need the player data
            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Reference.MOD_CHANNEL, null));
            cooldown = FART_SEND_DELAY;
        }
    }
    
    private boolean isCooldown() {
        cooldown = cooldown <= 0 ? 0 : cooldown-1;
        
        return cooldown > 0;
    }
}
