package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

import java.util.List;

/**
 * Class for the WeatherContainer item. Each weather container has a specific
 * WeatherContainerType which contains the actual data and functionality that
 * will be used when using this weather container. The different types of 
 * weather containers are identified by their item damage, which equals
 * to the ordinal of the corresponding WeatherContainerType.
 * Any new weather containers should by added by adding an entry in
 * the WeatherContainerType enum.
 * 
 * @author immortaleeb
 *
 */
public class ItemWeatherContainer extends Item {

    public ItemWeatherContainer(Properties properties) {
        super(properties);
    }

    public static WeatherContainerType getWeatherType(ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();
        if (tag != null) {
            try {
                return WeatherContainerType.valueOf(tag.getString("weather"));
            } catch (IllegalArgumentException error) {}
        }
        return WeatherContainerType.EMPTY;
    }

    public static void setWeatherType(ItemStack itemStack, WeatherContainerType type) {
        CompoundNBT tag = itemStack.getOrCreateTag();
        tag.putString("weather", type.name());
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.BOW;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!world.isRemote() && getWeatherType(itemStack) != WeatherContainerType.EMPTY) {
            world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            EntityWeatherContainer entity = new EntityWeatherContainer(world, player, itemStack.copy());
            entity.shoot(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.addEntity(entity);
            
            itemStack.shrink(1);
        }

        return MinecraftHelpers.successAction(itemStack);
    }
    
    /**
     * When the actual usage of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was thrown.
     */
    public void onUse(World world, ItemStack itemStack) {
        getWeatherType(itemStack).onUse(world, itemStack);
    }
    
    /**
     * When the actual filling of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was filled.
     */
    public void onFill(World world, ItemStack itemStack) {
        getWeatherType(itemStack).onFill(world, itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        WeatherContainerType type = getWeatherType(itemStack);
        list.add(type.description.applyTextStyle(type.damageColor));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            for (WeatherContainerType type : WeatherContainerType.values()) {
                ItemStack stack = new ItemStack(this);
                setWeatherType(stack, type);
                items.add(stack);
            }
        }
    }
    
    /**
     * Enum containing the data for the different weather container types.
     * New weather containers should be added by adding an entry in this enum
     * 
     * @author immortaleeb
     *
     */
    public enum WeatherContainerType {
        /**
         * Empty weather container.
         */
        EMPTY(null, "empty", TextFormatting.GRAY, Helpers.RGBToInt(125, 125, 125), Rarity.COMMON),
        /**
         * Clear weather container.
         */
        CLEAR(WeatherType.CLEAR, "clear", TextFormatting.AQUA, Helpers.RGBToInt(30, 150, 230), Rarity.UNCOMMON),
        /**
         * Rain weather container.
         */
        RAIN(WeatherType.RAIN, "rain", TextFormatting.DARK_BLUE, Helpers.RGBToInt(0, 0, 255), Rarity.UNCOMMON),
        /**
         * Lightning weather container.
         */
        LIGHTNING(WeatherType.LIGHTNING, "lightning", TextFormatting.GOLD, Helpers.RGBToInt(255, 215, 0), Rarity.RARE);
        
        private final WeatherType type;
        
        private final ITextComponent description;
        private final TextFormatting damageColor;
        private final int damageRenderColor;
        private final Rarity rarity;
        
        private WeatherContainerType(WeatherType type, String description, TextFormatting damageColor, int damageRenderColor, Rarity rarity) {
            this.type = type;
            
            this.description = new TranslationTextComponent("weather_container." + Reference.MOD_ID + "." + description);
            this.damageColor = damageColor;
            this.damageRenderColor = damageRenderColor;
            this.rarity = rarity;
        }
        
        /**
         * When the actual filling of the container should be called.
         * @param world The world.
         * @param containerStack The weather container that was filled.
         */
        public void onFill(World world, ItemStack containerStack) {
            WeatherContainerType currentWeatherType = EMPTY;
            
            // Find the weather container type who's weather is currently active
            for (WeatherContainerType type : values()) {
                if (type.type != null && type.type.isActive(world))
                    currentWeatherType = type;
            }

            setWeatherType(containerStack, currentWeatherType);
            currentWeatherType.type.deactivate(world);
            
        }
        
        /**
         * When the actual usage of the container should be called.
         * @param world The world.
         * @param containerStack The weather container that was thrown.
         */
        public void onUse(World world, ItemStack containerStack) {
            if (world.isRemote())
                return;
            
            if (type != null)
                type.activate(world);

            setWeatherType(containerStack, EMPTY);
        }
        
        /**
         * Returns the WeatherContainerType corresponding to the
         * given WeatherType.
         * @param weatherType The WeatherType.
         * @return The corresponding WeatherContainerType, or null in case no match was found.
         */
        public static WeatherContainerType getWeatherContainerType(WeatherType weatherType) {
            for (WeatherContainerType type : values()) {
                if (type.type == weatherType)
                    return type;
            }
            
            return null;
        }
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return getWeatherType(itemStack).rarity;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass > 0 ? 16777215 : getWeatherType(itemStack).damageRenderColor;
        }
    }
}
