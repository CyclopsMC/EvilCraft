package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
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

    // TODO
    /*@SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack itemStack, EntityPlayer player, int useRemaining, EnumHand hand) {
        if(itemStack.getItem() == this && player.getHeldItem(hand) != null
                && getItemInUseDuration(player) != getMaxItemUseDuration(itemStack)) {
            return modelArray[Math.min(this.modelArray.length - 1,
                    (player.getItemInUseDuration() / 3))];
        }
        return super.getModel(itemStack, player, useRemaining);
    }*/

    private int getItemInUseDuration(EntityLivingBase player) {
    	return Math.max(0, player.getItemInUseMaxCount() - player.getItemInUseCount());
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		if(getItemInUseDuration(player) > 0) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
		} else {
            player.setActiveHand(hand);
		}
        return MinecraftHelpers.successAction(itemStack);
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
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityLivingBase player, int duration) {
    	if(player.worldObj.isRemote && getItemInUseDuration(player) > 6) {
	    	// Play stop sound
	    	EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
	    			"vengeanceBeamStop", SoundCategory.AMBIENT, 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    	}
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityLivingBase player, int duration) {
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
        			"vengeanceBeamStart", SoundCategory.AMBIENT, 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
