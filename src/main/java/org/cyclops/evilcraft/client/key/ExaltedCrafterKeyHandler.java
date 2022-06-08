package org.cyclops.evilcraft.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.key.IKeyHandler;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
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
            ItemLocation found = null;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
            while(it.hasNext() && found == null) {
                ItemLocation itemLocation = it.nextIndexed();
                if(itemLocation.getItemStack(player).getItem() instanceof ItemExaltedCrafter) {
                    found = itemLocation;
                }
            }
            if(found != null) {
                EvilCraft._instance.getPacketHandler().sendToServer(new ExaltedCrafterOpenPacket(found));
            }
        }
    }

}
