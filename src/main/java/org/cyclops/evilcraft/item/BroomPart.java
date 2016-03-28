package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
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
        MinecraftForge.EVENT_BUS.register(this);
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
            if(itemStack.getItem() == this) {
                subItems.add(itemStack);
            }
        }
    }

    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event) {
        IBroomPart part = getPart(event.itemStack);
        if(part != null) {
            if(MinecraftHelpers.isShifted()) {
                event.toolTip.add(L10NHelpers.localize("broom.parts." + Reference.MOD_ID + ".type.name",
                        L10NHelpers.localize(part.getType().getUnlocalizedName())));
            } else {
                event.toolTip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.evilcraft.shiftinfo"));
            }
        }
    }
}
