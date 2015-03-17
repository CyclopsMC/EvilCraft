package evilcraft.core.item;

import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * An extended {@link ItemBlockExtended} that will add the NBT data that is stored inside
 * the item to the placed {@link TileEntity} for the blockState.
 * Subinstances of {@link ConfigurableBlockContainer} will perform the inverse operation, being
 * that broken blocks will save the NBT data inside the dropped {@link ItemBlock}.
 * @author rubensworks
 *
 */
public class ItemBlockNBT extends ItemBlockExtended {
    
    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockNBT(Block block) {
        super(block);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState blockState) {
        if (super.placeBlockAt(stack, player, world, blockPos, side, hitX, hitY, hitZ, blockState)) {
            TileEntity tile = world.getTileEntity(blockPos);

            if (tile != null && stack.getTagCompound() != null) {
                tile.readFromNBT(stack.getTagCompound());
                readAdditionalInfo(tile, stack);
            }

            return true;
        }

        return false;
    }
    
    /**
     * Read additional info about the item into the tile.
     * @param tile The tile that is being created.
     * @param itemStack The item that is placed.
     */
    protected void readAdditionalInfo(TileEntity tile, ItemStack itemStack) {
    	
    }

}
