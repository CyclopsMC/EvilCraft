package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Abstract grenade class.
 * @author immortaleeb
 *
 */
public abstract class AbstractGrenade extends ConfigurableItem {

    protected AbstractGrenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            if (!player.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            world.playSound(player, player.posX, player.posY, player.posZ, new SoundEvent(new ResourceLocation("random.bow")), SoundCategory.MASTER, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            EntityThrowable entity = getThrowableEntity(itemStack, world, player);
            // Last three params: pitch offset, velocity, inaccuracy
            entity.func_184538_a(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntityInWorld(entity);
        }
        return MinecraftHelpers.successAction(itemStack);
    }
    
    protected abstract EntityThrowable getThrowableEntity(ItemStack itemStack, World world, EntityPlayer player);
}
