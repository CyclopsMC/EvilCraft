package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.item.DamageIndicatedItemFluidContainer;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableDamageIndicatedItemFluidContainer extends DamageIndicatedItemFluidContainer implements Configurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ITEM;

    protected boolean canPickUp = true;
    private boolean placeFluids = false;

    /**
     * Make a new block instance.
     * @param eConfig Config for this item.
     * @param capacity The capacity for the fluid container this item should have.
     * @param fluid The fluid this container should be able to hold.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected ConfigurableDamageIndicatedItemFluidContainer(ExtendedConfig eConfig, int capacity, Fluid fluid) {
        super(eConfig.ID, capacity, fluid);
        if(eConfig != null)
            eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getUniqueName() {
        return "items."+eConfig.NAMEDID;
    }

    @Override
    public String getIconString() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }

    @Override
    public boolean isEntity() {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        FluidStack fluidStack = getFluid(itemStack);
        FluidStack drained = this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, false);
        int blockID = getFluid().getBlockID();

        boolean hasBucket = drained != null
                && (drained.amount == FluidContainerRegistry.BUCKET_VOLUME);
        boolean hasSpace = fluidStack == null
                || (fluidStack.amount + FluidContainerRegistry.BUCKET_VOLUME <= getCapacity(itemStack));
        MovingObjectPosition movingobjectpositionDrain = this.getMovingObjectPositionFromPlayer(world, player, false);
        MovingObjectPosition movingobjectpositionFill = this.getMovingObjectPositionFromPlayer(world, player, true);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (movingobjectpositionFill.typeOfHit == EnumMovingObjectType.TILE) {
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
                if (world.getBlockId(x, y, z) == blockID && world.getBlockMetadata(x, y, z) == 0) {
                    if(hasSpace) {
                        world.setBlockToAir(x, y, z);
                        this.fill(itemStack, new FluidStack(getFluid(), FluidContainerRegistry.BUCKET_VOLUME), true);
                    }
                    return itemStack;
                }
            }

            // Drain container and place fluid block
            if (hasBucket && movingobjectpositionDrain.typeOfHit == EnumMovingObjectType.TILE) {
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

                if (this.tryPlaceContainedLiquid(world, x, y, z, blockID, hasBucket)) {
                    this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, true);
                    return itemStack;
                }
            }
        }
        return itemStack;
    }

    private boolean tryPlaceContainedLiquid(World world, int x, int y, int z, int blockID, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            Material material = world.getBlockMaterial(x, y, z);

            if (!world.isAirBlock(x, y, z) && material.isSolid()) {
                return false;
            } else {
                if (!world.isRemote && !material.isSolid() && !material.isLiquid()) {
                    world.destroyBlock(x, y, z, true);
                }

                world.setBlock(x, y, z, blockID, 0, 3);

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

}
