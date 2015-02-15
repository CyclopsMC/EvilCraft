package evilcraft.core.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.PlayerExtendedInventoryIterator;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.item.DamageIndicatedItemFluidContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableDamageIndicatedItemFluidContainer extends DamageIndicatedItemFluidContainer implements IConfigurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    protected boolean canPickUp = true;
    private boolean placeFluids = false;

    /**
     * Make a new block instance.
     * @param eConfig Config for this item.
     * @param capacity The capacity for the fluid container this item should have.
     * @param fluid The fluid this container should be able to hold.
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableDamageIndicatedItemFluidContainer(ExtendedConfig eConfig, int capacity, Fluid fluid) {
        super(capacity, fluid);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getIconString() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        FluidStack fluidStack = getFluid(itemStack);
        FluidStack drained = this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, false);
        Block block = getFluid().getBlock();

        boolean hasBucket = drained != null
                && (drained.amount == FluidContainerRegistry.BUCKET_VOLUME);
        boolean hasSpace = fluidStack == null
                || (fluidStack.amount + FluidContainerRegistry.BUCKET_VOLUME <= getCapacity(itemStack));
        MovingObjectPosition movingobjectpositionDrain = this.getMovingObjectPositionFromPlayer(world, player, false);
        MovingObjectPosition movingobjectpositionFill = this.getMovingObjectPositionFromPlayer(world, player, true);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (movingobjectpositionFill.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                // Fill the container and remove fluid block
                int x = movingobjectpositionFill.blockX;
                int y = movingobjectpositionFill.blockY;
                int z = movingobjectpositionFill.blockZ;
                if (!world.canMineBlock(player, x, y, z)) {
                    return itemStack;
                }

                if (!player.canPlayerEdit(x, y, z, movingobjectpositionFill.sideHit, itemStack)) {
                    return itemStack;
                }
                if (world.getBlock(x, y, z) == block && world.getBlockMetadata(x, y, z) == 0) {
                    if(hasSpace) {
                        world.setBlockToAir(x, y, z);
                        this.fill(itemStack, new FluidStack(getFluid(), FluidContainerRegistry.BUCKET_VOLUME), true);
                    }
                    return itemStack;
                }
            }

            // Drain container and place fluid block
            if (hasBucket && movingobjectpositionDrain.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = movingobjectpositionDrain.blockX;
                int y = movingobjectpositionDrain.blockY;
                int z = movingobjectpositionDrain.blockZ;
                if (!world.canMineBlock(player, x, y, z)) {
                    return itemStack;
                }

                ForgeDirection direction = ForgeDirection.getOrientation(movingobjectpositionDrain.sideHit);
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;

                if (!player.canPlayerEdit(x, y, z, movingobjectpositionDrain.sideHit, itemStack)) {
                    return itemStack;
                }

                if (this.tryPlaceContainedLiquid(world, x, y, z, block, true)) {
                    this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, true);
                    return itemStack;
                }
            }
        }
        return itemStack;
    }

    private boolean tryPlaceContainedLiquid(World world, int x, int y, int z, Block block, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            Material material = world.getBlock(x, y, z).getMaterial();

            if (!world.isAirBlock(x, y, z) && material.isSolid()) {
                return false;
            } else {
                if (!world.isRemote && !material.isSolid() && !material.isLiquid()) {
                	// MCP destroyBlock
                    world.func_147480_a(x, y, z, true);
                }

                world.setBlock(x, y, z, block, 0, 3);

                return true;
            }
        }
    }

    @Override
    public boolean canItemEditBlocks() {
        return true;
    }

    /**
     * If this item can place fluids when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @return If it can place fluids.
     */
    public boolean isPlaceFluids() {
        return placeFluids;
    }

    /**
     * Set whether or not this item should be able to place fluids in the world
     * when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @param placeFluids If it can place fluids.
     */
    public void setPlaceFluids(boolean placeFluids) {
        this.placeFluids = placeFluids;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
    	L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
        super.addInformation(itemStack, entityPlayer, list, par4);
    }

    protected FluidStack drainFromOthers(int amount, ItemStack itemStack, Fluid fluid, EntityPlayer player, boolean doDrain) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        FluidStack drained = null;
        while(it.hasNext() && amount > 0) {
            ItemStack current = it.next();
            if(current != null &&current != itemStack && current.getItem() instanceof IFluidContainerItem) {
                FluidStack thisDrained = ((IFluidContainerItem) current.getItem()).drain(current, amount, doDrain);
                if(thisDrained != null && thisDrained.getFluid() == fluid) {
                    if(drained == null) {
                        drained = thisDrained;
                    } else {
                        drained.amount += drained.amount;
                    }
                    amount -= drained.amount;
                }
            }
        }
        return drained;
    }

    /**
     * If this container can consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return If the given amount can be drained.
     */
    public boolean canConsume(int amount, ItemStack itemStack, EntityPlayer player) {
        if(canDrain(amount, itemStack)) return true;
        int availableAmount = 0;
        if(getFluid(itemStack) != null) {
            availableAmount = getFluid(itemStack).amount;
        }
        return drainFromOthers(amount - availableAmount, itemStack, getFluid(), player, false) != null;
    }

    /**
     * Consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return The fluid that was drained.
     */
    public FluidStack consume(int amount, ItemStack itemStack, EntityPlayer player) {
        boolean doDrain = !player.capabilities.isCreativeMode && !player.worldObj.isRemote;
        if (amount == 0) return null;
        FluidStack drained = drain(itemStack, amount, doDrain);
        if (drained != null && drained.amount == amount) return drained;
        int drainedAmount = (drained == null ? 0 : drained.amount);
        int toDrain = amount - drainedAmount;
        FluidStack otherDrained = drainFromOthers(toDrain, itemStack, getFluid(), player, doDrain);
        if (otherDrained == null) return drained;
        otherDrained.amount += drainedAmount;
        return otherDrained;
    }

}
