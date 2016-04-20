package org.cyclops.evilcraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
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

import javax.annotation.Nullable;

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
        //this.setPotionEffect(PotionHelper.spiderEyeEffect); TODO
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPoisonRightClick(PlayerInteractEvent.RightClickBlock event) {
        EnumHand hand = event.getEntityPlayer().getActiveHand();
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(hand != null && event.getEntityPlayer().getHeldItem(hand) != null &&
                event.getEntityPlayer().getHeldItem(hand).getItem() == Items.glass_bottle && Configs.isEnabled(PoisonConfig.class)) {
            RayTraceResult pos = this.getMovingObjectPositionFromPlayer(event.getWorld(), event.getEntityPlayer(), true);
            if(pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) {
                if(event.getWorld().isBlockModifiable(event.getEntityPlayer(), pos.getBlockPos()) &&
                        event.getEntityPlayer().canPlayerEdit(pos.getBlockPos(), pos.sideHit, event.getEntityPlayer().getHeldItem(hand)) &&
                        event.getWorld().getBlockState(pos.getBlockPos()).getMaterial() == Material.water) {
                    if(event.getWorld().getBlockState(pos.getBlockPos()).getBlock() == FluidBlockPoison.getInstance()) {
                        InventoryHelpers.tryReAddToStack(event.getEntityPlayer(), event.getEntityPlayer().getHeldItem(hand), new ItemStack(this));
                        event.getWorld().setBlockToAir(pos.getBlockPos());
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return new ItemColor();
    }

    @SideOnly(Side.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack itemStack, int renderPass) {
            return renderPass == 0 ? Helpers.RGBToInt(77, 117, 15) : -1;
        }
    }

}
