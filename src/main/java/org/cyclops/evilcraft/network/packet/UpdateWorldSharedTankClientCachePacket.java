package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
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
	private FluidStack fluidStack = null;

	/**
	 * Creates a packet with no content
	 */
	public UpdateWorldSharedTankClientCachePacket() {
		
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	public UpdateWorldSharedTankClientCachePacket(String tankID, FluidStack fluidStack) {
		this.tankID = tankID;
		this.fluidStack = fluidStack;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {
		WorldSharedTankCache.getInstance().setTankContent(tankID, fluidStack);
	}    

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		// Do nothing
	}

}
