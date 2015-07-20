package org.cyclops.evilcraft.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ExaltedCrafter;
import org.cyclops.evilcraft.network.packet.ExaltedCrafterOpenPacket;

/**
 * A {@link IKeyHandler} which handles farts.
 * 
 * @author immortaleeb
 *
 */
@SideOnly(Side.CLIENT)
public class ExaltedCrafterKeyHandler implements IKeyHandler {
	
	@Override
	public void onKeyPressed(KeyBinding kb) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if(kb == Keys.EXALTEDCRAFTING) {
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
				EvilCraft._instance.getPacketHandler().sendToServer(new ExaltedCrafterOpenPacket(found.getLeft()));
			}
		}
	}
	
}
