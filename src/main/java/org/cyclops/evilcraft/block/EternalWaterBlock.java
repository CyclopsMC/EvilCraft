package org.cyclops.evilcraft.block;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.tileentity.TileEternalWaterBlock;

/**
 * Block for {@link EternalWaterBlockConfig}.
 * @author rubensworks
 */
public class EternalWaterBlock extends ConfigurableBlockContainer {

    private static EternalWaterBlock _instance = null;

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty(ignore = true)
    public static final IProperty[] _COMPAT = {BlockLiquid.LEVEL};

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EternalWaterBlock getInstance() {
        return _instance;
    }

    /**
     * Make a new blockState instance.
     *
     * @param eConfig    Config for this blockState.
     */
    public EternalWaterBlock(ExtendedConfig eConfig) {
        super(eConfig, Material.water, TileEternalWaterBlock.class);
    }

    @Override
    public boolean saveNBTToDroppedItem() {
        return false;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos blockPos) {
        return 0.5F;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side,
                                    float xp, float yp, float zp) {
        ItemStack itemStack = player.inventory.getCurrentItem();
        if(itemStack != null) {
            if(itemStack.getItem() instanceof IFluidContainerItem) {
                IFluidContainerItem containerItem = ((IFluidContainerItem) itemStack.getItem());
                containerItem.fill(itemStack, TileEternalWaterBlock.WATER, true);
            }
        }
        return true;
    }

}
