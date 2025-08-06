package org.cyclops.evilcraft.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityControlledZombie}.
 * @author rubensworks
 *
 */
public class EntityControlledZombieConfig extends EntityConfigCommon<IModBase, EntityControlledZombie> {

    public EntityControlledZombieConfig() {
        super(
                EvilCraft._instance,
            "controlled_zombie",
                eConfig -> EntityType.Builder.<EntityControlledZombie>of(EntityControlledZombie::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.8F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "controlled_zombie_spawn_egg")
        );
        EvilCraft._instance.getModEventBus().addListener(this::onEntityAttributeCreationEvent);
    }

    @Override
    public EntityClientConfig<IModBase, EntityControlledZombie> constructEntityClientConfig() {
        return new EntityControlledZombieConfigClient(this);
    }

    public void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .build());
    }
}
