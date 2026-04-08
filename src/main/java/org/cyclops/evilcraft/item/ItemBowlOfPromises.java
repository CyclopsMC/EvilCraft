package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A bowl of promises.
 * @author rubensworks
 *
 */
public class ItemBowlOfPromises extends Item {

    private final Type type;

    public ItemBowlOfPromises(Properties properties, Type type) {
        super(type.isActive() ? properties.craftRemainder(new ItemStackTemplate(RegistryEntries.ITEM_BOWL_OF_PROMISES_EMPTY, 1, DataComponentPatch.EMPTY)) : properties);
        this.type = type;
    }

    public Type getType() {
        return type;
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
