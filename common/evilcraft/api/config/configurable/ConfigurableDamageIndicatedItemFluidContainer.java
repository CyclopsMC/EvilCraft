package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.item.DamageIndicatedItemFluidContainer;
import evilcraft.blocks.FluidBlockBloodConfig;

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
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote && this.getFluid(itemStack) != null)
            player.sendChatToPlayer(ChatMessageComponent.createFromText(this.eConfig.NAME + ": " + this.getFluid(itemStack).amount + "/" + this.capacity + " mB"));
        return super.onItemRightClick(itemStack, world, player);
    }
    
}
