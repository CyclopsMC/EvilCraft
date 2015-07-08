package evilcraft.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.Helpers.IDType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * An alternative would be {@link SimpleNetworkWrapper}.
 * Partially based on the SecretRooms mod packet handling:
 * https://github.com/AbrarSyed/SecretRoomsMod-forge
 * @author rubensworks
 *
 */
@Sharable
public final class PacketHandler {

	private static Codec CODEC;
    private static final EnumMap<Side, FMLEmbeddedChannel> CHANNELS =
    		Maps.newEnumMap(Side.class);
	
    private PacketHandler() {
    	
    }
    
    /**
     * Initialize the packet handler.
     */
    public static void init() {
        if (!CHANNELS.isEmpty()) {
            return;
        }
        
        CODEC = new Codec();

        CHANNELS.putAll(NetworkRegistry.INSTANCE.newChannel(Reference.MOD_ID, CODEC, new HandlerServer()));
        
        // add handlers
        if (FMLCommonHandler.instance().getSide().isClient()) {
            // for the client
            FMLEmbeddedChannel channel = CHANNELS.get(Side.CLIENT);
            String codecName = channel.findChannelHandlerNameForType(Codec.class);
            channel.pipeline().addAfter(codecName, "ClientHandler", new HandlerClient());
        }
    }
    
    /**
     * Register a new packet.
     * @param packetType The class of the packet.
     */
    public static void register(Class<? extends PacketBase> packetType) {
    	CODEC.addDiscriminator(Helpers.getNewId(EvilCraft._instance, IDType.PACKET), packetType);
    }
    
    /**
     * Get the client-side channel.
     * @return The client channel.
     */
    public static FMLEmbeddedChannel getClientChannel() {
    	return CHANNELS.get(Side.CLIENT);
    }
    
    /**
     * Get the server-side channel.
     * @return The server channel.
     */
    public static FMLEmbeddedChannel getServerChannel() {
    	return CHANNELS.get(Side.SERVER);
    }
    
    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public static void sendToServer(PacketBase packet) {
    	getClientChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
    	getClientChannel().writeAndFlush(packet);
    }
    
    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public static void sendToPlayer(PacketBase packet, EntityPlayer player) {
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
    	getServerChannel().writeAndFlush(packet);
    }

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The area to send to.
     */
    public static void sendToAllAround(PacketBase packet, NetworkRegistry.TargetPoint point) {
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
    	getServerChannel().writeAndFlush(packet);
    }

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public static void sendToDimension(PacketBase packet, int dimension) {
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
    	getServerChannel().writeAndFlush(packet);
    }
    
    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public static void sendToAll(PacketBase packet) {
    	getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
    	getServerChannel().writeAndFlush(packet);
    }
    
    /**
     * Convert the given packet to a minecraft packet.
     * @param packet The packet.
     * @return The minecraft packet.
     */
    public static Packet toMcPacket(PacketBase packet) {
        return CHANNELS.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
    }
    
    /**
     * Coder/Decoder for using the FML messages in this system.
     * @author rubensworks
     *
     */
    private static final class Codec extends FMLIndexedMessageToMessageCodec<PacketBase> {
    	
        @Override
        public void encodeInto(ChannelHandlerContext ctx, PacketBase packet, ByteBuf target)
        		throws Exception {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            packet.encode(output);
            target.writeBytes(output.toByteArray());
        }

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, PacketBase packet) {
            ByteArrayDataInput input = ByteStreams.newDataInput(source.array());
            input.skipBytes(1); // skip the packet identifier byte
            packet.decode(input);
        }
        
    }
    
    @Sharable
    @SideOnly(Side.CLIENT)
    private static final class HandlerClient extends SimpleChannelInboundHandler<PacketBase> {
        
    	@Override
        protected void channelRead0(ChannelHandlerContext ctx, PacketBase packet)
        		throws Exception {
            Minecraft mc = Minecraft.getMinecraft();
            packet.actionClient(mc.theWorld, mc.thePlayer);
        }
    	
    }

    @Sharable
    private static final class HandlerServer extends SimpleChannelInboundHandler<PacketBase> {
        
    	@Override
        protected void channelRead0(ChannelHandlerContext ctx, PacketBase packet)
        		throws Exception {
            if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                // nothing on the client thread
                return;
            }
            
            EntityPlayerMP player = ((NetHandlerPlayServer) ctx.channel()
            		.attr(NetworkRegistry.NET_HANDLER).get()).playerEntity;
            packet.actionServer(player.worldObj, player);
        }
    	
    }
    
}
