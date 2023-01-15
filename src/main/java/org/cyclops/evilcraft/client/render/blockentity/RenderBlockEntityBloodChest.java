package org.cyclops.evilcraft.client.render.blockentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockBloodChest}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityBloodChest extends RenderBlockEntityChestBase<BlockEntityBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "model/blood_chest");

    public RenderBlockEntityBloodChest(BlockEntityRendererProvider.Context p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected Material getMaterial(BlockEntityBloodChest tile) {
        return new Material(Sheets.CHEST_SHEET, TEXTURE);
    }

    @Override
    protected Direction getDirection(BlockEntityBloodChest tile) {
        return tile.getRotation();
    }

}
