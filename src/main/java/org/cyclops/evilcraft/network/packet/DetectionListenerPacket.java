package org.cyclops.evilcraft.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.particle.ParticleBloodBrickData;

/**
 * Packet for telling clients if a structure has been formed for a blockState location.
 * @author rubensworks
 *
 */
public class DetectionListenerPacket extends PacketCodec {

	private static final int RANGE = 100;
	
	@CodecField
	private int x = 0;
    @CodecField
	private int y = 0;
    @CodecField
	private int z = 0;
    @CodecField
	private boolean activation = true;
    
    /**
     * Empty packet.
     */
    public DetectionListenerPacket() {
    	
    }
    
    /**
	 * Creates a packet with coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param activation If the structure is being activated, otherwise deactivated.
	 */
	public DetectionListenerPacket(int x, int y, int z, boolean activation) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.activation = activation;
	}
	
	/**
	 * Creates a packet which contains the location data.
	 * @param location The location data.
	 * @param activation If the structure is being activated, otherwise deactivated.
	 */
	public DetectionListenerPacket(BlockPos location, boolean activation) {
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.activation = activation;
	}

	@Override
	public boolean isAsync() {
		return false;
	}

    @OnlyIn(Dist.CLIENT)
    private void showActivatedParticle(World world, int x, int y, int z, Direction side) {
		world.addParticle(new ParticleBloodBrickData(side), x, y, z, 0, 0, 0);
	}
    
	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {
		if(activation) {
			for(Direction side : Direction.values()) {
				showActivatedParticle(world, x, y, z, side);
			}
		}
	}
	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		EvilCraft._instance.getPacketHandler().sendToAllAround(new DetectionListenerPacket(x, y, z, activation),
				LocationHelpers.createTargetPointFromEntity(player, RANGE));
	}
	
}