package org.cyclops.evilcraft.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.event.EntityStruckByLightningEventHook;
import org.cyclops.evilcraft.event.LivingUpdateEventHook;
import org.cyclops.evilcraft.network.packet.*;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBaseNeoForge<EvilCraft> getMod() {
        return EvilCraft._instance;
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(FartPacket.class, FartPacket.ID, FartPacket.CODEC);
        packetHandler.register(SanguinaryPedestalBlockReplacePacket.class, SanguinaryPedestalBlockReplacePacket.ID, SanguinaryPedestalBlockReplacePacket.CODEC);
        packetHandler.register(ExaltedCrafterOpenPacket.class, ExaltedCrafterOpenPacket.ID, ExaltedCrafterOpenPacket.CODEC);
        packetHandler.register(UpdateWorldSharedTankClientCachePacket.class, UpdateWorldSharedTankClientCachePacket.ID, UpdateWorldSharedTankClientCachePacket.CODEC);
        packetHandler.register(ResetChunkColorsPacket.class, ResetChunkColorsPacket.ID, ResetChunkColorsPacket.CODEC);

        EvilCraft.clog("Registered packet handler.");
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        NeoForge.EVENT_BUS.register(new EntityStruckByLightningEventHook());
        NeoForge.EVENT_BUS.register(new LivingUpdateEventHook());
        NeoForge.EVENT_BUS.register(WorldSharedTankCache.getInstance());
    }

}
