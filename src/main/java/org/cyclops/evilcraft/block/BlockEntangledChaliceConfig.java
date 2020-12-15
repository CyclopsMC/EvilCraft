package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

/**
 * Config for the {@link BlockEntangledChalice}.
 * @author rubensworks
 *
 */
public class BlockEntangledChaliceConfig extends BlockConfig {

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation chaliceModel;
    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation gemsModel;

    @ConfigurableProperty(category = "machine", comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.tab.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    public BlockEntangledChaliceConfig() {
        super(
                EvilCraft._instance,
                "entangled_chalice",
                eConfig -> new BlockEntangledChalice(Block.Properties.create(Material.IRON)
                        .hardnessAndResistance(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemEntangledChalice(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> () -> new RenderItemStackTileEntityEntangledChalice()))
        );
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        chaliceModel = new ResourceLocation(getMod().getModId() + ":block/chalice");
        gemsModel = new ResourceLocation(getMod().getModId() + ":block/gems");
        ModelLoader.addSpecialModel(chaliceModel);
        ModelLoader.addSpecialModel(gemsModel);
    }
}
