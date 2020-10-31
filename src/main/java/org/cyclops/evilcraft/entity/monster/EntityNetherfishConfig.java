package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderNetherfish;

/**
 * Config for the {@link EntityNetherfish}.
 * @author rubensworks
 *
 */
public class EntityNetherfishConfig extends EntityConfig<EntityNetherfish> {

    public EntityNetherfishConfig() {
        super(
                EvilCraft._instance,
                "netherfish",
                eConfig -> EntityType.Builder.<EntityNetherfish>create(EntityNetherfish::new, EntityClassification.MONSTER)
                        .size(0.4F, 0.3F)
                        .immuneToFire(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "netherfish_spawn_egg", Helpers.RGBToInt(73, 27, 20), Helpers.RGBToInt(160, 45, 27))
        );
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityNetherfish> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderNetherfish(entityRendererManager, this);
    }
    
}
