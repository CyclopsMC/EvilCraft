package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.entities.item.EntityBloodPearl;
import evilcraft.fluids.Blood;

/**
 * Ender pearl that runs on blood.
 * @author rubensworks
 *
 */
public class BloodPearlOfTeleportation extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodPearlOfTeleportation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodPearlOfTeleportation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodPearlOfTeleportation getInstance() {
        return _instance;
    }

    private BloodPearlOfTeleportation(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, 1000, Blood.getInstance());
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        FluidStack fluidStack = null;
        if(itemStack != null && itemStack.stackTagCompound != null)
            fluidStack = this.drain(itemStack, 100, false);
        if(fluidStack != null && fluidStack.amount > 0) {
            if (!player.capabilities.isCreativeMode) {
                this.drain(itemStack, 100, true);
            }
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            
            if (!world.isRemote) {
            	EntityBloodPearl pearl = new EntityBloodPearl(world, player);
            	pearl.motionX *= 3;
            	pearl.motionY *= 3;
            	pearl.motionZ *= 3;
                world.spawnEntityInWorld(pearl);
            }

            return itemStack;
        }
        return itemStack;
    }
    
}
