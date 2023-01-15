package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.PrimedTnt;
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
                eConfig -> EntityType.Builder.<EntityLightningBombPrimed>of(EntityLightningBombPrimed::new, MobCategory.MISC)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<PrimedTnt> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderBombPrimed(renderContext, RegistryEntries.BLOCK_LIGHTNING_BOMB_PRIMED);
    }

}
