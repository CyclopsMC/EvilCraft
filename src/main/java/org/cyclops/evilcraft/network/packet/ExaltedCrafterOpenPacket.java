package org.cyclops.evilcraft.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.ItemExaltedCrafter;

/**
 * Packet for clearing the exalted crafting grid.
 * @author rubensworks
 *
 */
public class ExaltedCrafterOpenPacket extends PacketCodec<ExaltedCrafterOpenPacket> {

    public static final Type<ExaltedCrafterOpenPacket> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "exalted_crafter_open"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExaltedCrafterOpenPacket> CODEC = getCodec(ExaltedCrafterOpenPacket::new);

    @CodecField
    private ItemLocation itemLocation = null;

    /**
     * Make a new instance.
     */
    public ExaltedCrafterOpenPacket() {
        super(ID);
    }

    public ExaltedCrafterOpenPacket(ItemLocation itemLocation) {
        super(ID);
        this.itemLocation = itemLocation;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level world, Player player) {

    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {
        if(itemLocation != null) {
            ItemStack found = itemLocation.getItemStack(player);
            if (!found.isEmpty() && found.getItem() instanceof ItemExaltedCrafter) {
                ((ItemExaltedCrafter) found.getItem()).openGuiForItemIndex(world, player, itemLocation);
            }
        }
    }

}
