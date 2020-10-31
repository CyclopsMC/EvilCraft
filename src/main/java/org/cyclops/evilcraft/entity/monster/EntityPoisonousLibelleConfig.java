package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderPoisonousLibelle;
import org.cyclops.evilcraft.client.render.entity.ModelPoisonousLibelle;

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
                eConfig -> EntityType.Builder.<EntityPoisonousLibelle>create(EntityPoisonousLibelle::new, EntityClassification.MONSTER)
                        .size(0.5F, 0.45F)
                        .immuneToFire(),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "poisonous_libelle_spawn_egg", Helpers.RGBToInt(57, 125, 27), Helpers.RGBToInt(196, 213, 57))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityPoisonousLibelle> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderPoisonousLibelle(entityRendererManager, this, new ModelPoisonousLibelle(), 0.5F);
    }
    
    @Override
    public void onRegistered() {
        super.onRegistered();
        Biomes.RIVER.getSpawns(getInstance().getClassification()).add(new Biome.SpawnListEntry(getInstance(), 1, 1, 2));
    }
    
}
