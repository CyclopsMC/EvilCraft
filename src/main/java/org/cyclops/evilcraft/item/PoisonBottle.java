package org.cyclops.evilcraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.FluidBlockPoison;
import org.cyclops.evilcraft.fluid.PoisonConfig;

/**
 * Bottle that is retrieved when right-clicking a poison source.
 * @author rubensworks
 *
 */
public class PoisonBottle extends ConfigurableItem {

    private static PoisonBottle _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PoisonBottle getInstance() {
        return _instance;
    }

    public PoisonBottle(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setPotionEffect(PotionHelper.spiderEyeEffect);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass) {
        return pass == 0 ? Helpers.RGBToInt(77, 117, 15) : super.getColorFromItemStack(itemStack, pass);
    }

    @SubscribeEvent
    public void onPoisonRightClick(PlayerInteractEvent event) {
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getHeldItem() != null &&
                event.entityPlayer.getHeldItem().getItem() == Items.glass_bottle && Configs.isEnabled(PoisonConfig.class)) {
            MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, true);
            if(pos != null && pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if(event.world.isBlockModifiable(event.entityPlayer, pos.getBlockPos()) &&
                        event.entityPlayer.canPlayerEdit(pos.getBlockPos(), pos.sideHit, event.entityPlayer.getHeldItem()) &&
                        event.world.getBlockState(pos.getBlockPos()).getBlock().getMaterial() == Material.water) {
                    if(event.world.getBlockState(pos.getBlockPos()).getBlock() == FluidBlockPoison.getInstance()) {
                        InventoryHelpers.tryReAddToStack(event.entityPlayer, event.entityPlayer.getHeldItem(), new ItemStack(this));
                        event.world.setBlockToAir(pos.getBlockPos());
                    }
                }
            }
        }
    }

}
