package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.entity.RenderVengeanceSpirit;

/**
 * Config for the {@link EntityNetherfish}.
 * @author rubensworks
 *
 */
public class EntityVengeanceSpiritConfig extends EntityConfig<EntityVengeanceSpirit> {

    @ConfigurableProperty(category = "mob", comment = "The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.")
    public static int spawnLimit = 5;

    @ConfigurableProperty(category = "mob", comment = "The area in which the spawn limit will be checked on each spawn attempt.")
    public static int spawnLimitArea = 5;

    @ConfigurableProperty(category = "mob", comment = "The blacklisted entity spirits, by entity name.")
    public static String[] entityBlacklist = new String[]{
            "evilcraft:vengeance_spirit",
            "evilcraft:controlled_zombie",
            "evilcraft:werewolf",
            "minecraft:ender_dragon",
    };

    @ConfigurableProperty(category = "mob", comment = "Whether vengeance spirits should always be visible in creative mode.")
    public static boolean alwaysVisibleInCreative = false;

    @ConfigurableProperty(category = "mob", comment = "The 1/X chance that an actual spirit will spawn when doing actions like mining with the Vengeance Pickaxe.")
    public static int nonDegradedSpawnChance = 5;

    public EntityVengeanceSpiritConfig() {
        super(
                EvilCraft._instance,
                "vengeance_spirit",
                eConfig -> EntityType.Builder.<EntityVengeanceSpirit>create(EntityVengeanceSpirit::new, EntityClassification.MONSTER)
                        .size(1, 1) // Dummy size, to avoid rare bounding box crashes before inner entity is init.
                        .immuneToFire(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "vengeance_spirit_spawn_egg", Helpers.RGBToInt(64, 16, 93), Helpers.RGBToInt(134, 60, 169))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityVengeanceSpirit> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderVengeanceSpirit(entityRendererManager, this);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        EvilCraft._instance.getImcHandler().registerAction(Reference.IMC_BLACKLIST_VENGEANCESPIRIT, message -> {
            Object value = message.getMessageSupplier().get();
            if (value instanceof String) {
                EntityVengeanceSpirit.addToBlacklistIMC((String) value);
                return true;
            }
            return false;
        });
    }
    
}
