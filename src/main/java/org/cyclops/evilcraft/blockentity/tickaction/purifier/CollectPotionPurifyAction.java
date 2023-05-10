package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.block.BlockPurifierConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;

import java.util.List;

/**
 * Purifier action to collect entity potion effects in an empty bottle.
 * @author Ruben Taelman
 */
public class CollectPotionPurifyAction implements IPurifierAction {

    /**
     * The allowed item instance.
     */
    public static final Item ALLOWED_ITEM = Items.GLASS_BOTTLE;

    private static final int PURIFY_DURATION = 60;

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getItem() == ALLOWED_ITEM;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canWork(BlockEntityPurifier tile) {
        if(tile.getPurifyItem().isEmpty() && !tile.getAdditionalItem().isEmpty() &&
                tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<LivingEntity> entities = tile.getLevel().getEntitiesOfClass(LivingEntity.class,
                    new AABB(tile.getBlockPos(), tile.getBlockPos().offset(1, 2, 1))
            );
            for(LivingEntity entity : entities) {
                for(MobEffectInstance potionEffect : entity.getActiveEffects()) {
                    if(!potionEffect.isAmbient()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean work(BlockEntityPurifier tile) {
        Level world = tile.getLevel();
        int tick = tile.getTick();

        // Try removing bad enchants.
        if(tile.getPurifyItem().isEmpty() && !tile.getAdditionalItem().isEmpty()
                && tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<LivingEntity> entities = tile.getLevel().getEntitiesOfClass(LivingEntity.class,
                    new AABB(tile.getBlockPos(), tile.getBlockPos().offset(1, 2, 1))
            );
            for(LivingEntity entity : entities) {
                if(!entity.getActiveEffects().isEmpty()) {
                    if(tick >= PURIFY_DURATION) {
                        if(!world.isClientSide()) {
                            for(MobEffectInstance potionEffect : entity.getActiveEffects()) {
                                if(!potionEffect.isAmbient()) {
                                    // Remove effect from entity
                                    entity.removeEffect(potionEffect.getEffect());

                                    // Limit duration of effect that can be taken
                                    if (BlockPurifierConfig.maxPotionEffectDuration > 0 && potionEffect.getDuration() > BlockPurifierConfig.maxPotionEffectDuration) {
                                        int remainingDuration = potionEffect.getDuration() - BlockPurifierConfig.maxPotionEffectDuration;
                                        MobEffectInstance remainingEffect = new MobEffectInstance(potionEffect);
                                        remainingEffect.duration = remainingDuration;
                                        entity.addEffect(remainingEffect);

                                        potionEffect.duration = BlockPurifierConfig.maxPotionEffectDuration;
                                    }

                                    ItemStack itemStack = new ItemStack(Items.POTION);

                                    // Add potion effects
                                    CompoundTag tag = new CompoundTag();
                                    ListTag tagList = new ListTag();
                                    CompoundTag potionTag = new CompoundTag();
                                    itemStack.setTag(tag);
                                    potionEffect.save(potionTag);
                                    tagList.add(potionTag);
                                    tag.put("CustomPotionEffects", tagList);

                                    // Update purifier state
                                    tile.setBuckets(0, 0);
                                    tile.setAdditionalItem(itemStack);
                                    return true;
                                }
                            }
                        }

                    }
                    if(world.isClientSide()) {
                        tile.showEffect();
                    }
                }
            }
        }

        return false;
    }

}
