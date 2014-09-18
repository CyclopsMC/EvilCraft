package evilcraft.event;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.BloodStainedBlockConfig;
import evilcraft.client.particle.EntityBloodSplashFX;
import evilcraft.core.algorithm.Location;
import evilcraft.core.world.FakeWorld;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import evilcraft.item.BloodExtractor;
import evilcraft.item.BloodExtractorConfig;
import evilcraft.item.VeinSword;
import evilcraft.item.VeinSwordConfig;
import evilcraft.item.WerewolfFlesh;
import evilcraft.item.WerewolfFleshConfig;

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
        dropHumanoidFleshEvent(event);
    }

	private void bloodObtainEvent(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP && !e.worldObj.isRemote
                && event.entityLiving != null && Configs.isEnabled(BloodExtractorConfig.class)) {
        	float boost = 1.0F;
            EntityPlayerMP player = (EntityPlayerMP) e;
            if(Configs.isEnabled(VeinSwordConfig.class) && player.getHeldItem() != null
            		&& player.getHeldItem().getItem() == VeinSword.getInstance()) {
            	boost = (float) VeinSwordConfig.extractionBoost;
            }
            float health = event.entityLiving.getMaxHealth();
            int minimumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.minimumMobMultiplier * boost);
            int maximumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.maximumMobMultiplier * boost);
            BloodExtractor.getInstance().fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }
    
    private void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.source.damageType == DamageSource.fall.damageType
                && Configs.isEnabled(BloodStainedBlockConfig.class)
                && !(event.entity instanceof VengeanceSpirit)) {
            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY - event.entity.getYOffset() - 1);
            int z = MathHelper.floor_double(event.entity.posZ);
            Block block = event.entity.worldObj.getBlock(x, y, z);
            if(BloodStainedBlock.getInstance().canSetInnerBlock(block, event.entity.worldObj, x, y, z)
            		|| block == BloodStainedBlock.getInstance()) {
                if (!event.entity.worldObj.isRemote) {
                    // Transform block into blood stained version
                	BloodStainedBlock.getInstance().stainBlock(event.entity.worldObj, new Location(x, y, z),
                			(int) (BloodStainedBlockConfig.bloodMBPerHP * event.entityLiving.getMaxHealth()));
                } else {
                    // Init particles
                    Random random = new Random();
                    EntityBloodSplashFX.spawnParticles(event.entity.worldObj, x, y + 1, z, ((int)event.entityLiving.getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
		double x = event.entityLiving.posX;
		double y = event.entityLiving.posY;
		double z = event.entityLiving.posZ;
		if(event.entityLiving != null) {
			World world = event.entityLiving.worldObj;
			if(!world.isRemote && !(world instanceof FakeWorld)
					&& world.difficultySetting != EnumDifficulty.PEACEFUL
					&& Configs.isEnabled(VengeanceSpiritConfig.class)
					&& VengeanceSpirit.canSustain(event.entityLiving)
					&& VengeanceSpirit.canSpawnNew(world, x, y, z)) {
				VengeanceSpirit spirit = new VengeanceSpirit(world);
				spirit.setInnerEntity(event.entityLiving);
				spirit.copyLocationAndAnglesFrom(event.entityLiving);
				spirit.onSpawnWithEgg((IEntityLivingData)null);
				world.spawnEntityInWorld(spirit);
			}
		}
	}
	
	private void dropHumanoidFleshEvent(LivingDeathEvent event) {
		if(event.entityLiving instanceof EntityPlayerMP
				&& Configs.isEnabled(WerewolfFleshConfig.class)
				&& !event.entityLiving.worldObj.isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
			ItemStack itemStack = new ItemStack(WerewolfFlesh.getInstance(), 1, 1);
			NBTTagCompound tag = itemStack.getTagCompound();
			if(tag == null) {
				tag = new NBTTagCompound();
				itemStack.setTagCompound(tag);
			}
			NBTUtil.func_152460_a(tag, player.getGameProfile());
			double x = player.posX;
			double y = player.posY;
			double z = player.posZ;
			EntityItem entity = new EntityItem(player.worldObj, x, y, z, itemStack);
			player.worldObj.spawnEntityInWorld(entity);
		}
	}
    
}
