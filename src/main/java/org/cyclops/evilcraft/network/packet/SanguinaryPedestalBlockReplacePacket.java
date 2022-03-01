package org.cyclops.evilcraft.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public void actionClient(Level world, Player player) {
        world.playLocalSound(x, y, z, RegistryEntries.BLOCK_BLOOD_STAIN.defaultBlockState().getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.1F + world.random.nextFloat() * 0.5F,
                0.9F + world.random.nextFloat() * 0.1F, false);
        ParticleBloodSplash.spawnParticles(world, new BlockPos((int) x, (int) y + 1, (int) z), 3 + world.random.nextInt(2), 1 + world.random.nextInt(2));
    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {

    }

}
