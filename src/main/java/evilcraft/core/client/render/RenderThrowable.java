package evilcraft.core.client.render;

import evilcraft.core.entity.item.EntityThrowable;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A renderer for a throwable item.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderThrowable extends RenderSnowball {

    public RenderThrowable(RenderManager renderManager, Item item, RenderItem renderItem) {
        super(renderManager, item, renderItem);
    }

    @Override
    public ItemStack func_177082_d(Entity entity) {
        return getItemStack((EntityThrowable) entity);
    }

    public ItemStack getItemStack(EntityThrowable entityItem) {
        return entityItem.getItemStack();
    }

}
