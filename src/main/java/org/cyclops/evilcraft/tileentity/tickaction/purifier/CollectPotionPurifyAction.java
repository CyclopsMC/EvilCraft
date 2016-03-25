package org.cyclops.evilcraft.tileentity.tickaction.purifier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
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
    public static final Item ALLOWED_ITEM = Items.glass_bottle;

    private static final int PURIFY_DURATION = 60;

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == ALLOWED_ITEM;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canWork(TilePurifier tile) {
        if(tile.getPurifyItem() == null && tile.getAdditionalItem() != null &&
                tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<EntityLivingBase> entities = tile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
                    new AxisAlignedBB(tile.getPos(), tile.getPos().add(1, 2, 1))
            );
            for(EntityLivingBase entity : entities) {
                for(PotionEffect potionEffect : (Collection<PotionEffect>) entity.getActivePotionEffects()) {
                    if(!potionEffect.getIsAmbient()) {
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
        World world = tile.getWorld();
        int tick = tile.getTick();

        // Try removing bad enchants.
        if(tile.getPurifyItem() == null && tile.getAdditionalItem() != null
                && tile.getAdditionalItem().getItem() == ALLOWED_ITEM && tile.getBucketsFloored() == tile.getMaxBuckets()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            List<EntityLivingBase> entities = tile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
                    new AxisAlignedBB(tile.getPos(), tile.getPos().add(1, 2, 1))
            );
            for(EntityLivingBase entity : entities) {
                if(!entity.getActivePotionEffects().isEmpty()) {
                    if(tick >= PURIFY_DURATION) {
                        if(!world.isRemote) {
                            for(PotionEffect potionEffect : (Collection<PotionEffect>) entity.getActivePotionEffects()) {
                                if(!potionEffect.getIsAmbient()) {
                                    // Remove effect from entity
                                    entity.removePotionEffect(potionEffect.getPotion());

                                    ItemStack itemStack = new ItemStack(Items.potionitem);

                                    // Add potion effects
                                    NBTTagCompound tag = new NBTTagCompound();
                                    NBTTagList tagList = new NBTTagList();
                                    NBTTagCompound potionTag = new NBTTagCompound();
                                    itemStack.setTagCompound(tag);
                                    potionEffect.writeCustomPotionEffectToNBT(potionTag);
                                    tagList.appendTag(potionTag);
                                    tag.setTag("CustomPotionEffects", tagList);

                                    // Set correct meta value
                                    // TODO: this might be improved?
                                    itemStack.setItemDamage(8229);

                                    // Update purifier state
                                    tile.setBuckets(0, 0);
                                    tile.setAdditionalItem(itemStack);
                                    return true;
                                }
                            }
                        }

                    }
                    if(world.isRemote) {
                        tile.showEffect();
                    }
                }
            }
        }

        return false;
    }

}
