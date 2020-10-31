package org.cyclops.evilcraft.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritPortal;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.item.ItemBloodExtractor;
import org.cyclops.evilcraft.item.ItemBloodExtractorConfig;
import org.cyclops.evilcraft.item.ItemVeinSword;
import org.cyclops.evilcraft.item.ItemVeinSwordConfig;
import org.cyclops.evilcraft.item.ItemVengeanceRing;
import org.cyclops.evilcraft.item.ItemWerewolfFleshConfig;

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
        Entity e = event.getSource().getTrueSource();
        if(e != null && e instanceof ServerPlayerEntity && !e.world.isRemote()
                && event.getEntityLiving() != null) {
        	float boost = 1.0F;
            ServerPlayerEntity player = (ServerPlayerEntity) e;
            Hand hand = player.getActiveHand();
            if(player.getHeldItem(hand) != null
            		&& player.getHeldItem(hand).getItem() instanceof ItemVeinSword) {
            	boost = (float) ItemVeinSwordConfig.extractionBoost;
            }
            float health = event.getEntityLiving().getMaxHealth();
            int minimumMB = MathHelper.floor(health * (float) ItemBloodExtractorConfig.minimumMobMultiplier * boost);
            int maximumMB = MathHelper.floor(health * (float) ItemBloodExtractorConfig.maximumMobMultiplier * boost);
            ItemBloodExtractor.fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }
    
    private void bloodStainedBlockEvent(LivingDeathEvent event) {
	    // TODO: rewrite blood stained blocks
	    /*
        if(event.getSource() == DamageSource.FALL
                && !(event.getEntity() instanceof EntityVengeanceSpirit)) {
            int x = MathHelper.floor(event.getEntity().getPosX());
            int y = MathHelper.floor(event.getEntity().getPosY() - (event.getEntity().height - 1));
            int z = MathHelper.floor(event.getEntity().getPosZ());
            BlockPos pos = new BlockPos(x, y, z);
            Block block = event.getEntity().world.getBlockState(pos).getBlock();
            if(BlockBloodStained.getInstance().canSetInnerBlock(event.getEntity().world.getBlockState(pos), block, event.getEntity().world, pos)
            		|| block == BlockBloodStained.getInstance()) {
                if (!event.getEntity().world.isRemote()) {
                    // Transform blockState into blood stained version
                    FMLCommonHandler.instance().getMinecraftServerInstance().futureTaskQueue.add(
                            ListenableFutureTask.create(Executors.callable(() -> {
                                // Only in the next tick, to resolve #601.
                                // The problem is that Vanilla's logic for handling fall events caches the Block.
                                // But Forge throws the living death event _after_ this block is determined,
                                // after which vanilla can still perform operators with this block.
                                // In some cases, this can result in inconsistencies, which can lead to crashes.
                                BlockBloodStained.getInstance().stainBlock(event.getEntity().world, pos,
                                        (int) (BlockBloodStainedConfig.bloodMBPerHP * event.getEntityLiving().getMaxHealth()));
                            })));
                } else {
                    // Init particles
                    Random random = new Random();
                    ParticleBloodSplash.spawnParticles(event.getEntity().world, pos.add(0, 1, 0), ((int) event.getEntityLiving().getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }*/
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
        if (event.getEntityLiving() != null) {
            World world = event.getEntityLiving().world;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!world.isRemote()
                    && world.getDifficulty() != Difficulty.PEACEFUL
                    && EntityVengeanceSpirit.canSustain(event.getEntityLiving())
                    && (directToPlayer || EntityVengeanceSpirit.canSpawnNew(world, event.getEntityLiving().getPosition()))) {
                EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(world);
                spirit.setInnerEntity(event.getEntityLiving());
                spirit.copyLocationAndAnglesFrom(event.getEntityLiving());
                world.addEntity(spirit);
                if(directToPlayer) {
                    PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setAttackTarget(player);
                }
            }
        }
	}

    private boolean shouldDirectSpiritToPlayer(LivingDeathEvent event) {
        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            for(PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
                ItemStack itemStack = it.next();
                if(!itemStack.isEmpty() && itemStack.getItem() instanceof ItemVengeanceRing) {
                    return true;
                }
            }
        }
        return false;
    }
	
	private void dropHumanoidFleshEvent(LivingDeathEvent event) {
		if(event.getEntityLiving() instanceof ServerPlayerEntity
				&& !event.getEntityLiving().world.isRemote()
                && event.getEntityLiving().world.rand.nextInt(ItemWerewolfFleshConfig.humanoidFleshDropChance) == 0) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
			ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_FLESH_HUMANOID);
			CompoundNBT tag = itemStack.getTag();
			if(tag == null) {
				tag = new CompoundNBT();
				itemStack.setTag(tag);
			}
            NBTUtil.writeGameProfile(tag, player.getGameProfile());
			double x = player.getPosX();
			double y = player.getPosY();
			double z = player.getPosZ();
			ItemEntity entity = new ItemEntity(player.world, x, y, z, itemStack);
			player.world.addEntity(entity);
		}
	}

    private void palingDeath(LivingDeathEvent event) {
        if(event.getSource() == ExtendedDamageSource.paling) {
            BlockSpiritPortal.tryPlacePortal(event.getEntityLiving().world, event.getEntityLiving().getPosition().add(0, 1, 0));
        }
    }
    
}
