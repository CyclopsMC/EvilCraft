package org.cyclops.evilcraft.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.model.SingleModelLoader;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * Config for the {@link EntangledChalice}.
 * @author rubensworks
 *
 */
public class EntangledChaliceConfig extends BlockContainerConfig {

    @SideOnly(Side.CLIENT)
    public static ResourceLocation chaliceModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation gemsModel;
    /**
     * If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.tab.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    /**
     * The unique instance.
     */
    public static EntangledChaliceConfig _instance;

    /**
     * Make a new instance.
     */
    public EntangledChaliceConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entangledChalice",
            null,
            EntangledChalice.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return EntangledChaliceItem.class;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        if(!EntangledChaliceConfig.staticBlockRendering) {
            EvilCraft._instance.getProxy().registerRenderer(TileEntangledChalice.class,
                    new RenderTileEntityEntangledChalice());
        }

        chaliceModel = new ResourceLocation(getMod().getModId() + ":block/chalice");
        gemsModel = new ResourceLocation(getMod().getModId() + ":block/gems");

        ModelEntangledChalice modelEntangledChalice = new ModelEntangledChalice();
        ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                Reference.MOD_ID, "models/item/entangledChalice", modelEntangledChalice));
        ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                Reference.MOD_ID, "models/block/entangledChalice", modelEntangledChalice));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onInit(Step step) {
        super.onInit(step);
        if (step == Step.INIT) {
            // Make sure that the chalice's model is available when activated.
            Item item = Item.getItemFromBlock(getBlockInstance());
            ItemStack itemStack = new ItemStack(item, 1, 1);
            String itemName = getModelName(itemStack);
            ModelResourceLocation model = new ModelResourceLocation(getMod().getModId() + ":" + itemName, "inventory");
            ModelBakery.registerItemVariants(item, model);
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, itemStack.getMetadata(), model);
        }
    }
}
