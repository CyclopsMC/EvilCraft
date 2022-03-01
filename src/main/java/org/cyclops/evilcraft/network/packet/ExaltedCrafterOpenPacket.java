package org.cyclops.evilcraft.network.packet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.evilcraft.item.ItemExaltedCrafter;

/**
 * Packet for clearing the exalted crafting grid.
 * @author rubensworks
 *
 */
public class ExaltedCrafterOpenPacket extends PacketCodec {

    @CodecField
    private int itemIndex = -1;
    @CodecField
    private boolean mainHand = true;

    /**
     * Make a new instance.
     */
    public ExaltedCrafterOpenPacket() {

    }

    /**
     * Make a new instance.
     * @param itemIndex The index of the crafter in the player inventory.
     * @param hand The hand the item is in.
     */
    public ExaltedCrafterOpenPacket(int itemIndex, InteractionHand hand) {
        this.itemIndex = itemIndex;
        this.mainHand = InteractionHand.MAIN_HAND.equals(hand);
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
        if(itemIndex >= 0) {
            ItemStack found;
            if (mainHand) {
                found = player.getInventory().items.get(itemIndex);
            } else {
                found = player.getOffhandItem();
            }
            if (!found.isEmpty() && found.getItem() instanceof ItemExaltedCrafter) {
                ((ItemExaltedCrafter) found.getItem()).openGuiForItemIndex(world, player, itemIndex, mainHand
                        ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            }
        }
    }

}
