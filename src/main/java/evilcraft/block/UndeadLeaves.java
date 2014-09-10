package evilcraft.block;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.block.component.EntityDropParticleFXBlockComponent;
import evilcraft.core.block.component.IEntityDropParticleFXBlock;
import evilcraft.core.config.configurable.ConfigurableBlockLeaves;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;

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
    public Item getItemDropped(int meta, Random random, int zero) {
        return Item.getItemFromBlock(Blocks.deadbush);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        super.randomDisplayTick(world, x, y, z, random);
        entityDropParticleFXBlockComponent.randomDisplayTick(world, x, y, z, random);
    }

	@Override
	public String[] func_150125_e() {
		return null;
	}

}
