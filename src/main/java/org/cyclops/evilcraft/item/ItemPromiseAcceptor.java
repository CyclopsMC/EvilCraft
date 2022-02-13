package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;

import net.minecraft.item.Item.Properties;

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
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    public Type getType() {
        return this.type;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return ((ItemPromiseAcceptor) itemStack.getItem()).getType().getColor();
        }
    }

    public static enum Type {
        IRON("iron", Helpers.RGBToInt(255, 255, 255)),
        GOLD("gold", Helpers.RGBToInt(230, 230, 160)),
        DIAMOND("diamond", Helpers.RGBToInt(150, 250, 200));

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
