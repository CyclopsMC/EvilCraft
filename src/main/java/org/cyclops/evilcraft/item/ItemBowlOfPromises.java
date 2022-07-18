package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A bowl of promises.
 * @author rubensworks
 *
 */
public class ItemBowlOfPromises extends Item {

    private final Type type;

    public ItemBowlOfPromises(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if(type.isActive()) {
            return new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_EMPTY);
        }
        return super.getCraftingRemainingItem(itemStack);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        return type.isActive();
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            if(((ItemBowlOfPromises) itemStack.getItem()).type.isActive() && renderPass == 0) {
                float division = (((float) (((Type.values().length - 2) -
                        (((ItemBowlOfPromises) itemStack.getItem()).type.getTier())) - 1) / 3) + 1);
                int channel = (int) (255 / division);
                return Helpers.RGBToInt(channel, channel, channel);
            }
            return -1;
        }
    }

    public static enum Type {
        EMPTY("empty", false, 0),
        DUSTED("dusted", false, 0),
        TIER0("tier0", true, 0),
        TIER1("tier1",true, 1),
        TIER2("tier2",true, 2),
        TIER3("tier3",true, 3);

        private final String name;
        private final boolean active;
        private final int tier;

        Type(String name, boolean active, int tier) {
            this.name = name;
            this.active = active;
            this.tier = tier;
        }

        public String getName() {
            return name;
        }

        public boolean isActive() {
            return active;
        }

        public int getTier() {
            return tier;
        }
    }

}
