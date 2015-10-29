package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.api.gameevent.BloodInfuserRemoveEvent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

/**
 * Container for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class ContainerBloodInfuser extends ContainerTileWorking<TileBloodInfuser> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;
    
    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 8;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 36;
    
    /**
     * Infuse slot X coordinate.
     */
    public static final int SLOT_INFUSE_X = 79;
    /**
     * Infuse slot Y coordinate.
     */
    public static final int SLOT_INFUSE_Y = 36;
    
    /**
     * Infuse result slot X coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_X = 133;
    /**
     * Infuse result slot Y coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_Y = 36;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileBloodInfuser.SLOT_CONTAINER, SLOT_CONTAINER_X, SLOT_CONTAINER_Y, tile.getTank())); // Container emptier
        addSlotToContainer(new SlotWorking<TileBloodInfuser>(TileBloodInfuser.SLOT_INFUSE, SLOT_INFUSE_X, SLOT_INFUSE_Y, tile)); // Infuse slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileBloodInfuser.SLOT_INFUSE_RESULT, SLOT_INFUSE_RESULT_X, SLOT_INFUSE_RESULT_Y) {

            public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
                IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>
                        recipe = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipeByOutput(new ItemStackRecipeComponent(itemStack));
                if(recipe != null) {
                    EntityHelpers.spawnXpAtPlayer(player.worldObj, player, (int) Math.floor(recipe.getProperties().getXp() * itemStack.stackSize));
                    FMLCommonHandler.instance().bus().post(new BloodInfuserRemoveEvent(player, itemStack));
                }
                super.onPickupFromSlot(player, itemStack);
            }

        }); // Infuse result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y);

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}