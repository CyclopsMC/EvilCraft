package evilcraft.items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItemFood;

/**
 * Random flesh drop from werewolves, gives some fine boosts at night.
 * @author rubensworks
 *
 */
public class WerewolfFlesh extends ConfigurableItemFood {
    
    private static WerewolfFlesh _instance = null;
    
    private static final int POISON_DURATION = 10;
    private static final int POWER_DURATION = 60;
    
    private boolean power = false;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new WerewolfFlesh(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WerewolfFlesh getInstance() {
        return _instance;
    }

    private WerewolfFlesh(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, -5, 0, false);
        setAlwaysEdible();
    }
    
    private boolean isPower() {
        return power;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack){
        return power ? EnumRarity.rare : EnumRarity.common;
    }
    
    @Override
    public boolean hasEffect(ItemStack par1ItemStack){
        return power;
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity player, int par4, boolean par5) {
        power = !Helpers.isDay(world);
    }
    
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        --itemStack.stackSize;
        if(isPower()) {
        	int foodLevel = this.func_150905_g(itemStack);// get healAmount
        	float saturationLevel = this.func_150906_h(itemStack);// get saturationModifier
            player.getFoodStats().addStats(foodLevel, saturationLevel);
            player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, POWER_DURATION * 20, 2));
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, POWER_DURATION * 20, 2));
            player.addPotionEffect(new PotionEffect(Potion.jump.id, POWER_DURATION * 20, 2));
            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, POWER_DURATION * 20, 2));
            world.playSoundAtEntity(player, "mob.wolf.howl", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        } else {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
            world.playSoundAtEntity(player, "mob.wolf.hurt", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }
        this.onFoodEaten(itemStack, world, player);
        return itemStack;
    }

}
