package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nullable;

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
        setHasSubtypes(true);
    }

    public IBroomPart getPart(ItemStack itemStack) {
        return BroomParts.REGISTRY.getPartFromItem(itemStack);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        IBroomPart part = getPart(itemStack);
        if(part != null) {
            return part.getTranslationKey();
        }
        return super.getTranslationKey(itemStack);
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
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        for (IBroomPart part : BroomParts.REGISTRY.getParts()) {
            for (ItemStack itemStack : BroomParts.REGISTRY.getItemsFromPart(part)) {
                if (itemStack.getItem() == this) {
                    list.add(itemStack);
                }
            }
        }
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
        public int colorMultiplier(ItemStack itemStack, int renderPass) {
            IBroomPart part = BroomPart.getInstance().getPart(itemStack);
            if (part != null) {
                return part.getModelColor();
            }
            return -1;
        }
    }
}
