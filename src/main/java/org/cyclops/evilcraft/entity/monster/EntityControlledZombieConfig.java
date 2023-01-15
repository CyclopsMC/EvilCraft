package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
                eConfig -> EntityType.Builder.<EntityControlledZombie>of(EntityControlledZombie::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "controlled_zombie_spawn_egg", Helpers.RGBToInt(10, 10, 10), Helpers.RGBToInt(114, 80, 129))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEntityAttributeCreationEvent);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityControlledZombie> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderControlledZombie(this, renderContext);
    }

    public void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .build());
    }
}
