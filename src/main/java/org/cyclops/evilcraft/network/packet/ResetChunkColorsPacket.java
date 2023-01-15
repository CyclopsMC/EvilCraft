package org.cyclops.evilcraft.network.packet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
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
    public void actionClient(Level world, Player player) {
        ((ClientLevel) world).onChunkLoaded(new ChunkPos(chunkX, chunkZ));
    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {

    }

}
