package evilcraft.api.config.configurable;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
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

    protected ExtendedConfig eConfig = null;
    protected boolean canPickUp = true;

    public static ElementType TYPE = ElementType.ITEM;

    protected ConfigurableDamageIndicatedItemFluidContainer(ExtendedConfig eConfig, int capacity, Fluid fluid) {
        super(eConfig.ID, capacity, fluid);
        if(eConfig != null)
            eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

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

    public boolean isEntity() {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking()) {
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

}
