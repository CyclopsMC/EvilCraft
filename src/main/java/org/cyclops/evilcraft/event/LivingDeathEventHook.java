package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
        Entity e = event.getSource().getEntity();
        if(e != null && e instanceof EntityPlayerMP && !e.worldObj.isRemote
                && event.getEntityLiving() != null && Configs.isEnabled(BloodExtractorConfig.class)) {
        	float boost = 1.0F;
            EntityPlayerMP player = (EntityPlayerMP) e;
            EnumHand hand = player.getActiveHand();
            if(Configs.isEnabled(VeinSwordConfig.class) && player.getHeldItem(hand) != null
            		&& player.getHeldItem(hand).getItem() == VeinSword.getInstance()) {
            	boost = (float) VeinSwordConfig.extractionBoost;
            }
            float health = event.getEntityLiving().getMaxHealth();
            int minimumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.minimumMobMultiplier * boost);
            int maximumMB = MathHelper.floor_float(health * (float) BloodExtractorConfig.maximumMobMultiplier * boost);
            BloodExtractor.getInstance().fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }
    
    private void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.getSource() == DamageSource.fall
                && Configs.isEnabled(BloodStainedBlockConfig.class)
                && !(event.getEntity() instanceof VengeanceSpirit)) {
            int x = MathHelper.floor_double(event.getEntity().posX);
            int y = MathHelper.floor_double(event.getEntity().posY - event.getEntity().getYOffset() - 1);
            int z = MathHelper.floor_double(event.getEntity().posZ);
            BlockPos pos = new BlockPos(x, y, z);
            Block block = event.getEntity().worldObj.getBlockState(pos).getBlock();
            if(BloodStainedBlock.getInstance().canSetInnerBlock(event.getEntity().worldObj.getBlockState(pos), block, event.getEntity().worldObj, pos)
            		|| block == BloodStainedBlock.getInstance()) {
                if (!event.getEntity().worldObj.isRemote) {
                    // Transform blockState into blood stained version
                	BloodStainedBlock.getInstance().stainBlock(event.getEntity().worldObj, pos,
                			(int) (BloodStainedBlockConfig.bloodMBPerHP * event.getEntityLiving().getMaxHealth()));
                } else {
                    // Init particles
                    Random random = new Random();
                    EntityBloodSplashFX.spawnParticles(event.getEntity().worldObj, pos.add(0, 1, 0), ((int) event.getEntityLiving().getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
        if (event.getEntityLiving() != null) {
            World world = event.getEntityLiving().worldObj;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!world.isRemote && !(world instanceof FakeWorld)
                    && world.getDifficulty() != EnumDifficulty.PEACEFUL
                    && Configs.isEnabled(VengeanceSpiritConfig.class)
                    && VengeanceSpirit.canSustain(event.getEntityLiving())
                    && (directToPlayer || VengeanceSpirit.canSpawnNew(world, event.getEntityLiving().getPosition()))) {
                VengeanceSpirit spirit = new VengeanceSpirit(world);
                spirit.setInnerEntity(event.getEntityLiving());
                spirit.copyLocationAndAnglesFrom(event.getEntityLiving());
                world.spawnEntityInWorld(spirit);
                if(directToPlayer) {
                    EntityPlayer player = (EntityPlayer) event.getSource().getSourceOfDamage();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setAttackTarget(player);
                }
            }
        }
	}

    private boolean shouldDirectSpiritToPlayer(LivingDeathEvent event) {
        if(event.getSource().getSourceOfDamage() instanceof EntityPlayer && Configs.isEnabled(VengeanceRingConfig.class)) {
            EntityPlayer player = (EntityPlayer) event.getSource().getSourceOfDamage();
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
		if(event.getEntityLiving() instanceof EntityPlayerMP
				&& Configs.isEnabled(WerewolfFleshConfig.class)
				&& !event.getEntityLiving().worldObj.isRemote
                && event.getEntityLiving().worldObj.rand.nextInt(WerewolfFleshConfig.humanoidFleshDropChance) == 0) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
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
        if(event.getSource() == ExtendedDamageSource.paling && Configs.isEnabled(SpiritPortalConfig.class)) {
            SpiritPortal.tryPlacePortal(event.getEntityLiving().worldObj, event.getEntityLiving().getPosition().add(0, 1, 0));
        }
    }
    
}
