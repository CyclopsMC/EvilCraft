package evilcraft.block;

import evilcraft.item.BucketBloodConfig;
import evilcraft.tileentity.TilePurifier;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class Purifier extends ConfigurableBlockContainer {

    public static final PropertyInteger FILL = PropertyInteger.create("fill", 0, 2);

    private static Purifier _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Purifier getInstance() {
        return _instance;
    }

    public Purifier(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TilePurifier.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FILL, 0));
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float motionX, float motionY, float motionZ) {
        if(world.isRemote) {
            return true;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TilePurifier tile = (TilePurifier) world.getTileEntity(blockPos);
            if(tile != null) {
                if (itemStack == null && tile.getPurifyItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(null);
                } else if (itemStack == null && tile.getBookItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getBookItem());
                    tile.setBookItem(null);
                } else if(itemStack != null && itemStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if(fluidStack != null && tile.getTank().canTankAccept(fluidStack.getFluid())
                    		&& tile.getTank().canCompletelyFill(fluidStack)
                    		&& itemStack.getItem() != Items.bucket) {
                    	tile.getTank().fill(fluidStack, true);
                    	tile.sendUpdate();
                    	if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }
                        return true;
                    } else if(itemStack.getItem() == Items.bucket) {
                        int buckets = tile.getBucketsFloored();
                        if (buckets > 0) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(BucketBloodConfig._instance.getItemInstance()));
                            }
                            tile.setBuckets(buckets - 1, tile.getBucketsRest());
                        }
                        return true;
                    }
                }  else if(itemStack != null && itemStack.getItem() == TilePurifier.ALLOWED_BOOK && tile.getBookItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setBookItem(copy);
                    itemStack.stackSize--;
                    return true;
                } else if(itemStack != null && tile.getPurifyItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setPurifyItem(copy);
                    player.inventory.getCurrentItem().stackSize--;
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState blockState, AxisAlignedBB area, List collisionBoxes, Entity entity) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, collisionBoxes, entity);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, collisionBoxes, entity);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, collisionBoxes, entity);
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        // TODO
        //return RenderPurifier.ID;
        return -1;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return (Integer) world.getBlockState(blockPos).getValue(FILL);
    }

}
