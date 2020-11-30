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
import org.cyclops.evilcraft.client.render.entity.RenderControlledZombie;

/**
 * Config for the {@link EntityControlledZombie}.
 * @author rubensworks
 *
 */
public class EntityControlledZombieConfig extends EntityConfig<EntityControlledZombie> {

    public EntityControlledZombieConfig() {
        super(
                EvilCraft._instance,
            "controlled_zombie",
                eConfig -> EntityType.Builder.<EntityControlledZombie>create(EntityControlledZombie::new, EntityClassification.MONSTER)
                        .size(0.6F, 1.8F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "controlled_zombie_spawn_egg", Helpers.RGBToInt(10, 10, 10), Helpers.RGBToInt(114, 80, 129))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityControlledZombie> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderControlledZombie(this, entityRendererManager);
    }
}
