package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceFocus extends ConfigurableItem {
	
	private static final int TICK_MODULUS = 3;
    
    private static VengeanceFocus _instance = null;
    
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
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() == VengeanceFocus.this ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

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
                    EvilCraftSoundEvents.effect_vengeancebeam_stop, SoundCategory.AMBIENT,
                    0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    	}
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityLivingBase player, int remaining) {
        int duration = getMaxItemUseDuration(itemStack) - remaining;
        if(duration > 6) {
    		if(WorldHelpers.efficientTick(player.worldObj, TICK_MODULUS, player.getEntityId())) {
		    	EntityAntiVengeanceBeam beam = new EntityAntiVengeanceBeam(player.worldObj, player);
		    	if(!player.worldObj.isRemote) {
                    // Last three params: pitch offset, velocity, inaccuracy
                    beam.func_184538_a(player, player.rotationPitch, player.rotationYaw, 0F, 0.5F, 1.0F);
		    		player.worldObj.spawnEntityInWorld(beam);
		        }
    		}
    	} else {
    		if(duration == 3 && player.worldObj.isRemote) {
			// Play start sound
    		EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
                    EvilCraftSoundEvents.effect_vengeancebeam_start, SoundCategory.AMBIENT,
                    0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
