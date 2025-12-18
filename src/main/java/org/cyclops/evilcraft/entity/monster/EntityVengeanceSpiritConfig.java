package org.cyclops.evilcraft.entity.monster;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

import java.util.List;

/**
 * Config for the {@link EntityNetherfish}.
 * @author rubensworks
 *
 */
public class EntityVengeanceSpiritConfig extends EntityConfigCommon<IModBase, EntityVengeanceSpirit> {

    @ConfigurablePropertyCommon(category = "mob", comment = "If vengeance spirits should also spawn when entities are killed by non-player damage sources.", isCommandable = true)
    public static boolean spawnOnNonPlayerKills = false;

    @ConfigurablePropertyCommon(category = "mob", comment = "The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.")
    public static int spawnLimit = 5;

    @ConfigurablePropertyCommon(category = "mob", comment = "The area in which the spawn limit will be checked on each spawn attempt.")
    public static int spawnLimitArea = 32;

    @ConfigurablePropertyCommon(category = "mob", comment = "The blacklisted entity spirits, by entity name. Regular expressions are allowed.")
    public static List<String> entityBlacklist = Lists.newArrayList(
            "evilcraft:vengeance_spirit",
            "evilcraft:controlled_zombie",
            "evilcraft:werewolf",
            "minecraft:ender_dragon",
            "farmingforblockheads:merchant" // It causes rendering issues: https://github.com/CyclopsMC/EvilCraft/issues/1004
    );

    @ConfigurablePropertyCommon(category = "mob", comment = "Whether vengeance spirits should always be visible in creative mode.")
    public static boolean alwaysVisibleInCreative = false;

    @ConfigurablePropertyCommon(category = "mob", comment = "The 1/X chance that an actual spirit will spawn when doing actions like mining with the Vengeance Pickaxe.")
    public static int nonDegradedSpawnChance = 5;

    public EntityVengeanceSpiritConfig() {
        super(
                EvilCraft._instance,
                "vengeance_spirit",
                eConfig -> EntityType.Builder.<EntityVengeanceSpirit>of(EntityVengeanceSpirit::new, MobCategory.MONSTER)
                        .sized(1, 1) // Dummy size, to avoid rare bounding box crashes before inner entity is init.
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "vengeance_spirit_spawn_egg")
        );
        EvilCraft._instance.getModEventBus().addListener(this::onEntityAttributeCreationEvent);
    }

    @Override
    public EntityClientConfig<IModBase, EntityVengeanceSpirit> constructEntityClientConfig() {
        return new EntityVengeanceSpiritConfigClient(this);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getImcHandler().registerAction(Reference.IMC_BLACKLIST_VENGEANCESPIRIT, message -> {
            Object value = message.messageSupplier().get();
            if (value instanceof String) {
                EntityVengeanceSpirit.addToBlacklistIMC((String) value);
                return true;
            }
            return false;
        });
        NeoForge.EVENT_BUS.register(EntityVengeanceSpirit.class);
    }

    public void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3125D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.STEP_HEIGHT, 5.0D)
                .build());
    }
}
