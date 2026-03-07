package org.cyclops.evilcraft.item;

import net.minecraft.util.ARGB;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Blood reactant.
 *
 * @author rubensworks
 *
 */
public class ItemPromiseAcceptor extends Item {

    private final Type type;

    public ItemPromiseAcceptor(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        IRON("iron", ARGB.color(255, 255, 255, 255)),
        GOLD("gold", ARGB.color(255, 230, 230, 160)),
        DIAMOND("diamond", ARGB.color(255, 150, 250, 200));

        private final String name;
        private final int color;

        Type(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }
    }

}
