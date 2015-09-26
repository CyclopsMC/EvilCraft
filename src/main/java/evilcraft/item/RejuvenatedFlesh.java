package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.fluid.Blood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

/**
 * Flesh which can be infinitely eaten, consuming Blood per bite.
 * @author rubensworks
 *
 */
public class RejuvenatedFlesh extends ConfigurableDamageIndicatedItemFluidContainer {

    private static RejuvenatedFlesh _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new RejuvenatedFlesh(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static RejuvenatedFlesh getInstance() {
        return _instance;
    }

    private RejuvenatedFlesh(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, RejuvenatedFleshConfig.containerSize, Blood.getInstance());
        this.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.rare;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.eat;
    }

    protected boolean canEat(ItemStack itemStack) {
        FluidStack fluidStack = getFluid(itemStack);
        return fluidStack != null && fluidStack.amount >= RejuvenatedFleshConfig.biteUsage;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(canEat(itemStack) && player.canEat(false)) {
            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        return itemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        drain(itemStack, RejuvenatedFleshConfig.biteUsage, true);
        player.getFoodStats().addStats(3, 0.5F);
        world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }

}
