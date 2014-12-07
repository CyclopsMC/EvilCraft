package evilcraft.network.packet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.inventory.container.ContainerExaltedCrafter;
import evilcraft.network.CodecField;
import evilcraft.network.PacketCodec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

/**
 * Packet for sending a button packet for the exalted crafting.
 * @author rubensworks
 *
 */
public class ExaltedCrafterButtonPacket extends PacketCodec {

    @CodecField
    private int buttonId;

    public ExaltedCrafterButtonPacket() {

    }

    public ExaltedCrafterButtonPacket(int buttonId) {
        this.buttonId = buttonId;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(player.openContainer instanceof ContainerExaltedCrafter) {
			((ContainerExaltedCrafter) player.openContainer).executePressButton(buttonId);
		}
	}
	
}