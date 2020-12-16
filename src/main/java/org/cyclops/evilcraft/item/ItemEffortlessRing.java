package org.cyclops.evilcraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.Reference;

/**
 * A ring that allows the player to walk faster with a double step height.
 * @author rubensworks
 *
 */
public class ItemEffortlessRing extends Item {

    private static final int TICK_MODULUS = 1;
    private static final String PLAYER_NBT_KEY = Reference.MOD_ID + ":" + "lastStepSize";

    private static final float SPEED_BONUS = 0.05F;
    private static final float STEP_SIZE = 1F;
    private static final float JUMP_DISTANCE_FACTOR = 0.05F;
    private static final float JUMP_HEIGHT_FACTOR = 0.3F;
    private static final float FALLDISTANCE_REDUCTION = 2F;

    public ItemEffortlessRing(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Re-apply the ring effects.
     * @param itemStack The item.
     * @param player The player.
     */
    public void adjustParameters(ItemStack itemStack, PlayerEntity player) {
        // Speed
        if(player.moveForward > 0 && player.onGround) {
            player.moveRelative(player.isInWater() ? SPEED_BONUS / 3 : SPEED_BONUS, new Vec3d(0, 0, 1));
        }

        // Step height
        if(!player.getPersistentData().contains(PLAYER_NBT_KEY)) {
            player.getPersistentData().putFloat(PLAYER_NBT_KEY, player.stepHeight);
        }
        player.stepHeight = player.isCrouching() ? 0.5F : STEP_SIZE;

        // Jump distance
        if(!player.onGround) {
            player.jumpMovementFactor = JUMP_DISTANCE_FACTOR;
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(ItemStackHelpers.hasPlayerItem(player, this)) {
                player.setMotion(player.getMotion().add(0, JUMP_HEIGHT_FACTOR, 0));;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        // Reset the step height.
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(player.getPersistentData().contains(PLAYER_NBT_KEY)) {
                if (!ItemStackHelpers.hasPlayerItem(player, this)) {
                    player.stepHeight = player.getPersistentData().getFloat(PLAYER_NBT_KEY);
                    player.getPersistentData().remove(PLAYER_NBT_KEY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(ItemStackHelpers.hasPlayerItem(player, this)) {
                event.setDistance(event.getDistance() - FALLDISTANCE_REDUCTION);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof PlayerEntity) {
            adjustParameters(stack, (PlayerEntity) entityIn);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

}
