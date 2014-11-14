package evilcraft.tileentity;

import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.core.tileentity.upgrade.Upgrades;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Set;

/**
 * Evilcraft working tile entity with upgrade declaration.
 * @author rubensworks
 */
public abstract class TileWorking<T extends TankInventoryTileEntity> extends WorkingTileEntity<T> {

    private static final Upgrades.Upgrade[] METADATA_UPGRADES = new Upgrades.Upgrade[] {
            UPGRADE_TIER, UPGRADE_SPEED, UPGRADE_EFFICIENCY
    };
    public static final Item UPGRADE_ITEM = Items.gold_ingot; // TODO

    private Set<Upgrades.Upgrade> upgradeTypes;

    /**
     * Make a new instance.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     * @param upgradeTypes The types of upgrade items.
     */
    public TileWorking(int inventorySize, String inventoryName,
                       int tankSize, String tankName, Fluid acceptedFluid,
                       Set<Upgrades.Upgrade> upgradeTypes) {
        super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
        this.upgradeTypes = upgradeTypes;
    }

    public Upgrades.Upgrade getUpgradeType(ItemStack itemStack) {
        if(itemStack.getItem() == UPGRADE_ITEM) {
            // TODO: Upgrades.Upgrade upgrade = METADATA_UPGRADES[itemStack.getItemDamage()];
            Upgrades.Upgrade upgrade = UPGRADE_SPEED;
            if(upgradeTypes.contains(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    public int getUpgradeLevel(ItemStack itemStack) {
        return itemStack.stackSize;
    }

}
