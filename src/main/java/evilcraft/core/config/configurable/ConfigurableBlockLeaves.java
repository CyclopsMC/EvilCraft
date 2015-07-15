package evilcraft.core.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;
import java.util.Random;

/**
 * Block that extends from BlockLeaves that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockLeaves extends BlockLeaves implements IConfigurable {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockState createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty(ignore = true)
    public static final IProperty[] _COMPAT = {DECAYABLE, CHECK_DECAY};

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockLeaves(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    @Override
    public abstract Item getItemDropped(IBlockState blockState, Random random, int zero);

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState blockState, float chance, int fortune) {
        if(!world.isRemote) {
            List<ItemStack> items = getDrops(world, blockPos, blockState, fortune);

            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.spawnAsEntity(world, blockPos, item);
                }
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        return !(isTransparent) || super.shouldSideBeRendered(world, blockPos, side);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
    
    @Override
    public boolean isLeaves (IBlockAccess world, BlockPos blockPos) {
        return true;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
}
