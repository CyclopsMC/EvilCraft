package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import java.util.Iterator;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
public class ItemInvigoratingPendant extends ItemBloodContainer {

	private static final int TICK_MODULUS = MinecraftHelpers.SECOND_IN_TICKS / 2;
	
    public ItemInvigoratingPendant(Item.Properties properties) {
        super(properties, ItemInvigoratingPendantConfig.capacity);
    }
    
    /**
     * Clear the bad effects of given player.
     * Each 'tick', a certain amount of bad effect duration reduction is reserved.
     * Each found effect it's duration is reduced by as much as possible (not larger than the reserved amount)
     * and the inner tank is drained according to how much was reduced.
     * If the reserved duration is not zero at the end, the next bad effect will be taken.
     * @param itemStack The pendant to drain.
     * @param player The player to receive the powers.
     */
    public void clearBadEffects(ItemStack itemStack, PlayerEntity player) {
    	int amount = ItemInvigoratingPendantConfig.usage;
    	if(canConsume(amount, itemStack, player)) {
    		
    		int originalReducableDuration = ItemInvigoratingPendantConfig.reduceDuration * MinecraftHelpers.SECOND_IN_TICKS;
    		int reducableDuration = originalReducableDuration;
    		
	    	@SuppressWarnings("unchecked")
			Iterator<EffectInstance> it = Lists.newLinkedList(player.getActiveEffects()).iterator();
	    	while(reducableDuration > 0 && it.hasNext() && canConsume(amount, itemStack, player)) {
	    		EffectInstance effect = it.next();
	    		Effect potion = effect.getEffect();
	    		
	    		boolean shouldClear = true;
	    		if(potion != null) {
	    			shouldClear = !potion.isBeneficial();
	    		}
                shouldClear = shouldClear & !effect.isAmbient();
	    		
	    		if(shouldClear) {	    			
	    			int reductionMultiplier = effect.getAmplifier() + 1;
	    			int reducableDurationForThisEffect = reducableDuration / reductionMultiplier;
	    			int remaining = effect.getDuration();
	    			int toReduce = Math.min(reducableDurationForThisEffect, remaining);
	    			int toDrain = amount;
	    			
	    			reducableDuration -= toReduce;
	    			if(remaining == toReduce) {
	    				player.removeEffect(potion);
	    			} else {
						effect.duration = remaining - toReduce;
						player.onEffectUpdated(effect, true);;
	    				toDrain = (int) Math.ceil((double) (reductionMultiplier * amount)
	    						* ((double) toReduce / (double) originalReducableDuration));
	    			}
	    			consume(toDrain, itemStack, player);
	    		}
	    	}
    	}

        if(ItemInvigoratingPendantConfig.fireUsage >= 0 && player.isOnFire() &&
                canConsume(ItemInvigoratingPendantConfig.fireUsage, itemStack, player)) {
            player.clearFire();
            consume(ItemInvigoratingPendantConfig.fireUsage, itemStack, player);
        }
	}
    
	@Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof PlayerEntity
        		&& WorldHelpers.efficientTick(world, TICK_MODULUS, entity.getId())) {
        	clearBadEffects(itemStack, (PlayerEntity) entity);
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

}
