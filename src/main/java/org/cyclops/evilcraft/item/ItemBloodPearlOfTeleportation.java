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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(canConsume(100, itemStack, player)) {
            this.consume(100, itemStack, player);
            world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            
            if (!world.isRemote()) {
            	EntityBloodPearl pearl = new EntityBloodPearl(world, player);
                // MCP: shoot
                pearl.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.0F, 0.0F);
                pearl.setMotion(pearl.getMotion().mul(3, 3, 3));
                world.addEntity(pearl);
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
