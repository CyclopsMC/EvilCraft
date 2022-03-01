package org.cyclops.evilcraft.client.render.blockentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockBloodChest}.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderBlockEntityBloodChest extends RenderBlockEntityChestBase<BlockEntityBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "model/blood_chest");

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(TEXTURE);
        }
    }

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
