package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.item.ExaltedCrafter;

/**
 * Packet for clearing the exalted crafting grid.
 * @author rubensworks
 *
 */
public class ExaltedCrafterOpenPacket extends PacketCodec {
    
	@CodecField
	private int itemIndex = -1;
	@CodecField
	private boolean mainHand = true;
	
	/**
	 * Make a new instance.
	 */
	public ExaltedCrafterOpenPacket() {
		
	}
	
	/**
	 * Make a new instance.
	 * @param itemIndex The index of the crafter in the player inventory.
	 * @param hand The hand the item is in.
	 */
	public ExaltedCrafterOpenPacket(int itemIndex, EnumHand hand) {
		this.itemIndex = itemIndex;
		this.mainHand = EnumHand.MAIN_HAND.equals(hand);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		
	}
	
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(itemIndex >= 0) {
			ItemStack found;
			if (mainHand) {
				found = player.inventory.mainInventory.get(itemIndex);
			} else {
				found = player.getHeldItemOffhand();
			}
			if (!found.isEmpty() && found.getItem() == ExaltedCrafter.getInstance()) {
				ExaltedCrafter.getInstance().openGuiForItemIndex(world, player, itemIndex, mainHand
						? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			}
		}
	}
	
}