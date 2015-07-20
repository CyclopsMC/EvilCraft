package org.cyclops.evilcraft.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceFocus extends ConfigurableItem {
	
	private static final int TICK_MODULUS = 3;
    
    private static VengeanceFocus _instance = null;
    
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation[] modelArray;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeanceFocus getInstance() {
        return _instance;
    }

    public VengeanceFocus(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        if(MinecraftHelpers.isClientSide()) {
            modelArray = new ModelResourceLocation[4];
        }
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack itemStack, EntityPlayer player, int useRemaining) {
        if(itemStack.getItem() == this && player.getItemInUse() != null
                && getItemInUseDuration(player) != getMaxItemUseDuration(itemStack)) {
            return modelArray[Math.min(this.modelArray.length - 1,
                    (player.getItemInUseDuration() / 3))];
        }
        return super.getModel(itemStack, player, useRemaining);
    }

    private int getItemInUseDuration(EntityPlayer player) {
    	return player.isUsingItem() ? ObfuscationHelpers.getItemInUse(player).getMaxItemUseDuration()
    			- ObfuscationHelpers.getItemInUseCount(player) : 0;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if(getItemInUseDuration(player) > 0) {
			player.clearItemInUse();
		} else {
			player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		}
        return itemStack;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }
    
    @Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int duration) {
    	if(player.worldObj.isRemote && getItemInUseDuration(player) > 6) {
	    	// Play stop sound
	    	EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
	    			"vengeanceBeamStop", 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    	}
    }
    
    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int duration) {
    	if(getItemInUseDuration(player) > 6) {
    		if(WorldHelpers.efficientTick(player.worldObj, TICK_MODULUS, player.getEntityId())) {
		    	EntityAntiVengeanceBeam beam = new EntityAntiVengeanceBeam(player.worldObj, player);
		    	if(!player.worldObj.isRemote) {
		    		player.worldObj.spawnEntityInWorld(beam);
		        }
    		}
    	} else {
    		if(getItemInUseDuration(player) == 3 && player.worldObj.isRemote) {
			// Play start sound
    		EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
        			"vengeanceBeamStart", 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
