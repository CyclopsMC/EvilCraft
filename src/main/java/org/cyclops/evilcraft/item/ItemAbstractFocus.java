package org.cyclops.evilcraft.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraftSoundEvents;

import net.minecraft.item.Item.Properties;

/**
 * A base focus item.
 * @author rubensworks
 *
 */
public abstract class ItemAbstractFocus extends Item {

	private static final int TICK_MODULUS = 3;

    public ItemAbstractFocus(Properties properties) {
        super(properties);
        if (MinecraftHelpers.isClientSide()) {
            registerProperties();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerProperties() {
        ItemModelsProperties.register(this, new ResourceLocation("pull"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, ClientWorld worldIn, LivingEntity entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemStack = entityIn.getUseItem();
                    return !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAbstractFocus ? (float)(stack.getUseDuration() - entityIn.getUseItemRemainingTicks()) / 20.0F : 0.0F;
                }
            }
        });
        ItemModelsProperties.register(this, new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, ClientWorld worldIn, LivingEntity entityIn) {
                return entityIn != null && entityIn.isUsingItem() && entityIn.getUseItem() == stack ? 1.0F : 0.0F;
            }
        });
    }

    private int getItemInUseDuration(LivingEntity player) {
    	return Math.max(0, player.getTicksUsingItem() - player.getUseItemRemainingTicks());
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
		if(getItemInUseDuration(player) > 0) {
			return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStack);
		} else {
            player.startUsingItem(hand);
		}
        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    public UseAction getUseAnimation(ItemStack itemStack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
	public void releaseUsing(ItemStack itemStack, World world, LivingEntity player, int duration) {
    	if(player.level.isClientSide() && getItemInUseDuration(player) > 6) {
	    	// Play stop sound
	    	player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_stop, 0.6F + player.level.random.nextFloat() * 0.2F, 1.0F);
    	}
    }

    protected abstract ThrowableEntity newBeamEntity(LivingEntity player);

    @Override
    public void onUsingTick(ItemStack itemStack, LivingEntity player, int remaining) {
        int duration = getUseDuration(itemStack) - remaining;
        if(duration > 6) {
    		if(WorldHelpers.efficientTick(player.level, TICK_MODULUS, player.getId())) {
                ThrowableEntity beam = newBeamEntity(player);
		    	if(!player.level.isClientSide()) {
                    // Last three params: pitch offset, velocity, inaccuracy
                    // MCP: shoot
                    beam.shootFromRotation(player, player.xRot, player.yRot, 0F, 0.5F, 1.0F);
		    		player.level.addFreshEntity(beam);
		        }
    		}
    	} else {
    		if(duration == 3 && player.level.isClientSide()) {
			    // Play start sound
    		    player.playSound(EvilCraftSoundEvents.effect_vengeancebeam_start,  0.6F + player.level.random.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
