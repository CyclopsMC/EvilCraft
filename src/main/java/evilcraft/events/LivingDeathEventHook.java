package evilcraft.events;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.items.BloodExtractor;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.render.particle.EntityBloodSplashFX;

/**
 * Event for {@link LivingDeathEvent}.
 * @author rubensworks
 *
 */
public class LivingDeathEventHook {

    /**
     * When a living death event is received.
     * @param event The received event.
     */
	@SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDeath(LivingDeathEvent event) {
        bloodObtainEvent(event);
        bloodStainedBlockEvent(event);
        vengeanceEvent(event);
    }

	private void bloodObtainEvent(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP && !e.worldObj.isRemote
                && event.entityLiving != null && Configs.isEnabled(BloodExtractorConfig.class)) {
            EntityPlayerMP player = (EntityPlayerMP) e;
            float health = event.entityLiving.getMaxHealth();
            int minimumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.minimumMobMultiplier);
            int maximumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.maximumMobMultiplier);
            BloodExtractor.getInstance().fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }
    
    private void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.source.damageType == DamageSource.fall.damageType
                && Configs.isEnabled(BloodStainedBlockConfig.class)) {
            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY - event.entity.getYOffset() - 1);
            int z = MathHelper.floor_double(event.entity.posZ);
            Block block = event.entity.worldObj.getBlock(x, y, z);
            int meta = BloodStainedBlock.getInstance().getMetadataFromBlock(block);
            if(meta > -1) {
                if (!event.entity.worldObj.isRemote) {
                    // Transform block into blood stained version
                    event.entity.worldObj.setBlock(x, y, z, BloodStainedBlock.getInstance());
                    event.entity.worldObj.setBlockMetadataWithNotify(x, y, z, meta, 2);
                } else {
                    // Init particles
                    Random random = new Random();
                    EntityBloodSplashFX.spawnParticles(event.entity.worldObj, x, y + 1, z, ((int)event.entityLiving.getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
		if(event.entityLiving != null) {
			World world = event.entityLiving.worldObj;
			if(!world.isRemote && VengeanceSpirit.canSustain(event.entityLiving)) {
				VengeanceSpirit spirit = new VengeanceSpirit(world);
				spirit.setInnerEntity(event.entityLiving);
				spirit.copyLocationAndAnglesFrom(event.entityLiving);
				spirit.onSpawnWithEgg((IEntityLivingData)null);
				world.spawnEntityInWorld(spirit);
			}
		}
	}
    
}
