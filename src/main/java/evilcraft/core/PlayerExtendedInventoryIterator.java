package evilcraft.core;

import evilcraft.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.Iterator;

/**
 * Iterate over a player's inventory and any other attached inventory like baubles.
 * @author rubensworks
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class PlayerExtendedInventoryIterator implements Iterator<ItemStack> {

    private PlayerInventoryIterator innerIt;
    private boolean hasIteratedInner = false;
    private int maxBaublesSize = 4;
    private int baublesIterator = maxBaublesSize;
    private EntityPlayer player;

    /**
     * Create a new HotbarIterator.
     * @param player The player to iterate the hotbar from.
     */
    public PlayerExtendedInventoryIterator(EntityPlayer player) {
        this.player = player;
        innerIt = new PlayerInventoryIterator(player);
        if(Loader.isModLoaded(Reference.MOD_BAUBLES)) {
            setBaublesData();
        }
    }

    @Override
    public boolean hasNext() {
        return !hasIteratedInner || baublesIterator < maxBaublesSize;
    }

    @Override
    public ItemStack next() {
        if(hasIteratedInner && hasNext()) {
            ItemStack itemStack = getBaublesStack(baublesIterator);
            baublesIterator++;
            return itemStack;
        } else {
            ItemStack next = innerIt.next();
            if(!innerIt.hasNext()) {
                hasIteratedInner = true;
            }
            return next;
        }
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    protected ItemStack getBaublesStack(int index) {
        return null;
        // TODO
        //return BaublesApi.getBaubles(player).getStackInSlot(index);
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    protected void setBaublesData() {
        // TODO
        //maxBaublesSize = BaublesApi.getBaubles(player).getSizeInventory();
        baublesIterator = 0;
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not implemented.");
    }

}
