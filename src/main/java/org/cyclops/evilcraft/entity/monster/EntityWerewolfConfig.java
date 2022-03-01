package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.entity.ModelWerewolf;
import org.cyclops.evilcraft.client.render.entity.RenderWerewolf;

/**
 * Config for the {@link EntityWerewolf}.
 * @author rubensworks
 *
 */
public class EntityWerewolfConfig extends EntityConfig<EntityWerewolf> {

    @OnlyIn(Dist.CLIENT)
    public static ModelLayerLocation MODEL = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "werewolf"), "main");

    public EntityWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                eConfig -> EntityType.Builder.<EntityWerewolf>of(EntityWerewolf::new, MobCategory.MONSTER)
                        .sized(0.6F, 2.9F),
                getDefaultSpawnEggItemConfigConstructor(EvilCraft._instance, "werewolf_spawn_egg", Helpers.RGBToInt(105, 67, 18), Helpers.RGBToInt(57, 25, 10))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEntityAttributesModification);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ForgeHooksClient.registerLayerDefinition(MODEL, ModelWerewolf::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityWerewolf> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderWerewolf(renderContext, this, new ModelWerewolf(renderContext.bakeLayer(MODEL)), 0.5F);
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

        event.add(getInstance(), Attributes.FOLLOW_RANGE, 35.0D);
        event.add(getInstance(), Attributes.MAX_HEALTH, 40.0D);
        event.add(getInstance(), Attributes.MOVEMENT_SPEED, 0.4D);
        event.add(getInstance(), Attributes.ATTACK_DAMAGE, 7.0D);
    }

}
