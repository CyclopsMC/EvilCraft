package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.entity.ModelPoisonousLibelle;
import org.cyclops.evilcraft.client.render.entity.RenderPoisonousLibelle;

/**
 * @author rubensworks
 */
public class EntityPoisonousLibelleConfigClient extends EntityClientConfig<ModBaseNeoForge<?>, EntityPoisonousLibelle> {

    private final ModelLayerLocation model;

    public EntityPoisonousLibelleConfigClient(EntityConfigCommon<ModBaseNeoForge<?>, EntityPoisonousLibelle> entityConfig) {
        super(entityConfig);
        this.model = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "poisonous_libelle"), "main");
        entityConfig.getMod().getModEventBus().addListener(this::loadLayerDefinitions);
    }

    public void loadLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(this.model, ModelPoisonousLibelle::createBodyLayer);
    }

    @Override
    public EntityRenderer<? super EntityPoisonousLibelle, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderPoisonousLibelle(renderContext, (EntityPoisonousLibelleConfig) getEntityConfig(), new ModelPoisonousLibelle(renderContext.bakeLayer(this.model)), 0.5F);
    }
}
