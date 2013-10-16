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
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigurableItemFood;
import evilcraft.api.config.ExtendedConfig;

public class WerewolfFlesh extends ConfigurableItemFood {
    
    private static WerewolfFlesh _instance = null;
    
    private static final int POISON_DURATION = 10;
    private static final int POWER_DURATION = 60;
    
    private boolean power = false;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new WerewolfFlesh(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static WerewolfFlesh getInstance() {
        return _instance;
    }

    private WerewolfFlesh(ExtendedConfig eConfig) {
        super(eConfig, -5, 0, false);
    }
    
    private boolean isPower() {
        return power;
    }
    
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack){
        return power ? EnumRarity.rare : EnumRarity.common;
    }
    
    @Override
    public boolean hasEffect(ItemStack par1ItemStack){
        return power;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3EntityPlayer, int par4, boolean par5) {
        power = !Helpers.isDay(par2World);
    }
    
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        --par1ItemStack.stackSize;
        if(isPower()) {
            par3EntityPlayer.getFoodStats().addStats(this);
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.damageBoost.id, POWER_DURATION * 20, 2));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, POWER_DURATION * 20, 2));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.jump.id, POWER_DURATION * 20, 2));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, POWER_DURATION * 20, 2));
            par2World.playSoundAtEntity(par3EntityPlayer, "mob.wolf.howl", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        } else {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
            par2World.playSoundAtEntity(par3EntityPlayer, "mob.wolf.hurt", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        }
        this.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return par1ItemStack;
    }

}
