package evilcraft.api.config.configurable;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.item.DamageIndicatedItemComponent;
import evilcraft.fluids.Blood;
import evilcraft.items.BucketBlood;

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
                    // TODO: Remove the line below once Forge has fixed it's hashcode bug for FluidContainers.
                    fluidStack = new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME);
                    if(fluidStack != null &&
                            (fluidStack.getFluid() == tile.getTank().getFluidType()
                            || tile.getTank().getFluidType() == null) && tile.getTank().getFluidAmount() + FluidContainerRegistry.BUCKET_VOLUME <= tile.getTank().getCapacity()) {
                        tile.getTank().fill(new FluidStack(fluidStack.getFluid(), FluidContainerRegistry.BUCKET_VOLUME), true);
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
