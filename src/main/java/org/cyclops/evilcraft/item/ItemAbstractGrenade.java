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

import net.minecraft.item.Item.Properties;

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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide()) {
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            world.playSound(player, player.getX(), player.getY(), player.getZ(), new SoundEvent(new ResourceLocation("random.bow")), SoundCategory.MASTER, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            ThrowableEntity entity = getThrowableEntity(itemStack, world, player);
            // MCP: shoot
            entity.shootFromRotation(player, player.xRot, player.yRot, -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
        }
        return MinecraftHelpers.successAction(itemStack);
    }
    
    protected abstract ThrowableEntity getThrowableEntity(ItemStack itemStack, World world, PlayerEntity player);
}
