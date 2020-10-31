package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;

/**
 * A renderer for a throwable item.
 * @author rubensworks
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderThrowable extends SpriteRenderer<EntityThrowable> {

    public RenderThrowable(EntityRendererManager renderManager, ItemRenderer renderItem) {
        super(renderManager, renderItem);
    }

}
