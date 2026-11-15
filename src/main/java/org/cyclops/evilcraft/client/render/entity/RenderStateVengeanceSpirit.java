package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

/**
 * Extends {@link LivingEntityRenderState}, as other mods may pass it to models of living entities.
 * @author rubensworks
 */
public class RenderStateVengeanceSpirit extends LivingEntityRenderState {
    public EntityVengeanceSpirit spirit;
}
