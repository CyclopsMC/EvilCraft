package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.tileentity.TileDarkTank;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

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
        getMod().getProxy().registerRenderer(TileDarkTank.class, new RenderTileEntityDarkTank());
    }

}
