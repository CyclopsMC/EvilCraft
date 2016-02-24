package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

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
	private String fluidName = null;
	@CodecField
	private int fluidAmount = 0;

	/**
	 * Creates a packet with no content
	 */
	public UpdateWorldSharedTankClientCachePacket() {
		
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	/**
	 * Creates a packet which contains the fluid stack.
	 * @param tankID The id of the shared tank.
	 * @param fluidStack The fluid stack.
	 */
	public UpdateWorldSharedTankClientCachePacket(String tankID, FluidStack fluidStack) {
		this.tankID = tankID;
		if(fluidStack == null) {
			this.fluidName = "";
			this.fluidAmount = -1;
		} else {
			this.fluidName = fluidStack.getFluid().getName();
			this.fluidAmount = fluidStack.amount;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		FluidStack fluidStack = null;
		Fluid fluid;
		if(fluidAmount >= 0 && fluidName != null && (fluid = FluidRegistry.getFluid(fluidName)) != null) {
			fluidStack = new FluidStack(fluid, fluidAmount);
        }
		WorldSharedTankCache.getInstance().setTankContent(tankID, fluidStack);
	}    

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		// Do nothing
	}

}
