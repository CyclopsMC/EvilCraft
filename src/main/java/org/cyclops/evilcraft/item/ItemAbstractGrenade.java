package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Abstract grenade class.
 * @author immortaleeb
 *
 */
public abstract class ItemAbstractGrenade extends Item {

    protected ItemAbstractGrenade(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!world.isRemote()) {
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), new SoundEvent(new ResourceLocation("random.bow")), SoundCategory.MASTER, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            ThrowableEntity entity = getThrowableEntity(itemStack, world, player);
            // MCP: shoot
            entity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.addEntity(entity);
        }
        return MinecraftHelpers.successAction(itemStack);
    }
    
    protected abstract ThrowableEntity getThrowableEntity(ItemStack itemStack, World world, PlayerEntity player);
}
