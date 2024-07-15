package org.cyclops.evilcraft.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;

/**
 * Updates the world shared tank cache for all clients.
 *
 * @author rubensworks
 *
 */
public class UpdateWorldSharedTankClientCachePacket extends PacketCodec<UpdateWorldSharedTankClientCachePacket> {

    public static final Type<UpdateWorldSharedTankClientCachePacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "update_world_shared_tank_client_cache"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateWorldSharedTankClientCachePacket> CODEC = getCodec(UpdateWorldSharedTankClientCachePacket::new);

    @CodecField
    private String tankID = null;
    @CodecField
    private FluidStack fluidStack = null;

    /**
     * Creates a packet with no content
     */
    public UpdateWorldSharedTankClientCachePacket() {
        super(ID);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    public UpdateWorldSharedTankClientCachePacket(String tankID, FluidStack fluidStack) {
        super(ID);
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
