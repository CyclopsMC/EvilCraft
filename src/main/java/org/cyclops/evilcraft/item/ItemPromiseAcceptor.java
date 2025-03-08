package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;

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
        IRON("iron", IModHelpers.get().getBaseHelpers().RGBAToInt(255, 255, 255, 255)),
        GOLD("gold", IModHelpers.get().getBaseHelpers().RGBAToInt(230, 230, 160, 255)),
        DIAMOND("diamond", IModHelpers.get().getBaseHelpers().RGBAToInt(150, 250, 200, 255));

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
