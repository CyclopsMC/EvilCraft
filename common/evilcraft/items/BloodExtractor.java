package evilcraft.items;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.fluids.Blood;
import evilcraft.render.particle.EntityBloodSplashFX;

/**
 * Can extract blood from attacking mobs and {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class BloodExtractor extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodExtractor _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodExtractor(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodExtractor getInstance() {
        return _instance;
    }

    private BloodExtractor(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, BloodExtractorConfig.containerSize, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int blockID = world.getBlockId(x, y, z);
        if(blockID == BloodStainedBlockConfig._instance.ID) {
            Random random = world.rand;
            
            // Fill the extractor a bit
            int toFill = BloodExtractorConfig.minMB + random.nextInt(BloodExtractorConfig.maxMB - BloodExtractorConfig.minMB);
            ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
            int filled = container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
            
            // Transform bloody dirt into regular dirt if we used some of the blood
            if(filled > 0) {
                int metaData = world.getBlockMetadata(x, y, z);
                world.setBlock(x, y, z, BloodStainedBlock.getInstance().getBlockFromMetadata(metaData).blockID);
                
                if (world.isRemote) {
                    // Init particles
                    EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 5, 1 + random.nextInt(2));
                }
            }
        }
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

}
