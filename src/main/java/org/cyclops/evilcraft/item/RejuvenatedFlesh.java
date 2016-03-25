package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.fluid.Blood;

/**
 * Flesh which can be infinitely eaten, consuming Blood per bite.
 * @author rubensworks
 *
 */
public class RejuvenatedFlesh extends ConfigurableDamageIndicatedItemFluidContainer {

    private static RejuvenatedFlesh _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static RejuvenatedFlesh getInstance() {
        return _instance;
    }

    public RejuvenatedFlesh(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, RejuvenatedFleshConfig.containerSize, Blood.getInstance());
        this.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.EAT;
    }

    protected boolean canEat(ItemStack itemStack) {
        FluidStack fluidStack = getFluid(itemStack);
        return fluidStack != null && fluidStack.amount >= RejuvenatedFleshConfig.biteUsage;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(canEat(itemStack) && player.canEat(false)) {
            player.setActiveHand(hand);
            return MinecraftHelpers.successAction(itemStack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entity) {
        drain(itemStack, RejuvenatedFleshConfig.biteUsage, true);
        if(entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).getFoodStats().addStats(3, 0.5F);
        }
        world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.entity_player_burp, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }

}
