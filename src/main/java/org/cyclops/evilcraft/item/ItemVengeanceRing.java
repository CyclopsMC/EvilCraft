package org.cyclops.evilcraft.item;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;

import java.util.List;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
public class ItemVengeanceRing extends Item {

    private static final int BONUS_TICK_MODULUS = 5;
    private static final int BONUS_POTION_DURATION = 3 * 20;
    // Array of effects, each element: potion ID, duration, potion level.
    private static final List<Triple<MobEffect, Integer, Integer>> RING_POWERS =
            Lists.<Triple<MobEffect, Integer, Integer>>newArrayList(
                    Triple.of(MobEffects.JUMP, BONUS_POTION_DURATION, 2),
                    Triple.of(MobEffects.INVISIBILITY, BONUS_POTION_DURATION, 1),
                    Triple.of(MobEffects.MOVEMENT_SPEED, BONUS_POTION_DURATION, 1),
                    Triple.of(MobEffects.DIG_SPEED, BONUS_POTION_DURATION, 1)
            );

    public ItemVengeanceRing(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(player.isCrouching()) {
            if(!world.isClientSide())
                ItemHelpers.toggleActivation(itemStack);
            return MinecraftHelpers.successAction(itemStack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    /**
     * Toggle vengeance around the given entity within the given area of effect.
     * @param world The world.
     * @param entity The entity to activate vengeance around.
     * @param area The area size.
     * @param enableVengeance If vengeance should be enabled (if false, will be disabled)
     * @param spawnRandom If a random spirit should be spawned when no spirits where in the area.
     * @param forceGlobal If global vengeance should be enabled for newly spawned spirits. This
     * is of no use of spawnRandom is not true.
     */
    @SuppressWarnings("unchecked")
    public static void toggleVengeanceArea(Level world, Entity entity, int area,
            boolean enableVengeance, boolean spawnRandom, boolean forceGlobal) {
        if(world.getDifficulty() != Difficulty.PEACEFUL) {
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            BlockPos blockPos = entity.blockPosition();

            // Look for spirits in an area.
            AABB box = new AABB(x, y, z, x, y, z).inflate(area, area, area);
            List<EntityVengeanceSpirit> spirits = world.getEntitiesOfClass(EntityVengeanceSpirit.class, box,
                    new Predicate<Entity>() {

                        @Override
                        public boolean apply(Entity entity) {
                            return entity instanceof EntityVengeanceSpirit;
                        }

                    });

            // Vengeance all the spirits in the neighbourhood
            for(EntityVengeanceSpirit spirit : spirits) {
                spirit.setEnabledVengeance((Player) entity, enableVengeance);
                if(enableVengeance) {
                    spirit.setTarget((LivingEntity) entity);
                } else if(spirit.getTarget() == entity) {
                    spirit.setTarget(null);
                }
            }

            // If no spirits were found in an area, we spawn a new one and make him angry.
            if(spirits.size() == 0 && enableVengeance) {
                EntityVengeanceSpirit spirit = EntityVengeanceSpirit.spawnRandom(world, blockPos, area / 4);
                if(spirit != null) {
                    if(forceGlobal) {
                        spirit.setGlobalVengeance(true);
                    } else {
                        spirit.setEnabledVengeance((Player) entity, true);
                    }
                    spirit.setTarget((LivingEntity) entity);
                    int chance = EntityVengeanceSpiritConfig.nonDegradedSpawnChance;
                    spirit.setSwarm(chance <= 0 || world.random.nextInt(chance) > 0);
                }
            }
        }
    }

    /**
     * Give bonus abilities to the given player.
     * @param player The player to receive the powers.
     */
    public static void updateRingPowers(Player player) {
        for(Triple<MobEffect, Integer, Integer> power : RING_POWERS) {
            player.addEffect(new MobEffectInstance(power.getLeft(), power.getMiddle(), power.getRight(), false, true));
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int par4, boolean par5) {
        if(entity instanceof Player && !world.isClientSide()
                && WorldHelpers.efficientTick(world, BONUS_TICK_MODULUS, entity.getId())) {
            int area = ItemVengeanceRingConfig.areaOfEffect;
            toggleVengeanceArea(world, entity, area, ItemHelpers.isActivated(itemStack), true, false);
            if(ItemHelpers.isActivated(itemStack)) {
                updateRingPowers((Player) entity);
            }
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getDescriptionId() + ".info.status");
    }

}
