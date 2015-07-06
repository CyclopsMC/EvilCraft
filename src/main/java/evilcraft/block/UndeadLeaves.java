package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.component.EntityDropParticleFXBlockComponent;
import org.cyclops.cyclopscore.block.component.IEntityDropParticleFXBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;
import java.util.Random;

/**
 * Leaves for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadLeaves extends ConfigurableBlockLeaves implements IEntityDropParticleFXBlock {
    
    private static UndeadLeaves _instance = null;
    
    private EntityDropParticleFXBlockComponent entityDropParticleFXBlockComponent;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new UndeadLeaves(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadLeaves getInstance() {
        return _instance;
    }

    private UndeadLeaves(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig);
        
        setHardness(0.2F);
        setLightOpacity(1);
        setStepSound(soundTypeGrass);
        
        if (MinecraftHelpers.isClientSide()) {
            entityDropParticleFXBlockComponent = new EntityDropParticleFXBlockComponent(1.0F, 0.0F, 0.0F);
            entityDropParticleFXBlockComponent.setOffset(0);
            entityDropParticleFXBlockComponent.setChance(50);
        }
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(Blocks.deadbush);
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return UndeadSapling.TYPE;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand) {
        entityDropParticleFXBlockComponent.randomDisplayTick(world, blockPos, blockState, rand);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return null;
    }
}
