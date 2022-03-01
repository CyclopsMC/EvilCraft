package org.cyclops.evilcraft.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemExaltedCrafter;
import org.cyclops.evilcraft.network.packet.ExaltedCrafterOpenPacket;

/**
 * A {@link IKeyHandler} which handles farts.
 *
 * @author immortaleeb
 *
 */
@OnlyIn(Dist.CLIENT)
public class ExaltedCrafterKeyHandler implements IKeyHandler {

    @Override
    public void onKeyPressed(KeyMapping kb) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(kb == Keys.EXALTEDCRAFTING) {
            Triple<Integer, InteractionHand, ItemStack> found = null;
            PlayerInventoryIterator it = new PlayerInventoryIterator(player);
            while(it.hasNext() && found == null) {
                Pair<Integer, ItemStack> pair = it.nextIndexed();
                if(pair.getRight() != null && pair.getRight().getItem() instanceof ItemExaltedCrafter) {
                    found = Triple.of(pair.getLeft(), InteractionHand.MAIN_HAND, pair.getRight());
                }
            }
            if(found == null) {
                if (player.getOffhandItem().getItem() instanceof ItemExaltedCrafter) {
                    found = Triple.of(0, InteractionHand.OFF_HAND, player.getOffhandItem());
                }
            }
            if(found != null) {
                EvilCraft._instance.getPacketHandler().sendToServer(new ExaltedCrafterOpenPacket(found.getLeft(), found.getMiddle()));
            }
        }
    }

}
