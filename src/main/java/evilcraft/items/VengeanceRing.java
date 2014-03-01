package evilcraft.items;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.ItemHelpers;
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
     */
    @SuppressWarnings("unchecked")
	public static void toggleVengeanceArea(World world, Entity entity, int area, boolean enableVengeance) {
    	double x = entity.posX;
    	double y = entity.posY;
    	double z = entity.posZ;
    	
    	AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
    	List<VengeanceSpirit> spirits = world.getEntitiesWithinAABBExcludingEntity(entity, box, new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity entity) {
				return entity instanceof VengeanceSpirit;
			}
        	
        });
    	
    	for(VengeanceSpirit spirit : spirits) {
    		spirit.setEnabledVengeance((EntityPlayer) entity, enableVengeance);
    	}
    }
    
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer && !world.isRemote) {
        	int area = VengeanceRingConfig.areaOfEffect;
        	toggleVengeanceArea(world, entity, area, ItemHelpers.isActivated(itemStack));
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(Helpers.getLocalizedInfo(this, ".main"));
        String autoSupply = EnumChatFormatting.RESET + StatCollector.translateToLocal(getUnlocalizedName() + ".info.disabled");
        if(ItemHelpers.isActivated(itemStack)) {
            autoSupply = EnumChatFormatting.GREEN + StatCollector.translateToLocal(getUnlocalizedName() + ".info.enabled");
        }
        list.add(EnumChatFormatting.BOLD + StatCollector.translateToLocal(getUnlocalizedName() + ".info.autoSupply") + " " + autoSupply);
    }

}
