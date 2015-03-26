package evilcraft.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.ExtendedDamageSource;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.BloodStainedBlockConfig;
import evilcraft.block.SpiritPortal;
import evilcraft.block.SpiritPortalConfig;
import evilcraft.client.particle.EntityBloodSplashFX;
import evilcraft.core.PlayerExtendedInventoryIterator;
import evilcraft.core.algorithm.Location;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.world.FakeWorld;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import evilcraft.item.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

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
        palingDeath(event);
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
        if(event.source == DamageSource.fall
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
        if (event.entityLiving != null) {
            World world = event.entityLiving.worldObj;
            double x = event.entityLiving.posX;
            double y = event.entityLiving.posY;
            double z = event.entityLiving.posZ;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!world.isRemote && !(world instanceof FakeWorld)
                    && world.difficultySetting != EnumDifficulty.PEACEFUL
                    && Configs.isEnabled(VengeanceSpiritConfig.class)
                    && VengeanceSpirit.canSustain(event.entityLiving)
                    && (directToPlayer || VengeanceSpirit.canSpawnNew(world, x, y, z))) {
                VengeanceSpirit spirit = new VengeanceSpirit(world);
                spirit.setInnerEntity(event.entityLiving);
                spirit.copyLocationAndAnglesFrom(event.entityLiving);
                spirit.onSpawnWithEgg((IEntityLivingData) null);
                world.spawnEntityInWorld(spirit);
                if(directToPlayer) {
                    EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setTarget(player);
                }
            }
        }
	}

    private boolean shouldDirectSpiritToPlayer(LivingDeathEvent event) {
        if(event.source.getSourceOfDamage() instanceof EntityPlayer && Configs.isEnabled(VengeanceRingConfig.class)) {
            EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
            for(PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
                ItemStack itemStack = it.next();
                if(itemStack != null && itemStack.getItem() == VengeanceRing.getInstance()) {
                    return true;
                }
            }
        }
        return false;
    }
	
	private void dropHumanoidFleshEvent(LivingDeathEvent event) {
		if(event.entityLiving instanceof EntityPlayerMP
				&& Configs.isEnabled(WerewolfFleshConfig.class)
				&& !event.entityLiving.worldObj.isRemote
                && event.entityLiving.worldObj.rand.nextInt(WerewolfFleshConfig.humanoidFleshDropChance) == 0) {
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

    private void palingDeath(LivingDeathEvent event) {
        if(event.source == ExtendedDamageSource.paling && Configs.isEnabled(SpiritPortalConfig.class)) {
            int x = (int) (event.entityLiving.posX - event.entityLiving.width / 2);
            int y = (int) (event.entityLiving.posY - event.entityLiving.height / 2 + 1);
            int z = (int) (event.entityLiving.posZ - event.entityLiving.width / 2);
            SpiritPortal.tryPlacePortal(event.entityLiving.worldObj, x, y, z);
        }
    }
    
}
