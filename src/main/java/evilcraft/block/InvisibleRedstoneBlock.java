package evilcraft.block;

import evilcraft.Configs;
import evilcraft.item.RedstoneGrenade;
import evilcraft.item.RedstoneGrenadeConfig;
import evilcraft.tileentity.TileInvisibleRedstoneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;
import java.util.Random;

/**
 * An invisible blockState where players can walk through and disappears after a few ticks.
 * @author immortaleeb
 *
 */
public class InvisibleRedstoneBlock extends ConfigurableBlockContainer {
    private static InvisibleRedstoneBlock _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if (_instance == null)
            _instance = new InvisibleRedstoneBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static InvisibleRedstoneBlock getInstance() {
        return _instance;
    }

    private InvisibleRedstoneBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.air, TileInvisibleRedstoneBlock.class);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
    	if(Configs.isEnabled(RedstoneGrenadeConfig.class)) {
    		return RedstoneGrenade.getInstance();
    	} else {
    		return null;
    	}
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, BlockPos blockPos, IBlockState blockState, EnumFacing side) {
        return 15;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public boolean isReplaceable(World world, BlockPos blockPos) {
        return true;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState blockState) {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        return AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState blockStatedata) {
        return new TileInvisibleRedstoneBlock(world);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        // Do not appear in creative tab
    }
}
