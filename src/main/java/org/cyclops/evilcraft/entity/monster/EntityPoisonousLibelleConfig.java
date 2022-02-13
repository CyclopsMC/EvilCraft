package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.ModelPoisonousLibelle;
import org.cyclops.evilcraft.client.render.entity.RenderPoisonousLibelle;

/**
 * Config for the {@link EntityPoisonousLibelle}.
 * @author rubensworks
 *
 */
public class EntityPoisonousLibelleConfig extends EntityConfig<EntityPoisonousLibelle> {

    @ConfigurableProperty(category = "mob", comment = "Should the Poisonous Libelle do damage, next to poisoning?", isCommandable = true)
    public static boolean hasAttackDamage = false;

    @ConfigurableProperty(category = "mob", comment = "The minimum Y-level this mob can spawn at.", isCommandable = true)
    public static int minY = 55;

    @ConfigurableProperty(category = "mob", comment = "1/X chance on getting poisoned when hit.", isCommandable = true)
    public static int poisonChance = 20;

    public EntityPoisonousLibelleConfig() {
        super(
                EvilCraft._instance,
            "poisonous_libelle",
                eConfig -> EntityType.Builder.<EntityPoisonousLibelle>of(EntityPoisonousLibelle::new, EntityClassification.MONSTER)
                        .sized(0.5F, 0.45F)
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "poisonous_libelle_spawn_egg", Helpers.RGBToInt(57, 125, 27), Helpers.RGBToInt(196, 213, 57))
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getName().equals(Biomes.RIVER.location())) {
            event.getSpawns().getSpawner(getInstance().getCategory())
                    .add(new MobSpawnInfo.Spawners(getInstance(), 1, 1, 2));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityPoisonousLibelle> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderPoisonousLibelle(entityRendererManager, this, new ModelPoisonousLibelle(), 0.5F);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        GlobalEntityTypeAttributes.put(getInstance(), MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.625D)
                .build());
    }
    
}
