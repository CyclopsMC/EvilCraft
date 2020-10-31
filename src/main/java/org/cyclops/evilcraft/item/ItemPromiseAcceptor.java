package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;

import java.util.List;

/**
 * Blood reactant.
 * 
 * @author rubensworks
 *
 */
public class ItemPromiseAcceptor extends Item {

    public static final List<Integer> COLORS = Lists.newArrayList(
            Helpers.RGBToInt(255, 255, 255),
            Helpers.RGBToInt(230, 230, 160),
            Helpers.RGBToInt(150, 250, 200)
    );

    private final int color;

    public ItemPromiseAcceptor(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    public int getColor() {
        return this.color;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return COLORS.get(((ItemPromiseAcceptor) itemStack.getItem()).getColor());
        }
    }

}
