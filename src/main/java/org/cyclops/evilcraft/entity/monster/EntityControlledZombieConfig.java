package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
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
        // Copied from Monster.createMonsterAttributes()
        AttributeSupplier attributeSupplier = AttributeSupplier.builder()
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get())
                .add(net.minecraftforge.common.ForgeMod.NAMETAG_DISTANCE.get())
                .add(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get())

                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D).build();
        event.put(getInstance(), attributeSupplier);
    }
}
