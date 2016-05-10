package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;

/**
 * A renderer for a throwable item.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderThrowable extends RenderSnowball<EntityThrowable> {

    public RenderThrowable(RenderManager renderManager, Item item, RenderItem renderItem) {
        super(renderManager, item, renderItem);
    }

    @Override
    public ItemStack getPotion(EntityThrowable entity) {
        return getItemStack(entity);
    }

    public ItemStack getItemStack(EntityThrowable entityItem) {
        return entityItem.getItemStack();
    }

}
