package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDisplayStand;
import org.jetbrains.annotations.Nullable;

/**
 * Model for the display stand.
 * @author rubensworks
 */
public class ModelDisplayStand implements UnbakedModel {

    private final BlockModel blockModel;

    public ModelDisplayStand(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        this.blockModel.resolveDependencies(resolver);
    }

    @Override
    public @Nullable UnbakedGeometry geometry() {
        return new ExtendedUnbakedGeometry() {
            @Override
            public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, ModelDebugName debugName, ContextMap additionalProperties) {
                ModelLoaderDisplayStand.getInstance().getResolvedModels().put(debugName.debugName(), baker.getModel(ResourceLocation.parse(debugName.debugName())));
                ModelLoaderDisplayStand.getInstance().setBaker(baker);
                ModelLoaderDisplayStand.getInstance().setTextureSlots(textureSlots);

                return blockModel.geometry().bake(textureSlots, baker, state, debugName, additionalProperties);
            }
        };
    }

    @Override
    public TextureSlots.Data textureSlots() {
        return this.blockModel.textureSlots();
    }

}
