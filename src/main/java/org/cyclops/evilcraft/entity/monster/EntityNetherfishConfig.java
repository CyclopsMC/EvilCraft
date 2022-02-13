package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
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
                eConfig -> EntityType.Builder.<EntityNetherfish>of(EntityNetherfish::new, EntityClassification.MONSTER)
                        .sized(0.4F, 0.3F)
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "netherfish_spawn_egg", Helpers.RGBToInt(73, 27, 20), Helpers.RGBToInt(160, 45, 27))
        );
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityNetherfish> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderNetherfish(entityRendererManager, this);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        // A bit stronger than those normal silverfish...
        GlobalEntityTypeAttributes.put(getInstance(), MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .build());
    }
    
}
