package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Vengeance Essence.
 * @author rubensworks
 *
 */
public class VengeanceEssenceConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static VengeanceEssenceConfig _instance;

    /**
     * Make a new instance.
     */
    public VengeanceEssenceConfig() {
        super(
                EvilCraft._instance,
            true,
            "vengeance_essence",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableItem item = new ConfigurableItem(this) {
            @Override
            public EnumRarity getRarity(ItemStack itemStack) {
                return EnumRarity.EPIC;
            }

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            @SideOnly(Side.CLIENT)
            public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
                for(int i = 0; i < 2; i++) {
                    list.add(new ItemStack(item, 1, i));
                }
            }

            @Override
            public String getUnlocalizedName(ItemStack itemStack) {
                String suffix = "";
                if(itemStack.getItemDamage() == 1) suffix = ".materialized";
                return super.getUnlocalizedName(itemStack) + suffix;
            }
        };
        item.setHasSubtypes(true);
        item.setMaxDamage(0);
        return item;
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if (itemStack.getMetadata() == 1) {
            return super.getModelName(itemStack) + "_materialized";
        }
        return super.getModelName(itemStack);
    }
    
}
