package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

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
	public boolean isAsync() {
		return false;
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