package org.cyclops.evilcraft.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritConfig;
import org.cyclops.evilcraft.modcompat.baubles.BaublesModCompat;

import java.util.List;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class VengeanceRing extends ConfigurableItem implements IBauble {

	private static final int BONUS_TICK_MODULUS = 5;
	private static final int BONUS_POTION_DURATION = 3 * 20;
	// Array of effects, each element: potion ID, duration, potion level.
	private static final List<Triple<Potion, Integer, Integer>> RING_POWERS =
			Lists.<Triple<Potion, Integer, Integer>>newArrayList(
					Triple.of(MobEffects.jump, BONUS_POTION_DURATION, 2),
					Triple.of(MobEffects.invisibility, BONUS_POTION_DURATION, 1),
					Triple.of(MobEffects.moveSpeed, BONUS_POTION_DURATION, 1),
					Triple.of(MobEffects.digSpeed, BONUS_POTION_DURATION, 1)
			);
	
    private static VengeanceRing _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeanceRing getInstance() {
        return _instance;
    }

	public VengeanceRing(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking()) {
            if(!world.isRemote)
            	ItemHelpers.toggleActivation(itemStack);
            return MinecraftHelpers.successAction(itemStack);
        }
        return super.onItemRightClick(itemStack, world, player, hand);
    }
    
    @Optional.Method(modid = Reference.MOD_BAUBLES)
    private void equipBauble(ItemStack itemStack, EntityPlayer player) {
    	IInventory inventory = BaublesApi.getBaubles(player);
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			if(inventory.getStackInSlot(i) == null && inventory.isItemValidForSlot(i, itemStack)) {
				inventory.setInventorySlotContents(i, itemStack.copy());
				if(!player.capabilities.isCreativeMode){
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				onEquipped(itemStack, player);
				break;
			}
		}
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
    	if(world.getDifficulty() != EnumDifficulty.PEACEFUL) {
	    	double x = entity.posX;
	    	double y = entity.posY;
	    	double z = entity.posZ;
            BlockPos blockPos = entity.getPosition();
	    	
	    	// Look for spirits in an area.
	    	AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).expand(area, area, area);
	    	List<VengeanceSpirit> spirits = world.getEntitiesWithinAABB(VengeanceSpirit.class, box,
					new Predicate<Entity>() {

						@Override
						public boolean apply(Entity entity) {
							return entity instanceof VengeanceSpirit;
						}

					});
	    	
	    	// Vengeance all the spirits in the neighbourhood
	    	for(VengeanceSpirit spirit : spirits) {
	    		spirit.setEnabledVengeance((EntityPlayer) entity, enableVengeance);
	    		if(enableVengeance) {
	    			spirit.setAttackTarget((EntityLivingBase) entity);
	    		} else if(spirit.getAttackTarget() == entity) {
	    			spirit.setAttackTarget(null);
	    		}
	    	}

	    	// If no spirits were found in an area, we spawn a new one and make him angry.
	    	if(spirits.size() == 0 && enableVengeance) {
	    		VengeanceSpirit spirit = VengeanceSpirit.spawnRandom(world, blockPos, area / 4);
	    		if(spirit != null) {
	    			if(forceGlobal) {
	    				spirit.setGlobalVengeance(true);
	    			} else {
	    				spirit.setEnabledVengeance((EntityPlayer) entity, true);
	    			}
	    			spirit.setAttackTarget((EntityLivingBase) entity);
                    int chance = VengeanceSpiritConfig.nonDegradedSpawnChance;
                    spirit.setIsSwarm(chance <= 0 || world.rand.nextInt(chance) > 0);
	    		}
	    	}
    	}
    }
    
    /**
     * Give bonus abilities to the given player.
     * @param player The player to receive the powers.
     */
    public static void updateRingPowers(EntityPlayer player) {
    	for(Triple<Potion, Integer, Integer> power : RING_POWERS) {
    		player.addPotionEffect(new PotionEffect(power.getLeft(), power.getMiddle(), power.getRight(), false, true));
    	}
	}
    
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer && !world.isRemote
        		&& WorldHelpers.efficientTick(world, BONUS_TICK_MODULUS, entity.getEntityId())) {
        	int area = VengeanceRingConfig.areaOfEffect;
        	toggleVengeanceArea(world, entity, area, ItemHelpers.isActivated(itemStack), true, false);
        	if(ItemHelpers.isActivated(itemStack)) {
        		updateRingPowers((EntityPlayer) entity);
        	}
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
				getUnlocalizedName() + ".info.status");
    }

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
		return BaublesModCompat.canUse();
	}

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
		return true;
	}

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.RING;
	}

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {
		
	}

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {
		
	}

	@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {
		if(BaublesModCompat.canUse()) {
			this.onUpdate(itemStack, entity.worldObj, entity, 0, false);
		}
	}

}
