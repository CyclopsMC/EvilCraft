package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.FluidBlockBlood;
import evilcraft.blocks.FluidBlockBloodConfig;

/**
 * Item food that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableItemBucket extends ItemBucket implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    protected boolean canPickUp = true;
    
    public static ElementType TYPE = ElementType.ITEM;
    
    protected ConfigurableItemBucket(ExtendedConfig eConfig, int blockID) {
        super(eConfig.ID, blockID);
        eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        setContainerItem(Item.bucketEmpty);
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
    
}
