package evilcraft.item;

import com.google.common.base.Predicate;
import evilcraft.Reference;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class VengeanceRing extends ConfigurableItem {

	private static final int BONUS_TICK_MODULUS = 5;
	private static final int BONUS_POTION_DURATION = 3 * 20;
	// Array of effects, each element: potion ID, duration, potion level.
	private static final int[][] RING_POWERS = {
		{Potion.jump.id, BONUS_POTION_DURATION, 2},
		{Potion.invisibility.id, BONUS_POTION_DURATION, 1},
		{Potion.moveSpeed.id, BONUS_POTION_DURATION, 1},
		{Potion.digSpeed.id, BONUS_POTION_DURATION, 1},
	};
	
    private static VengeanceRing _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new VengeanceRing(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeanceRing getInstance() {
        return _instance;
    }

    private VengeanceRing(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            if(!world.isRemote)
            	ItemHelpers.toggleActivation(itemStack);
            return itemStack;
        }
        return super.onItemRightClick(itemStack, world, player);
    }
    
    @Optional.Method(modid = Reference.MOD_BAUBLES)
    private void equipBauble(ItemStack itemStack, EntityPlayer player) {
    	IInventory inventory = null; //BaublesApi.getBaubles(player); TODO
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			if(inventory.getStackInSlot(i) == null && inventory.isItemValidForSlot(i, itemStack)) {
				inventory.setInventorySlotContents(i, itemStack.copy());
				if(!player.capabilities.isCreativeMode){
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				// TODO
				//onEquipped(itemStack, player);
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
	    	AxisAlignedBB box = AxisAlignedBB.fromBounds(x, y, z, x, y, z).expand(area, area, area);
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
    	for(int[] power : RING_POWERS) {
    		player.addPotionEffect(new PotionEffect(power[0], power[1], power[2], false, true));
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

	// TODO
	/*@Optional.Method(modid = Reference.MOD_BAUBLES)
	@Override
	public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
		// TODO: re-enable when baubles is updated.
		//return BaublesModCompat.canUse();
		return true;
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
		// TODO: re-enable when baubles is updated.
		if(BaublesModCompat.canUse()) {
			this.onUpdate(itemStack, entity.worldObj, entity, 0, false);
		}
	}*/

}
