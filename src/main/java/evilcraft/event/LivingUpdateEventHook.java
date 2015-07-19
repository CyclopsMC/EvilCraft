package evilcraft.event;

import evilcraft.Configs;
import evilcraft.ExtendedDamageSource;
import evilcraft.GeneralConfig;
import evilcraft.block.ExcrementPile;
import evilcraft.block.ExcrementPileConfig;
import evilcraft.entity.monster.Werewolf;
import evilcraft.entity.villager.WerewolfVillagerConfig;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.helper.WorldHelpers;

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
    public void onLivingUpdate(LivingUpdateEvent event) {
    	if(WorldHelpers.efficientTick(event.entity.worldObj, 80)) {
	        dropExcrement(event);
	        dieWithoutAnyReason(event);
	        transformWerewolfVillager(event);
    	}
    }
    
    private void dropExcrement(LivingUpdateEvent event) {
        if(event.entity instanceof EntityAnimal && Configs.isEnabled(ExcrementPileConfig.class)
        		&& !event.entity.worldObj.isRemote
                && event.entity.worldObj.rand.nextInt(CHANCE_DROP_EXCREMENT) == 0) {
            EntityAnimal entity = (EntityAnimal) event.entity;
            World world = entity.worldObj;
            BlockPos blockPos = entity.getPosition();
            if(world.getBlockState(blockPos).getBlock() == Blocks.air && world.getBlockState(blockPos.add(0, -1, 0)).getBlock().isNormalCube()) {
                world.setBlockState(blockPos, ExcrementPile.getInstance().getDefaultState());
            } else if(world.getBlockState(blockPos).getBlock() == ExcrementPile.getInstance()) {
                ExcrementPile.getInstance().heightenPileAt(world, blockPos);
            }
        }
    }
    
    private void dieWithoutAnyReason(LivingUpdateEvent event) {
        if(event.entity instanceof EntityPlayer && GeneralConfig.dieWithoutAnyReason
        		&& event.entity.worldObj.rand.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0
        		&& !event.entity.worldObj.isRemote) {
            EntityPlayer entity = (EntityPlayer) event.entity;
            entity.attackEntityFrom(ExtendedDamageSource.dieWithoutAnyReason, Float.MAX_VALUE);
        }
    }
    
    private void transformWerewolfVillager(LivingUpdateEvent event) {
        if(event.entity instanceof EntityVillager && !event.entity.worldObj.isRemote) {
            EntityVillager villager = (EntityVillager) event.entity;
            if(Werewolf.isWerewolfTime(event.entity.worldObj) && Configs.isEnabled(WerewolfVillagerConfig.class)
                    && villager.getProfession() == WerewolfVillagerConfig._instance.getId()) {
                Werewolf.replaceVillager(villager);
            }
        }
    }
    
}
