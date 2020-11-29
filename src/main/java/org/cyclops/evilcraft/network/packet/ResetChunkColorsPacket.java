package org.cyclops.evilcraft.network.packet;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

public class ResetChunkColorsPacket extends PacketCodec {

    @CodecField
    private int chunkX;
    @CodecField
    private int chunkZ;

    public ResetChunkColorsPacket() {

    }

    public ResetChunkColorsPacket(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(World world, PlayerEntity player) {
        ((ClientWorld) world).onChunkLoaded(chunkX, chunkZ);
    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {

    }

}
