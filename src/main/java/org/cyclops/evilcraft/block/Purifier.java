package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.item.BucketBloodConfig;
import org.cyclops.evilcraft.tileentity.TilePurifier;

import java.util.List;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class Purifier extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyInteger FILL = PropertyInteger.create("fill", 0, 3);

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
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float motionX, float motionY, float motionZ) {
        if(world.isRemote) {
            return true;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TilePurifier tile = (TilePurifier) world.getTileEntity(blockPos);
            if(tile != null) {
                if (itemStack == null && tile.getPurifyItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(null);
                } else if (itemStack == null && tile.getAdditionalItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getAdditionalItem());
                    tile.setAdditionalItem(null);
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
                }  else if(itemStack != null && tile.getActions().isItemValidForAdditionalSlot(itemStack) && tile.getAdditionalItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setAdditionalItem(copy);
                    itemStack.stackSize--;
                    return true;
                } else if(itemStack != null && tile.getActions().isItemValidForMainSlot(itemStack) && tile.getPurifyItem() == null) {
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

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return super.collisionRayTrace(blockState, worldIn, pos, start, end);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos blockPos, AxisAlignedBB area, List<AxisAlignedBB> collisionBoxes, Entity entity) {
        float f = 0.125F;
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getValue(FILL);
    }

}
