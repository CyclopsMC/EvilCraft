package evilcraft.core.config.configurable;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.core.IInformationProvider;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.item.DamageIndicatedItemComponent;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.item.BucketBlood;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockContainerGuiTankInfo extends ConfigurableBlockContainerGui implements IInformationProvider {

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The class of the tile entity this block holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGuiTankInfo(ExtendedConfig eConfig,
            Material material, Class<? extends TankInventoryTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
    }
    
    /**
     * Get the NBT name for the inner tank.
     * @return The NBT key for the tank.
     */
    public abstract String getTankNBTName();
    /**
     * Get the maximal tank capacity.
     * @return The maximal tank capacity in mB.
     */
    public abstract int getTankCapacity();
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float motionX, float motionY, float motionZ) {
        if(world.isRemote) {
            return super.onBlockActivated(world, x, y, z, player, side, motionX, motionY, motionZ);
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TankInventoryTileEntity tile = (TankInventoryTileEntity) world.getTileEntity(x, y, z);
            if(tile != null) {
                if(itemStack != null && itemStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if(fluidStack != null &&
                            (tile.getTank().canTankAccept(fluidStack.getFluid())
                            || tile.getTank().getFluidType() == null) && tile.getTank().canCompletelyFill(fluidStack)) {
                        tile.getTank().fill(fluidStack, true);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }
                        return true;
                    } else if(itemStack.getItem() == Items.bucket && tile.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
                        tile.getTank().drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(BucketBlood.getInstance()));
                        }
                        return true;
                    }
                }
            }
        }
        return super.onBlockActivated(world, x, y, z, player, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        int amount = 0;
        if(itemStack.getTagCompound() != null) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(getTankNBTName()));
            if(fluid != null)
                amount = fluid.amount;
        }
        return DamageIndicatedItemComponent.getInfo(amount, getTankCapacity());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }

}
