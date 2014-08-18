package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.helpers.MinecraftHelpers;
import evilcraft.fluids.Blood;

/**
 * A block for the {@link Blood} fluid.
 * @author rubensworks
 *
 */
public class FluidBlockBlood extends ConfigurableBlockFluidClassic {
    
    private static final int CHANCE_HARDEN = 10;

    private static FluidBlockBlood _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new FluidBlockBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static FluidBlockBlood getInstance() {
        return _instance;
    }

    private FluidBlockBlood(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Blood.getInstance(), Material.water);
        
        if (MinecraftHelpers.isClientSide())
            this.setParticleColor(1.0F, 0.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(World par1World) {
        return 100;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if(isSourceBlock(world, x, y, z) && !world.isRaining() && random.nextInt(CHANCE_HARDEN) == 0) {
            world.setBlock(x, y, z, HardenedBlood.getInstance());
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        } else {
            super.updateTick(world, x, y, z, random);
        }
    }

}
