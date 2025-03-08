package org.cyclops.evilcraft.core.client.model;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

/**
 * Model for a variant of a broom item.
 * @author rubensworks
 */
public class BroomModel implements UnbakedModel {

    @Override
    public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
        BroomModelBaked bakedModel = new BroomModelBaked();

        // Add aspects to baked model.
        for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
            try {
                BakedModel bakedAspectModel = baker.bake(BroomParts.REGISTRY.getPartModel(part), modelState);
                BroomModelBaked.addBroomModel(part, bakedAspectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bakedModel;
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation partModel : BroomParts.REGISTRY.getPartModels()) {
            resolver.resolve(partModel);
        }
    }
}
