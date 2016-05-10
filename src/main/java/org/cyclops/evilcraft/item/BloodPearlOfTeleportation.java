package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.entity.item.EntityBloodPearl;
import org.cyclops.evilcraft.fluid.Blood;

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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(canConsume(100, itemStack, player)) {
            this.consume(100, itemStack, player);
            world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            
            if (!world.isRemote) {
            	EntityBloodPearl pearl = new EntityBloodPearl(world, player);
                // Last three params: pitch offset, velocity, inaccuracy
                pearl.func_184538_a(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            	pearl.motionX *= 3;
            	pearl.motionY *= 3;
            	pearl.motionZ *= 3;
                world.spawnEntityInWorld(pearl);
            }

            return MinecraftHelpers.successAction(itemStack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }
    
}
