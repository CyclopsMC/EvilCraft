package org.cyclops.evilcraft.proxy;

import net.minecraftforge.common.MinecraftForge;
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
        packetHandler.register(FartPacket.class);
        packetHandler.register(SanguinaryPedestalBlockReplacePacket.class);
        packetHandler.register(ExaltedCrafterOpenPacket.class);
        packetHandler.register(UpdateWorldSharedTankClientCachePacket.class);
        packetHandler.register(ResetChunkColorsPacket.class);

        EvilCraft.clog("Registered packet handler.");
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        MinecraftForge.EVENT_BUS.register(new EntityStruckByLightningEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingUpdateEventHook());
        MinecraftForge.EVENT_BUS.register(WorldSharedTankCache.getInstance());
    }

}
