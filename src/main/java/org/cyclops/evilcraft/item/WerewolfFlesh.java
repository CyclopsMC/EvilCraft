package org.cyclops.evilcraft.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItemFood;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Random flesh drop from werewolves, gives some fine boosts at night.
 * @author rubensworks
 *
 */
public class WerewolfFlesh extends ConfigurableItemFood {
    
    private static WerewolfFlesh _instance = null;
    
    private static final int POISON_DURATION = 10;
    private static final int POWER_DURATION = 60;
    private static final int POWER_DURATION_BONUS = POWER_DURATION * 4;
    
    private boolean power = false;
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
    	if(isHumanFlesh(itemStack)) {
    		return "item.items." + Reference.MOD_ID + ".humanFlesh";
    	}
        return "item.items."  + Reference.MOD_ID + "." + eConfig.getNamedId();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WerewolfFlesh getInstance() {
        return _instance;
    }

    public WerewolfFlesh(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, -5, 0, false);
        setHasSubtypes(true);
        setAlwaysEdible();
        this.setMaxStackSize(16);
    }
    
    private boolean isPower() {
        return power;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack){
        return isHumanFlesh(itemStack) ? EnumRarity.RARE : EnumRarity.EPIC;
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return isPower();
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity player, int par4, boolean par5) {
        power = !MinecraftHelpers.isDay(world);
    }
    
    private int getPowerDuration(ItemStack itemStack) {
    	if(isHumanFlesh(itemStack)) {
    		return POWER_DURATION;
    	}
    	return POWER_DURATION_BONUS;
    }
    
    private boolean isHumanFlesh(ItemStack itemStack) {
    	return itemStack.getItemDamage() == 1;
    }
    
    private boolean isOwnCanibal(ItemStack itemStack, EntityPlayer player) {
    	if(itemStack.getTagCompound() != null) {
			GameProfile profile = NBTUtil.readGameProfileFromNBT(itemStack.getTagCompound());
			return player.getGameProfile().equals(profile);
		}
    	return false;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            --itemStack.stackSize;
            if (itemStack.getItemDamage() == 1) {
                player.addStat(Achievements.CANNIBAL, 1);
            }
            if (isOwnCanibal(itemStack, player)) {
                if (!world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.wither,
                            POISON_DURATION * 20, 1));
                    player.addPotionEffect(new PotionEffect(MobEffects.blindness,
                            getPowerDuration(itemStack) * 20, 1));
                }
                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.entity_wolf_hurt, SoundCategory.HOSTILE, 0.5F,
                        world.rand.nextFloat() * 0.1F + 0.9F);
            } else if (isPower()) {
                int foodLevel = this.getHealAmount(itemStack);
                float saturationLevel = this.getSaturationModifier(itemStack);
                player.getFoodStats().addStats(foodLevel, saturationLevel);
                if (!world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.damageBoost,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addPotionEffect(new PotionEffect(MobEffects.moveSpeed,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addPotionEffect(new PotionEffect(MobEffects.jump,
                            getPowerDuration(itemStack) * 20, 2));
                    player.addPotionEffect(new PotionEffect(MobEffects.nightVision,
                            getPowerDuration(itemStack) * 20, 2));
                }
                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.entity_wolf_howl, SoundCategory.HOSTILE, 0.5F,
                        world.rand.nextFloat() * 0.1F + 0.9F);
            } else {
                if (!world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.poison,
                            POISON_DURATION * 20, 1));
                }
                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.entity_wolf_hurt, SoundCategory.HOSTILE, 0.5F,
                        world.rand.nextFloat() * 0.1F + 0.9F);
            }
            this.onFoodEaten(itemStack, world, player);
        }
        return itemStack;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
    	super.addInformation(itemStack, entityPlayer, list, par4);
    	if(isHumanFlesh(itemStack)) {
    		String player = TextFormatting.ITALIC + "None";
    		if(itemStack.getTagCompound() != null) {
    			GameProfile profile = NBTUtil.readGameProfileFromNBT(itemStack.getTagCompound());
    			player = profile.getName();
    		}
    		list.add("Player: " + TextFormatting.WHITE + player);
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
            if(WerewolfFlesh.getInstance().isHumanFlesh(itemStack)) {
                return Helpers.RGBToInt(255, 200, 180);
            }
            return -1;
        }
    }

}
