package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import java.util.List;

/**
 * Item-form of a broom part.
 * @author rubensworks
 *
 */
public class BroomPart extends ConfigurableItem {

    private static BroomPart _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BroomPart getInstance() {
        return _instance;
    }

    public BroomPart(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    public IBroomPart getPart(ItemStack itemStack) {
        return BroomParts.REGISTRY.getPartFromItem(itemStack);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        IBroomPart part = getPart(itemStack);
        if(part != null) {
            return part.getUnlocalizedName();
        }
        return super.getUnlocalizedName(itemStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return part.getRarity();
        }
        return super.getRarity(stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return part.isEffect();
        }
        return super.hasEffect(stack);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (IBroomPart part : BroomParts.REGISTRY.getParts()) {
            ItemStack itemStack = BroomParts.REGISTRY.getItemFromPart(part);
            if (itemStack != null && itemStack.getItem() == this) {
                subItems.add(itemStack);
            }
        }
    }
}
