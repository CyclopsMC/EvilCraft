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
import org.cyclops.evilcraft.client.render.entity.ModelWerewolf;
import org.cyclops.evilcraft.client.render.entity.RenderWerewolf;

/**
 * Config for the {@link EntityWerewolf}.
 * @author rubensworks
 *
 */
public class EntityWerewolfConfig extends EntityConfig<EntityWerewolf> {

    public EntityWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                eConfig -> EntityType.Builder.<EntityWerewolf>of(EntityWerewolf::new, EntityClassification.MONSTER)
                        .sized(0.6F, 2.9F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "werewolf_spawn_egg", Helpers.RGBToInt(105, 67, 18), Helpers.RGBToInt(57, 25, 10))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityWerewolf> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderWerewolf(entityRendererManager, this, new ModelWerewolf(), 0.5F);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        GlobalEntityTypeAttributes.put(getInstance(), MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .build());
    }
    
}
