package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBloodChest;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * Renderer for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderTileEntityBloodChest extends RenderTileEntityChestBase<TileBloodChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "model/blood_chest");

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(Atlases.CHEST_SHEET)) {
            event.addSprite(TEXTURE);
        }
    }

    public RenderTileEntityBloodChest(TileEntityRendererDispatcher p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected RenderMaterial getMaterial(TileBloodChest tile) {
        return new RenderMaterial(Atlases.CHEST_SHEET, TEXTURE);
    }

    @Override
    protected Direction getDirection(TileBloodChest tile) {
        return tile.getRotation();
    }

}
