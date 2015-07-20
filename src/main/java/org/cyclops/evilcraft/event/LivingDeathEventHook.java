package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.BloodStainedBlockConfig;
import org.cyclops.evilcraft.block.SpiritPortal;
import org.cyclops.evilcraft.block.SpiritPortalConfig;
import org.cyclops.evilcraft.client.particle.EntityBloodSplashFX;
import org.cyclops.evilcraft.core.world.FakeWorld;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritConfig;
import org.cyclops.evilcraft.item.*;

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
            BlockPos pos = new BlockPos(x, y, z);
            Block block = event.entity.worldObj.getBlockState(pos).getBlock();
            if(BloodStainedBlock.getInstance().canSetInnerBlock(block, event.entity.worldObj, pos)
            		|| block == BloodStainedBlock.getInstance()) {
                if (!event.entity.worldObj.isRemote) {
                    // Transform blockState into blood stained version
                	BloodStainedBlock.getInstance().stainBlock(event.entity.worldObj, pos,
                			(int) (BloodStainedBlockConfig.bloodMBPerHP * event.entityLiving.getMaxHealth()));
                } else {
                    // Init particles
                    Random random = new Random();
                    EntityBloodSplashFX.spawnParticles(event.entity.worldObj, pos.add(0, 1, 0), ((int) event.entityLiving.getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
        if (event.entityLiving != null) {
            World world = event.entityLiving.worldObj;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!world.isRemote && !(world instanceof FakeWorld)
                    && world.getDifficulty() != EnumDifficulty.PEACEFUL
                    && Configs.isEnabled(VengeanceSpiritConfig.class)
                    && VengeanceSpirit.canSustain(event.entityLiving)
                    && (directToPlayer || VengeanceSpirit.canSpawnNew(world, event.entityLiving.getPosition()))) {
                VengeanceSpirit spirit = new VengeanceSpirit(world);
                spirit.setInnerEntity(event.entityLiving);
                spirit.copyLocationAndAnglesFrom(event.entityLiving);
                world.spawnEntityInWorld(spirit);
                if(directToPlayer) {
                    EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setAttackTarget(player);
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
            NBTUtil.writeGameProfile(tag, player.getGameProfile());
			double x = player.posX;
			double y = player.posY;
			double z = player.posZ;
			EntityItem entity = new EntityItem(player.worldObj, x, y, z, itemStack);
			player.worldObj.spawnEntityInWorld(entity);
		}
	}

    private void palingDeath(LivingDeathEvent event) {
        if(event.source == ExtendedDamageSource.paling && Configs.isEnabled(SpiritPortalConfig.class)) {
            SpiritPortal.tryPlacePortal(event.entityLiving.worldObj, event.entityLiving.getPosition().add(0, 1, 0));
        }
    }
    
}
