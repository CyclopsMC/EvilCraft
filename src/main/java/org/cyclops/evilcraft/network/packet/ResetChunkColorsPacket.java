package org.cyclops.evilcraft.network.packet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.Reference;

public class ResetChunkColorsPacket extends PacketCodec {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "reset_chunk_colors");

    @CodecField
    private int chunkX;
    @CodecField
    private int chunkZ;

    public ResetChunkColorsPacket() {
        super(ID);
    }

    public ResetChunkColorsPacket(int chunkX, int chunkZ) {
        super(ID);
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
