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
import org.cyclops.evilcraft.client.render.entity.ModelWerewolf;
import org.cyclops.evilcraft.client.render.entity.RenderWerewolf;

/**
 * @author rubensworks
 */
public class EntityWerewolfConfigClient extends EntityClientConfig<ModBaseNeoForge<?>, EntityWerewolf> {

    private final ModelLayerLocation model;

    public EntityWerewolfConfigClient(EntityConfigCommon<ModBaseNeoForge<?>, EntityWerewolf> entityConfig) {
        super(entityConfig);
        this.model = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "werewolf"), "main");
        entityConfig.getMod().getModEventBus().addListener(this::loadLayerDefinitions);
    }

    public void loadLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(this.model, ModelWerewolf::createBodyLayer);
    }

    @Override
    public EntityRenderer<? super EntityWerewolf, ?> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderWerewolf(renderContext, (EntityWerewolfConfig) getEntityConfig(), new ModelWerewolf(renderContext.bakeLayer(this.model)), 0.5F);
    }
}
