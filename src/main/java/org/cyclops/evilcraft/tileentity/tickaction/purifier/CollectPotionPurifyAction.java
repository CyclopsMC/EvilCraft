package org.cyclops.evilcraft.tileentity.tickaction.purifier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.tileentity.TilePurifier;

import java.util.Collection;
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
    public boolean canWork(TilePurifier tile) {
        if(tile.getPurifyItem().isEmpty() && !tile.getAdditionalItem().isEmpty() &&
                tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<LivingEntity> entities = tile.getLevel().getEntitiesOfClass(LivingEntity.class,
                    new AxisAlignedBB(tile.getBlockPos(), tile.getBlockPos().offset(1, 2, 1))
            );
            for(LivingEntity entity : entities) {
                for(EffectInstance potionEffect : entity.getActiveEffects()) {
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
    public boolean work(TilePurifier tile) {
        World world = tile.getLevel();
        int tick = tile.getTick();

        // Try removing bad enchants.
        if(tile.getPurifyItem().isEmpty() && !tile.getAdditionalItem().isEmpty()
                && tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<LivingEntity> entities = tile.getLevel().getEntitiesOfClass(LivingEntity.class,
                    new AxisAlignedBB(tile.getBlockPos(), tile.getBlockPos().offset(1, 2, 1))
            );
            for(LivingEntity entity : entities) {
                if(!entity.getActiveEffects().isEmpty()) {
                    if(tick >= PURIFY_DURATION) {
                        if(!world.isClientSide()) {
                            for(EffectInstance potionEffect : entity.getActiveEffects()) {
                                if(!potionEffect.isAmbient()) {
                                    // Remove effect from entity
                                    entity.removeEffect(potionEffect.getEffect());

                                    ItemStack itemStack = new ItemStack(Items.POTION);

                                    // Add potion effects
                                    CompoundNBT tag = new CompoundNBT();
                                    ListNBT tagList = new ListNBT();
                                    CompoundNBT potionTag = new CompoundNBT();
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
