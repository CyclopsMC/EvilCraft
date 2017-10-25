package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraftSoundEvents;

/**
 * A base focus item.
 * @author rubensworks
 *
 */
public abstract class AbstractFocus extends ConfigurableItem {

	private static final int TICK_MODULUS = 3;

    public AbstractFocus(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemStack = entityIn.getActiveItemStack();
                    return !itemStack.isEmpty() && itemStack.getItem() instanceof AbstractFocus ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
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
    	if(player.world.isRemote && getItemInUseDuration(player) > 6) {
	    	// Play stop sound
	    	player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_stop, 0.6F + player.world.rand.nextFloat() * 0.2F, 1.0F);
    	}
    }

    protected abstract EntityThrowable newBeamEntity(EntityLivingBase player);

    @Override
    public void onUsingTick(ItemStack itemStack, EntityLivingBase player, int remaining) {
        int duration = getMaxItemUseDuration(itemStack) - remaining;
        if(duration > 6) {
    		if(WorldHelpers.efficientTick(player.world, TICK_MODULUS, player.getEntityId())) {
                EntityThrowable beam = newBeamEntity(player);
		    	if(!player.world.isRemote) {
                    // Last three params: pitch offset, velocity, inaccuracy
                    beam.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 0.5F, 1.0F);
		    		player.world.spawnEntity(beam);
		        }
    		}
    	} else {
    		if(duration == 3 && player.world.isRemote) {
			    // Play start sound
    		    player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_start,  0.6F + player.world.rand.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
