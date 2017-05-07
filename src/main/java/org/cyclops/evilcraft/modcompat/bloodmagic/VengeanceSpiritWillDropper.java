package org.cyclops.evilcraft.modcompat.bloodmagic;

import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;

/**
 * Will's will be dropped when Vengeance Spirits are killed.
 * @author rubensworks
 */
public class VengeanceSpiritWillDropper {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        EntityLivingBase attackedEntity = event.getEntityLiving();
        if (attackedEntity instanceof VengeanceSpirit && event.getSource() instanceof ExtendedDamageSource.VengeanceBeamDamageSource) {
            double amountOfSouls = attackedEntity.getEntityWorld().rand.nextDouble() * 20;
            ItemStack soulStack = ((IDemonWill) ModItems.MONSTER_SOUL).createWill(0, amountOfSouls);
            event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
        }
    }

}
