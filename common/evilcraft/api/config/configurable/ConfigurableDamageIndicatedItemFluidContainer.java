package evilcraft.api.config.configurable;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
 * @author Ruben Taelman
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
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking() && isPlaceFluids()) {
            FluidStack fluidStack = getFluid(itemStack);
            // Empty container
            FluidStack drained = this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, false);
            
            ForgeDirection direction = ForgeDirection.getOrientation(side);
            x += direction.offsetX;
            y += direction.offsetY;
            z += direction.offsetZ;
            
            if(drained != null && drained.amount == FluidContainerRegistry.BUCKET_VOLUME && (world.isAirBlock(x, y, z) || world.getBlockId(x, y, z) == fluidStack.getFluid().getBlockID())) {
                this.drain(itemStack, FluidContainerRegistry.BUCKET_VOLUME, true);
                world.setBlock(x, y, z, fluidStack.getFluid().getBlockID(), 0, 3);
            }
        }
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
