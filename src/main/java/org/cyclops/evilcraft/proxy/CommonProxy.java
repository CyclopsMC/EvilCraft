package org.cyclops.evilcraft.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.event.EntityStruckByLightningEventHook;
import org.cyclops.evilcraft.event.LivingUpdateEventHook;
import org.cyclops.evilcraft.network.packet.ExaltedCrafterOpenPacket;
import org.cyclops.evilcraft.network.packet.FartPacket;
import org.cyclops.evilcraft.network.packet.ResetChunkColorsPacket;
import org.cyclops.evilcraft.network.packet.SanguinaryPedestalBlockReplacePacket;
import org.cyclops.evilcraft.network.packet.UpdateWorldSharedTankClientCachePacket;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return EvilCraft._instance;
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);

        // Register packets.
        packetHandler.register(FartPacket.ID, FartPacket::new);
        packetHandler.register(SanguinaryPedestalBlockReplacePacket.ID, SanguinaryPedestalBlockReplacePacket::new);
        packetHandler.register(ExaltedCrafterOpenPacket.ID, ExaltedCrafterOpenPacket::new);
        packetHandler.register(UpdateWorldSharedTankClientCachePacket.ID, UpdateWorldSharedTankClientCachePacket::new);
        packetHandler.register(ResetChunkColorsPacket.ID, ResetChunkColorsPacket::new);

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
