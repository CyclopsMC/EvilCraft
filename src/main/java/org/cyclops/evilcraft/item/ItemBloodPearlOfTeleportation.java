package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityBloodPearl;

/**
 * Ender pearl that runs on blood.
 * @author rubensworks
 *
 */
public class ItemBloodPearlOfTeleportation extends ItemBloodContainer {

    public ItemBloodPearlOfTeleportation(Item.Properties properties) {
        super(properties, 1000);
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(canConsume(100, itemStack, player)) {
            this.consume(100, itemStack, player);
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            
            if (!world.isClientSide()) {
            	EntityBloodPearl pearl = new EntityBloodPearl(world, player);
                // MCP: shoot
                pearl.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.0F, 0.0F);
                pearl.setDeltaMovement(pearl.getDeltaMovement().multiply(3, 3, 3));
                world.addFreshEntity(pearl);
            }

            return MinecraftHelpers.successAction(itemStack);
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStack);
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.UNCOMMON;
    }
    
}
