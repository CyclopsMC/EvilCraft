package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;

/**
 * A renderer for a throwable item.
 * @author rubensworks
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderThrowable extends ThrownItemRenderer<EntityThrowable> {

    public RenderThrowable(EntityRendererProvider.Context context) {
        super(context);
    }

}
