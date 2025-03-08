package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    public Type getType() {
        return type;
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        if(type.isActive()) {
            return new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_EMPTY);
        }
        return super.getCraftingRemainder(itemStack);
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
