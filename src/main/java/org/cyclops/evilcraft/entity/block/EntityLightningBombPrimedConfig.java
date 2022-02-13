package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.block.RenderBombPrimed;

/**
 * Config for {@link EntityLightningBombPrimed}.
 * @author rubensworks
 *
 */
public class EntityLightningBombPrimedConfig extends EntityConfig<EntityLightningBombPrimed> {

    @ConfigurableProperty(category = "entity", comment = "The amount of ticks (on average), this bomb should tick before explosion.")
    public static int fuse = 100;

    public EntityLightningBombPrimedConfig() {
        super(
                EvilCraft._instance,
                "lightning_bomb_primed",
                eConfig -> EntityType.Builder.<EntityLightningBombPrimed>of(EntityLightningBombPrimed::new, EntityClassification.MISC)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<TNTEntity> getRender(EntityRendererManager renderManager, ItemRenderer renderItem) {
        return new RenderBombPrimed(renderManager, RegistryEntries.BLOCK_LIGHTNING_BOMB_PRIMED);
    }
    
}
