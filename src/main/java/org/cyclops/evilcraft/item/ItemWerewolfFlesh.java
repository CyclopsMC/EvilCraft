package org.cyclops.evilcraft.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Random flesh drop from werewolves, gives some fine boosts at night.
 * @author rubensworks
 *
 */
public class ItemWerewolfFlesh extends Item {

    private static final int POISON_DURATION = 10;
    private static final int POWER_DURATION = 60;
    private static final int POWER_DURATION_BONUS = POWER_DURATION * 4;

    private final boolean humanoid;

    private boolean power = false;

    public ItemWerewolfFlesh(Item.Properties properties, boolean humanoid) {
        super(properties
                .food((new FoodProperties.Builder())
                        .nutrition(0)
                        .saturationModifier(0)
                        .alwaysEdible()
                        .build()));
        this.humanoid = humanoid;
        if (this.humanoid) {
            NeoForge.EVENT_BUS.addListener(this::dropHumanoidFleshEvent);
        }
    }

    /**
     * The length of one Minecraft day.
     */
    public static final int MINECRAFT_DAY = 24000;

    /**
     * Check if it's day in this world.
     * @param world The world.
     * @return If it is day in the world, checked with the world time.
     */
    public static boolean isDay(Level world) {
        return world.getDayTime() % MINECRAFT_DAY < MINECRAFT_DAY / 2;
    }

    private boolean isPower(Level world) {
        return world == null ? power : (power = !isDay(world));
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return isPower(null);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity player, int par4, boolean par5) {
        isPower(world);
    }

    private int getPowerDuration(ItemStack itemStack) {
        if(isHumanFlesh(itemStack)) {
            return POWER_DURATION;
        }
        return POWER_DURATION_BONUS;
    }

    private boolean isHumanFlesh(ItemStack itemStack) {
        return this.humanoid;
    }

    private boolean isOwnCanibal(ItemStack itemStack, Player player) {
        ResolvableProfile resolvableProfile = itemStack.get(DataComponents.PROFILE);
        if(resolvableProfile != null) {
            GameProfile profile = resolvableProfile.gameProfile();
            return player.getGameProfile().equals(profile);
        }
        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, @Nullable Level world, LivingEntity entity) {
        if(world != null && entity instanceof Player) {
            Player player = (Player) entity;
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, itemStack);
            }
            if (isOwnCanibal(itemStack, player)) {
                if (!world.isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.WITHER,
                            POISON_DURATION * 20, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,
                            getPowerDuration(itemStack) * 20, 1));
                }
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.WOLF_HURT, SoundSource.HOSTILE, 0.5F,
                        world.random.nextFloat() * 0.1F + 0.9F);
            } else if (isPower(world)) {
                int foodLevel = getFoodProperties(itemStack, entity).nutrition();
                float saturationLevel = getFoodProperties(itemStack, entity).saturation();
                player.getFoodData().eat(foodLevel, saturationLevel);
                player.getFoodData().addExhaustion(20);
                if (!world.isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.JUMP,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,
                            getPowerDuration(itemStack) * 20, 2));
                }
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE, 0.5F,
                        world.random.nextFloat() * 0.1F + 0.9F);
            } else {
                if (!world.isClientSide()) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON,
                            POISON_DURATION * 20, 1));
                }
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.WOLF_HURT, SoundSource.HOSTILE, 0.5F,
                        world.random.nextFloat() * 0.1F + 0.9F);
            }
            entity.eat(world, itemStack);
        }
        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, list, flag);
        if(isHumanFlesh(itemStack)) {
            String player = ChatFormatting.ITALIC + "None";
            ResolvableProfile resolvableProfile = itemStack.get(DataComponents.PROFILE);
            if(resolvableProfile != null) {
                GameProfile profile = resolvableProfile.gameProfile();
                if (profile != null && !profile.getProperties().isEmpty()) {
                    player = profile.getName();
                }
            }
            list.add(Component.literal("Player: ")
                    .withStyle(ChatFormatting.WHITE)
                    .append(player));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            if (((ItemWerewolfFlesh) itemStack.getItem()).isHumanFlesh(itemStack)) {
                return Helpers.RGBAToInt(255, 200, 180, 255);
            }
            return -1;
        }
    }

    public void dropHumanoidFleshEvent(LivingDeathEvent event) {
        if(event.getEntity() instanceof ServerPlayer
                && !event.getEntity().level().isClientSide()
                && event.getEntity().level().random.nextInt(ItemWerewolfFleshConfig.humanoidFleshDropChance) == 0) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            ItemStack itemStack = new ItemStack(this);
            itemStack.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            ItemEntity entity = new ItemEntity(player.level(), x, y, z, itemStack);
            player.level().addFreshEntity(entity);
        }
    }

}
