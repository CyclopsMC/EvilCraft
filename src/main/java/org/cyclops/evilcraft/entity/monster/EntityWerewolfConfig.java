package org.cyclops.evilcraft.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityWerewolf}.
 * @author rubensworks
 *
 */
public class EntityWerewolfConfig extends EntityConfigCommon<ModBaseNeoForge<?>, EntityWerewolf> {

    public EntityWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                eConfig -> EntityType.Builder.<EntityWerewolf>of(EntityWerewolf::new, MobCategory.MONSTER)
                        .sized(0.6F, 2.9F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "werewolf_spawn_egg")
        );
        EvilCraft._instance.getModEventBus().addListener(this::onEntityAttributeCreationEvent);
    }

    @Override
    public EntityClientConfig<ModBaseNeoForge<?>, EntityWerewolf> constructEntityClientConfig() {
        return new EntityWerewolfConfigClient(this);
    }

    public void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D)
                .build());
    }

}
