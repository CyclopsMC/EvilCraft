package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.fluid.Blood;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

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
        if(random.nextInt(CHANCE_HARDEN) == 0 &&
                isSourceBlock(world, x, y, z) && !world.isRaining() && !isWaterInArea(world, x, y, z)) {
            world.setBlock(x, y, z, HardenedBlood.getInstance());
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        } else {
            super.updateTick(world, x, y, z, random);
        }
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

    protected boolean isWaterInArea(World world, int x, int y, int z) {
        return WorldHelpers.foldArea(world, 4, x, y, z, new WorldHelpers.WorldFoldingFunction<Boolean, Boolean>() {

            @Nullable
            @Override
            public Boolean apply(@Nullable Boolean input, World world, int x, int y, int z) {
                return input || world.getBlock(x, y, z) == Blocks.water;
            }

        }, false);
    }

}
