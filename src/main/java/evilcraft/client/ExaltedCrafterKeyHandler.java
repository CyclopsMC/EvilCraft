package evilcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.PlayerInventoryIterator;
import evilcraft.item.ExaltedCrafter;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.ExaltedCrafterOpenPacket;

/**
 * A {@link KeyHandler} which handles farts.
 * 
 * @author immortaleeb
 *
 */
@SideOnly(Side.CLIENT)
public class ExaltedCrafterKeyHandler implements KeyHandler {
	
	@Override
	public void onKeyPressed(KeyBinding kb) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if(kb == Keys.EXALTEDCRAFTING.keyBinding) {
			Pair<Integer, ItemStack> found = null;
			PlayerInventoryIterator it = new PlayerInventoryIterator(player);
			while(it.hasNext() && found == null) {
				Pair<Integer, ItemStack> pair = it.nextIndexed();
				if(pair.getRight() != null && pair.getRight().getItem() == ExaltedCrafter.getInstance()) {
					found = pair;
				}
			}
			if(found != null) {
				ExaltedCrafter.getInstance().openGuiForItemIndex(Minecraft.getMinecraft().theWorld, player, found.getLeft());
				PacketHandler.sendToServer(new ExaltedCrafterOpenPacket(found.getLeft()));
			}
		}
	}
	
}
