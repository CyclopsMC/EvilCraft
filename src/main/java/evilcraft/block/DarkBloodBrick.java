package evilcraft.block;

import evilcraft.core.algorithm.Wrapper;
import evilcraft.core.block.CubeDetector;
import evilcraft.core.block.CubeDetector.IDetectionListener;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Wall brick for the Spirit Furnace.
 * @author rubensworks
 *
 */
public class DarkBloodBrick extends ConfigurableBlock implements IDetectionListener {

    @BlockProperty
    public static final PropertyBool ACTIVE = PropertyBool.create("enabled");

    private static DarkBloodBrick _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkBloodBrick getInstance() {
        return _instance;
    }

    public DarkBloodBrick(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }
    
    @Override
    public boolean canCreatureSpawn(IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
    	return false;
    }
    
    private void triggerDetector(World world, BlockPos blockPos, boolean valid) {
    	TileSpiritFurnace.detector.detect(world, blockPos, valid, true);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
    	triggerDetector(world, blockPos, true);
        //world.func_147453_f(blockPos, this);
    }

    // TODO
    /*@Override
    public void onBlockPreDestroy(World world, BlockPos blockPos, IBlockState blockState) {super.onBlockDestroyedByPlayer();
    	if((Boolean)blockState.getValue(ACTIVE)) triggerDetector(world, blockPos, false);
    	super.onBlockPreDestroy(world, blockPos, blockState);
    }*/
    
    @Override
	public void onDetect(World world, BlockPos location, Vec3i size, boolean valid) {
		Block block = world.getBlockState(location).getBlock();
		if(block == this) {
			TileSpiritFurnace.detectStructure(world, location, size, valid);
		}
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side,
                                    float posX, float posY, float posZ) {
        if((Boolean) blockState.getValue(ACTIVE)) {
            final Wrapper<BlockPos> tileLocationWrapper = new Wrapper<BlockPos>();
            TileSpiritFurnace.detector.detect(world, blockPos, true, new CubeDetector.IValidationAction() {

                @Override
                public void onValidate(BlockPos location, Block block) {
                    if(block == SpiritFurnace.getInstance()) {
                        tileLocationWrapper.set(location);
                    }
                }

            }, false);
            BlockPos tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                world.getBlockState(tileLocation).getBlock().
                        onBlockActivated(world, tileLocation, world.getBlockState(tileLocation),
                                player, side, posX, posY, posZ);
                return true;
            }
        }
        return super.onBlockActivated(world, blockPos, blockState, player, side, posX, posY, posZ);
    }

}
