package org.cyclops.evilcraft.item;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;

import java.util.List;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
public class ItemVengeanceRing extends Item {

	private static final int BONUS_TICK_MODULUS = 5;
	private static final int BONUS_POTION_DURATION = 3 * 20;
	// Array of effects, each element: potion ID, duration, potion level.
	private static final List<Triple<Effect, Integer, Integer>> RING_POWERS =
			Lists.<Triple<Effect, Integer, Integer>>newArrayList(
					Triple.of(Effects.JUMP_BOOST, BONUS_POTION_DURATION, 2),
					Triple.of(Effects.INVISIBILITY, BONUS_POTION_DURATION, 1),
					Triple.of(Effects.SPEED, BONUS_POTION_DURATION, 1),
					Triple.of(Effects.HASTE, BONUS_POTION_DURATION, 1)
			);

	public ItemVengeanceRing(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
        if(player.isCrouching()) {
            if(!world.isRemote())
            	ItemHelpers.toggleActivation(itemStack);
            return MinecraftHelpers.successAction(itemStack);
        }
        return super.onItemRightClick(world, player, hand);
    }

	@Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    /**
     * Toggle vengeance around the given entity within the given area of effect.
     * @param world The world.
     * @param entity The entity to activate vengeance around.
     * @param area The area size.
     * @param enableVengeance If vengeance should be enabled (if false, will be disabled)
     * @param spawnRandom If a random spirit should be spawned when no spirits where in the area.
     * @param forceGlobal If global vengeance should be enabled for newly spawned spirits. This
     * is of no use of spawnRandom is not true.
     */
    @SuppressWarnings("unchecked")
	public static void toggleVengeanceArea(World world, Entity entity, int area,
			boolean enableVengeance, boolean spawnRandom, boolean forceGlobal) {
    	if(world.getDifficulty() != Difficulty.PEACEFUL) {
	    	double x = entity.getPosX();
	    	double y = entity.getPosY();
	    	double z = entity.getPosZ();
            BlockPos blockPos = entity.getPosition();
	    	
	    	// Look for spirits in an area.
	    	AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).grow(area, area, area);
	    	List<EntityVengeanceSpirit> spirits = world.getEntitiesWithinAABB(EntityVengeanceSpirit.class, box,
					new Predicate<Entity>() {

						@Override
						public boolean apply(Entity entity) {
							return entity instanceof EntityVengeanceSpirit;
						}

					});
	    	
	    	// Vengeance all the spirits in the neighbourhood
	    	for(EntityVengeanceSpirit spirit : spirits) {
	    		spirit.setEnabledVengeance((PlayerEntity) entity, enableVengeance);
	    		if(enableVengeance) {
	    			spirit.setAttackTarget((LivingEntity) entity);
	    		} else if(spirit.getAttackTarget() == entity) {
	    			spirit.setAttackTarget(null);
	    		}
	    	}

	    	// If no spirits were found in an area, we spawn a new one and make him angry.
	    	if(spirits.size() == 0 && enableVengeance) {
	    		EntityVengeanceSpirit spirit = EntityVengeanceSpirit.spawnRandom(world, blockPos, area / 4);
	    		if(spirit != null) {
	    			if(forceGlobal) {
	    				spirit.setGlobalVengeance(true);
	    			} else {
	    				spirit.setEnabledVengeance((PlayerEntity) entity, true);
	    			}
	    			spirit.setAttackTarget((LivingEntity) entity);
                    int chance = EntityVengeanceSpiritConfig.nonDegradedSpawnChance;
                    spirit.setSwarm(chance <= 0 || world.rand.nextInt(chance) > 0);
	    		}
	    	}
    	}
    }
    
    /**
     * Give bonus abilities to the given player.
     * @param player The player to receive the powers.
     */
    public static void updateRingPowers(PlayerEntity player) {
    	for(Triple<Effect, Integer, Integer> power : RING_POWERS) {
    		player.addPotionEffect(new EffectInstance(power.getLeft(), power.getMiddle(), power.getRight(), false, true));
    	}
	}
    
	@Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof PlayerEntity && !world.isRemote()
        		&& WorldHelpers.efficientTick(world, BONUS_TICK_MODULUS, entity.getEntityId())) {
        	int area = ItemVengeanceRingConfig.areaOfEffect;
        	toggleVengeanceArea(world, entity, area, ItemHelpers.isActivated(itemStack), true, false);
        	if(ItemHelpers.isActivated(itemStack)) {
        		updateRingPowers((PlayerEntity) entity);
        	}
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
	public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
		super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
				getTranslationKey() + ".info.status");
    }

}
