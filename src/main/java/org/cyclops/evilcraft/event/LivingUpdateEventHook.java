package org.cyclops.evilcraft.event;

import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockExcrementPile;
import org.cyclops.evilcraft.entity.monster.EntityWerewolf;

/**
 * Event hook for {@link LivingUpdateEvent}.
 * @author rubensworks
 *
 */
public class LivingUpdateEventHook {

    private static final int CHANCE_DROP_EXCREMENT = 500; // Real chance is 1/CHANCE_DROP_EXCREMENT
    private static final int CHANCE_DIE_WITHOUT_ANY_REASON = 1000000; // Real chance is 1/CHANCE_DIE_WITHOUT_ANY_REASON

    /**
     * When a sound event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void tick(LivingUpdateEvent event) {
    	if(WorldHelpers.efficientTick(event.getEntity().world, 80)) {
	        dropExcrement(event);
	        dieWithoutAnyReason(event);
	        transformWerewolfVillager(event);
    	}
    }

    private void dropExcrement(LivingUpdateEvent event) {
        if(event.getEntity() instanceof AnimalEntity
        		&& !event.getEntity().world.isRemote()
                && event.getEntity().world.rand.nextInt(CHANCE_DROP_EXCREMENT) == 0) {
            AnimalEntity entity = (AnimalEntity) event.getEntity();
            World world = entity.world;
            BlockPos blockPos = entity.getPosition();
            if(world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.getBlockState(blockPos.add(0, -1, 0)).isNormalCube(world, blockPos.add(0, -1, 0))) {
                world.setBlockState(blockPos, RegistryEntries.BLOCK_EXCREMENT_PILE.getDefaultState());
            } else if(world.getBlockState(blockPos).getBlock() instanceof BlockExcrementPile) {
                BlockExcrementPile.heightenPileAt(world, blockPos);
            }
        }
    }

    private void dieWithoutAnyReason(LivingUpdateEvent event) {
        if(event.getEntity() instanceof PlayerEntity && GeneralConfig.dieWithoutAnyReason
        		&& event.getEntity().world.rand.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0
        		&& !event.getEntity().world.isRemote()) {
            PlayerEntity entity = (PlayerEntity) event.getEntity();
            entity.attackEntityFrom(ExtendedDamageSource.dieWithoutAnyReason, Float.MAX_VALUE);
        }
    }

    private void transformWerewolfVillager(LivingUpdateEvent event) {
        if(event.getEntity() instanceof VillagerEntity && !event.getEntity().world.isRemote()) {
            VillagerEntity villager = (VillagerEntity) event.getEntity();
            if(EntityWerewolf.isWerewolfTime(event.getEntity().world)
                    && villager.getVillagerData().getProfession() == RegistryEntries.VILLAGER_PROFESSION_WEREWOLF
                    && villager.world.getLightFor(LightType.SKY, villager.getPosition()) > 0) {
                EntityWerewolf.replaceVillager(villager);
            }
        }
    }
    
}
