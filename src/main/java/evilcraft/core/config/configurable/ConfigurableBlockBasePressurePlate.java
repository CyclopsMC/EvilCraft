package evilcraft.core.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * {@link BlockBasePressurePlate} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockBasePressurePlate extends BlockBasePressurePlate implements IConfigurable {
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockBasePressurePlate(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        setHardness(2F);
        setStepSound(Block.soundTypeStone);
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState blockState, Block block) {
        if(!canPlaceBlockAt(world, blockPos)) {
        	this.dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), 0);
            world.setBlockToAir(blockPos);
        }
    }
    
    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

}
