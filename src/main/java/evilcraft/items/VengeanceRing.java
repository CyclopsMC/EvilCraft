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

	private static final int BONUS_TICK_MODULUS = 10;
	private static final int BONUS_POTION_DURATION = 3 * 20;
	
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
     */
    @SuppressWarnings("unchecked")
	public static void toggleVengeanceArea(World world, Entity entity, int area,
			boolean enableVengeance, boolean spawnRandom) {
    	double x = entity.posX;
    	double y = entity.posY;
    	double z = entity.posZ;
    	
    	AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
    	List<VengeanceSpirit> spirits = world.getEntitiesWithinAABBExcludingEntity(entity, box,
    			new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity entity) {
				return entity instanceof VengeanceSpirit;
			}
        	
        });
    	
    	for(VengeanceSpirit spirit : spirits) {
    		spirit.setEnabledVengeance((EntityPlayer) entity, enableVengeance);
    	}
    	
    	if(spirits.size() == 0) {
    		VengeanceSpirit.spawnRandom(world, (int) Math.round(x), (int) Math.round(y)
    				, (int) Math.round(z), area, entity);
    	}
    }
    
    /**
     * Give bonus abilities to the given player.
     * @param player The player to receive the powers.
     */
    public static void updateRingPowers(EntityPlayer player) {
    	player.addPotionEffect(new PotionEffect(Potion.jump.id, BONUS_POTION_DURATION, 2, true));
    	player.addPotionEffect(new PotionEffect(Potion.invisibility.id, BONUS_POTION_DURATION, 1, true));
    	player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, BONUS_POTION_DURATION, 1, true));
    	player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, BONUS_POTION_DURATION, 1, true));
	}
    
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(ItemHelpers.isActivated(itemStack) && entity instanceof EntityPlayer && !world.isRemote
        		&& Helpers.efficientTick(world, BONUS_TICK_MODULUS)) {
        	int area = VengeanceRingConfig.areaOfEffect;
        	toggleVengeanceArea(world, entity, area, ItemHelpers.isActivated(itemStack), true);
        	updateRingPowers((EntityPlayer) entity);
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
