package evilcraft.block;
import net.minecraft.block.material.Material;
import evilcraft.client.gui.container.GuiSpiritReanimator;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.inventory.container.ContainerSpiritReanimator;
import evilcraft.tileentity.TileSpiritReanimator;

/**
 * A machine that can reanimate spirits inside BOEC's.
 * @author rubensworks
 *
 */
public class SpiritReanimator extends ConfigurableBlockContainerGuiTankInfo {
    
    private static SpiritReanimator _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SpiritReanimator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritReanimator getInstance() {
        return _instance;
    }

    private SpiritReanimator(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSpiritReanimator.class);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiSpiritReanimator.class);
        setContainer(ContainerSpiritReanimator.class);
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritReanimator.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileSpiritReanimator.LIQUID_PER_SLOT;
    }

}
