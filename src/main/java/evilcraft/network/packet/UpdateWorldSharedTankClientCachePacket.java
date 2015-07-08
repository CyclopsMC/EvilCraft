package evilcraft.network.packet;

import evilcraft.core.fluid.WorldSharedTankCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Updates the world shared tank cache for all clients.
 * 
 * @author rubensworks
 *
 */
public class UpdateWorldSharedTankClientCachePacket extends PacketCodec {
	
	@CodecField
	private String tankID = null;
	@CodecField
	private int fluidId = 0;
	@CodecField
	private int fluidAmount = 0;

	/**
	 * Creates a packet with no content
	 */
	public UpdateWorldSharedTankClientCachePacket() {
		
	}
	
	/**
	 * Creates a packet which contains the fluid stack.
	 * @param tankID The id of the shared tank.
	 * @param fluidStack The fluid stack.
	 */
	public UpdateWorldSharedTankClientCachePacket(String tankID, FluidStack fluidStack) {
		this.tankID = tankID;
		if(fluidStack == null) {
			this.fluidId = -1;
			this.fluidAmount = -1;
		} else {
			this.fluidId = fluidStack.getFluidID();
			this.fluidAmount = fluidStack.amount;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		FluidStack fluidStack = null;
		if(fluidAmount >= 0 && fluidId >= 0) {
			fluidStack = new FluidStack(fluidId, fluidAmount);
		}
		WorldSharedTankCache.getInstance().setTankContent(tankID, fluidStack);
	}    

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		// Do nothing
	}

}
