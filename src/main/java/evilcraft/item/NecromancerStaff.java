package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.entity.effect.EntityNecromancersHead;
import evilcraft.fluid.Blood;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A staff that can summon evil creatures that will target another entity.
 * The evil creatures will die instantly when the target is dead and will not drop items.
 * The creatures have less HP than in their regular form.
 * @author rubensworks
 *
 */
public class NecromancerStaff extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static NecromancerStaff _instance = null;
    
    private static final int CONTAINER_SIZE = NecromancerStaffConfig.capacity;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new NecromancerStaff(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NecromancerStaff getInstance() {
        return _instance;
    }

    private NecromancerStaff(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CONTAINER_SIZE, Blood.getInstance());
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    protected void throwNecromancersHead(EntityLivingBase entityLiving, Class<? extends EntityLiving> mobType) {
    	EntityNecromancersHead head = new EntityNecromancersHead(entityLiving.worldObj, entityLiving);
    	if(!entityLiving.worldObj.isRemote) {
    		head.setMobType(mobType);
    		entityLiving.worldObj.spawnEntityInWorld(head);
        }
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if(canConsume(NecromancerStaffConfig.usage, itemStack, player)) {
			consume(NecromancerStaffConfig.usage, itemStack, player);
			Class<? extends EntityLiving> mobType = EntityZombie.class; // Other types might be allowed in the future.
			throwNecromancersHead(player, mobType);
			return itemStack;
		}
        return super.onItemRightClick(itemStack, world, player);
    }

}
