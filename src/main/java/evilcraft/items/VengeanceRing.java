package evilcraft.items;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.IInformationProvider;
import evilcraft.api.ItemHelpers;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.monster.VengeanceSpirit;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
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
    	double x = entity.posX;
    	double y = entity.posY;
    	double z = entity.posZ;
    	
    	// Look for spirits in an area.
    	AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
    	List<VengeanceSpirit> spirits = world.getEntitiesWithinAABBExcludingEntity(entity, box,
    			new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity entity) {
				return entity instanceof VengeanceSpirit;
			}
        	
        });
    	
    	// Vengeance all the spirits in the neighbourhood
    	for(VengeanceSpirit spirit : spirits) {
    		spirit.setEnabledVengeance((EntityPlayer) entity, enableVengeance);
    		if(enableVengeance) {
    			spirit.setTarget(entity);
    		} else if(spirit.getEntityToAttack() == entity) {
    			spirit.setTarget(null);
    		}
    	}
    	
    	// If no spirits were found in an area, we spawn a new one and make him angry.
    	if(spirits.size() == 0 && enableVengeance) {
    		VengeanceSpirit spirit = VengeanceSpirit.spawnRandom(world, (int) Math.round(x),
    				(int) Math.round(y) , (int) Math.round(z), area / 4);
    		if(spirit != null) {
    			if(forceGlobal) {
    				spirit.setGlobalVengeance(true);
    			} else {
    				spirit.setEnabledVengeance((EntityPlayer) entity, enableVengeance);
    			}
    			spirit.setTarget(entity);
    		}
    	}
    }
    
    /**
     * Give bonus abilities to the given player.
     * @param player The player to receive the powers.
     */
    public static void updateRingPowers(EntityPlayer player) {
    	for(int[] power : RING_POWERS) {
    		player.addPotionEffect(new PotionEffect(power[0], power[1], power[2], true));
    	}
	}
    
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer && !world.isRemote
        		&& Helpers.efficientTick(world, BONUS_TICK_MODULUS)) {
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
        list.add(IInformationProvider.INFO_PREFIX + L10NHelpers.getLocalizedInfo(this, ".main"));
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
        		getUnlocalizedName() + ".info.status");
    }

}
