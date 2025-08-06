package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.jetbrains.annotations.Nullable;

/**
 * Model the box of eternal closure.
 * @author rubensworks
 */
public class ModelBoxOfEternalClosure implements UnbakedModel {

    public static ResourceLocation boxModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box");
    public static ResourceLocation boxLidModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid");
    public static ResourceLocation boxLidRotatedModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid_rotated");

    @Override
    public TextureSlots.Data textureSlots() {
        return new TextureSlots.Data.Builder()
                .addTexture("0", new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse("evilcraft:block/box_of_eternal_closure")))
                .build();
    }

    @Override
    public @Nullable UnbakedGeometry geometry() {
        return new UnbakedGeometry() {
            @Override
            public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, ModelDebugName debugName) {
                if (modelState == BlockModelRotation.X0_Y90) {
                    ModelBoxOfEternalClosureBaked.boxModel = baker.getModel(boxModel).bakeTopGeometry(textureSlots, baker, modelState).getAll();
                    ModelBoxOfEternalClosureBaked.boxLidModel = baker.getModel(boxLidModel).bakeTopGeometry(textureSlots, baker, modelState).getAll();
                    ModelBoxOfEternalClosureBaked.boxLidRotatedModel = baker.getModel(boxLidRotatedModel).bakeTopGeometry(textureSlots, baker, modelState).getAll();
                }
                return new QuadCollection.Builder().build();
            }
        };
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.markDependency(boxModel);
        resolver.markDependency(boxLidModel);
        resolver.markDependency(boxLidRotatedModel);
    }
}
