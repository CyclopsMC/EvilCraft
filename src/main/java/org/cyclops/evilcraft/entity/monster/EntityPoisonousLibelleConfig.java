package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
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
    public static ModelLayerLocation MODEL = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "poisonous_libelle"), "main");

    public EntityPoisonousLibelleConfig() {
        super(
                EvilCraft._instance,
            "poisonous_libelle",
                eConfig -> EntityType.Builder.<EntityPoisonousLibelle>of(EntityPoisonousLibelle::new, MobCategory.MONSTER)
                        .sized(0.5F, 0.45F)
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "poisonous_libelle_spawn_egg", Helpers.RGBToInt(57, 125, 27), Helpers.RGBToInt(196, 213, 57))
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEntityAttributesModification);
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getName().equals(Biomes.RIVER.location())) {
            event.getSpawns().getSpawner(getInstance().getCategory())
                    .add(new MobSpawnSettings.SpawnerData(getInstance(), 1, 1, 2));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ForgeHooksClient.registerLayerDefinition(MODEL, ModelPoisonousLibelle::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityPoisonousLibelle> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderPoisonousLibelle(renderContext, this, new ModelPoisonousLibelle(renderContext.bakeLayer(MODEL)), 0.5F);
    }

    public void onEntityAttributesModification(EntityAttributeModificationEvent event) {
        // Copied from Monster.createMonsterAttributes()
        event.add(getInstance(), Attributes.ATTACK_DAMAGE);
        event.add(getInstance(), Attributes.FOLLOW_RANGE, 16.0D);
        event.add(getInstance(), Attributes.ATTACK_KNOCKBACK);
        event.add(getInstance(), Attributes.MAX_HEALTH);
        event.add(getInstance(), Attributes.KNOCKBACK_RESISTANCE);
        event.add(getInstance(), Attributes.MOVEMENT_SPEED);
        event.add(getInstance(), Attributes.ARMOR);
        event.add(getInstance(), Attributes.ARMOR_TOUGHNESS);
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.SWIM_SPEED.get());
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.NAMETAG_DISTANCE.get());
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

        event.add(getInstance(), Attributes.MAX_HEALTH, 1.0D);
        event.add(getInstance(), Attributes.MOVEMENT_SPEED, 0.625D);
    }

}
