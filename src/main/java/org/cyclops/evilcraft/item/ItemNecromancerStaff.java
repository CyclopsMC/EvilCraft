package org.cyclops.evilcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;

/**
 * A staff that can summon evil creatures that will target another entity.
 * The evil creatures will die instantly when the target is dead and will not drop items.
 * The creatures have less HP than in their regular form.
 * @author rubensworks
 *
 */
public class ItemNecromancerStaff extends ItemBloodContainer {
    
    private static final int CONTAINER_SIZE = ItemNecromancerStaffConfig.capacity;

    public ItemNecromancerStaff(Item.Properties properties) {
        super(properties, CONTAINER_SIZE);
    }
    
    protected void throwNecromancersHead(LivingEntity entityLiving, Class<? extends MobEntity> mobType) {
    	EntityNecromancersHead head = new EntityNecromancersHead(entityLiving.level, entityLiving);
    	if(!entityLiving.level.isClientSide()) {
    		head.setMobType(mobType);
            // Last three params: pitch offset, velocity, inaccuracy
            // MCP: shoot
            head.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, -20.0F, 0.5F, 1.0F);
    		entityLiving.level.addFreshEntity(head);
        }
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
		if(canConsume(ItemNecromancerStaffConfig.usage, itemStack, player)) {
			consume(ItemNecromancerStaffConfig.usage, itemStack, player);
			Class<? extends MobEntity> mobType = ZombieEntity.class; // Other types might be allowed in the future.
			throwNecromancersHead(player, mobType);
			return MinecraftHelpers.successAction(itemStack);
		}
        return super.use(world, player, hand);
    }

}
