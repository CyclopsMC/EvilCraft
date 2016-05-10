package org.cyclops.evilcraft.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

/**
 * Config for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class DarkTankConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static DarkTankConfig _instance;
    
    /**
	 * The maximum tank size possible by combining tanks.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The maximum tank size possible by combining tanks. (Make sure that you do not cross the max int size.)")
	public static int maxTankSize = 65536000;
	/**
	 * The maximum tank size visible in the creative tabs.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The maximum tank size visible in the creative tabs. (Make sure that you do not cross the max int size.)")
	public static int maxTankCreativeSize = 4096000;
    /**
     * If creative versions for all fluids should be added to the creative tab.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If creative versions for all fluids should be added to the creative tab.")
    public static boolean creativeTabFluids = true;
    /**
     * If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.tab.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    /**
     * Make a new instance.
     */
    public DarkTankConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkTank",
            null,
            DarkTank.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockFluidContainer.class;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        if(!DarkTankConfig.staticBlockRendering) {
            getMod().getProxy().registerRenderer(TileDarkTank.class, new RenderTileEntityDarkTank());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onInit(Step step) {
        super.onInit(step);
        if(step == Step.INIT) {
            // Handle additional type of dark tank item rendering
            for (int meta = 0; meta < 2; meta++) {
                Item item = Item.getItemFromBlock(getBlockInstance());
                String modId = getMod().getModId();
                String itemName = getModelName(new ItemStack(item, 1, meta));
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(modId + ":" + itemName, "inventory");
                ModelBakery.registerItemVariants(item, modelResourceLocation);
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                        item, meta, modelResourceLocation);
            }
        }
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 0) {
            return super.getModelName(itemStack) + "_off";
        } else {
            return super.getModelName(itemStack) + "_on";
        }
    }

}
