package org.cyclops.evilcraft.network.packet;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;

/**
 * Packet for playing a sound at a location.
 * @author rubensworks
 *
 */
public class SanguinaryPedestalBlockReplacePacket extends PacketCodec {

	public static final int RANGE = 15;
	
	@CodecField
	private double x = 0;
    @CodecField
	private double y = 0;
    @CodecField
	private double z = 0;
    
    /**
     * Empty packet.
     */
    public SanguinaryPedestalBlockReplacePacket() {
    	
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	public SanguinaryPedestalBlockReplacePacket(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
    
	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {
		world.playLocalSound(x, y, z, RegistryEntries.BLOCK_BLOOD_STAIN.defaultBlockState().getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.1F + world.random.nextFloat() * 0.5F,
    			0.9F + world.random.nextFloat() * 0.1F, false);
		ParticleBloodSplash.spawnParticles(world, new BlockPos((int) x, (int) y + 1, (int) z), 3 + world.random.nextInt(2), 1 + world.random.nextInt(2));
	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {

	}
	
}