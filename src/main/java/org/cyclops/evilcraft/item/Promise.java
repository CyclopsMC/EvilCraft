package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Promise item singleton.
 * Used for machine upgrades.
 * 
 * @author rubensworks
 *
 */
public class Promise extends ConfigurableItem {

    private static Promise _instance = null;
    public static final Upgrades.Upgrade[] UPGRADES = new Upgrades.Upgrade[]{
            WorkingTileEntity.UPGRADE_TIER1,
            WorkingTileEntity.UPGRADE_TIER2,
            WorkingTileEntity.UPGRADE_TIER3,
            WorkingTileEntity.UPGRADE_SPEED,
            WorkingTileEntity.UPGRADE_EFFICIENCY
    };
    public static final Map<Upgrades.Upgrade, Integer> MAIN_COLORS = Maps.newHashMap();
    public static final Map<Upgrades.Upgrade, Integer> SECONDARY_COLORS = Maps.newHashMap();
    static {
        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER1, Helpers.RGBToInt(220, 220, 220));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER1, Helpers.RGBToInt(255, 255, 255));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER2, Helpers.RGBToInt(234, 238, 87));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER2, Helpers.RGBToInt(230, 230, 160));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER3, Helpers.RGBToInt(51, 235, 203));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER3, Helpers.RGBToInt(150, 250, 200));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_SPEED, Helpers.RGBToInt(200, 90, 80));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_SPEED, Helpers.RGBToInt(240, 120, 110));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_EFFICIENCY, Helpers.RGBToInt(80, 70, 200));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_EFFICIENCY, Helpers.RGBToInt(120, 120, 210));
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Promise getInstance() {
        return _instance;
    }

    public Promise(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(4);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        if(itemStack.getItemDamage() <= 2) { // All the 'tier' upgrades can only have stacksize 1.
            return 1;
        }
        return super.getItemStackLimit(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(TextFormatting.DARK_GREEN + L10NHelpers.localize(super.getUnlocalizedName(itemStack) + ".useIn"));
            for(BlockConfig upgradable : getUpgrade(itemStack).getUpgradables()) {
                list.add(TextFormatting.ITALIC + L10NHelpers.localize("tile." + upgradable.getUnlocalizedName() + ".name"));
            }
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < UPGRADES.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack) + "." + getUpgrade(itemStack).getId();
    }

    /**
     * Get the upgrade for given damage.
     * @param itemStack The item.
     * @return The upgrade instance.
     */
    public Upgrades.Upgrade getUpgrade(ItemStack itemStack) {
        return UPGRADES[Math.min(UPGRADES.length - 1, itemStack.getItemDamage())];
    }

    /**
     * @param itemStack The item.
     * @return If the upgrade is a tier upgrade.
     */
    public boolean isTierUpgrade(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemDamage() <= 2;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return itemStack.getItemDamage() < 3 ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return new ItemColor();
    }

    @SideOnly(Side.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack itemStack, int renderPass) {
            Upgrades.Upgrade upgrade = Promise.getInstance().getUpgrade(itemStack);
            return renderPass == 0 ? SECONDARY_COLORS.get(upgrade) : MAIN_COLORS.get(upgrade);
        }
    }

}
