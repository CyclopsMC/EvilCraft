package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
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

    @OnlyIn(Dist.CLIENT)
    public static ModelLayerLocation MODEL;

    public EntityPoisonousLibelleConfig() {
        super(
                EvilCraft._instance,
            "poisonous_libelle",
                eConfig -> EntityType.Builder.<EntityPoisonousLibelle>of(EntityPoisonousLibelle::new, MobCategory.MONSTER)
                        .sized(0.5F, 0.45F)
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "poisonous_libelle_spawn_egg", Helpers.RGBToInt(57, 125, 27), Helpers.RGBToInt(196, 213, 57))
        );
        EvilCraft._instance.getModEventBus().addListener(this::onEntityAttributeCreationEvent);
        EvilCraft._instance.getModEventBus().addListener(this::registerSpawnPlacements);
        if (MinecraftHelpers.isClientSide()) {
            ModelLoader.registerModel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ClientHooks.registerLayerDefinition(MODEL, ModelPoisonousLibelle::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityPoisonousLibelle> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderPoisonousLibelle(renderContext, this, new ModelPoisonousLibelle(renderContext.bakeLayer(MODEL)), 0.5F);
    }

    public void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.625D)
                .build());
    }

    public void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(getInstance(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityPoisonousLibelle::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public static class ModelLoader {
        public static Object registerModel() {
            MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "poisonous_libelle"), "main");
            return null;
        }
    }

}
