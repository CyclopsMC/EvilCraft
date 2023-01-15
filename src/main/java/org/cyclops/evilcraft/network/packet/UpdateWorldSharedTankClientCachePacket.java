package org.cyclops.evilcraft.network.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    public void actionClient(Level world, Player player) {
        WorldSharedTankCache.getInstance().setTankContent(tankID, fluidStack);
    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {
        // Do nothing
    }

}
