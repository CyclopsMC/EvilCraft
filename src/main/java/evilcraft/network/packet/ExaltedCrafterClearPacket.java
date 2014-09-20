package evilcraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.inventory.container.ContainerExaltedCrafter;
import evilcraft.network.PacketCodec;

/**
 * Packet for clearing the exalted crafting grid.
 * @author rubensworks
 *
 */
public class ExaltedCrafterClearPacket extends PacketCodec {
    
	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(player.openContainer instanceof ContainerExaltedCrafter) {
			((ContainerExaltedCrafter) player.openContainer).clearGrid();
		}
	}
	
}