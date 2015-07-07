package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.entity.item.EntityBloodPearl;
import evilcraft.fluid.Blood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Ender pearl that runs on blood.
 * @author rubensworks
 *
 */
public class BloodPearlOfTeleportation extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodPearlOfTeleportation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodPearlOfTeleportation getInstance() {
        return _instance;
    }

    public BloodPearlOfTeleportation(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, 1000, Blood.getInstance());
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(canConsume(100, itemStack, player)) {
            this.consume(100, itemStack, player);
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

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }
    
}
