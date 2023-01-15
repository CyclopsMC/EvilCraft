package org.cyclops.evilcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
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
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            try {
                return WeatherContainerType.valueOf(tag.getString("weather"));
            } catch (IllegalArgumentException error) {}
        }
        return WeatherContainerType.EMPTY;
    }

    public static void setWeatherType(ItemStack itemStack, WeatherContainerType type) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putString("weather", type.name());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide() && getWeatherType(itemStack) != WeatherContainerType.EMPTY) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
            EntityWeatherContainer entity = new EntityWeatherContainer(world, player, itemStack.copy());
            // MCP: shoot
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);

            itemStack.shrink(1);
        }

        return MinecraftHelpers.successAction(itemStack);
    }

    /**
     * When the actual usage of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was thrown.
     */
    public void onUse(ServerLevel world, ItemStack itemStack) {
        getWeatherType(itemStack).onUse(world, itemStack);
    }

    /**
     * When the actual filling of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was filled.
     */
    public void onFill(ServerLevel world, ItemStack itemStack) {
        getWeatherType(itemStack).onFill(world, itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        WeatherContainerType type = getWeatherType(itemStack);
        list.add(type.description.withStyle(type.damageColor));
    }

    public void fillItemCategory(NonNullList<ItemStack> items) {
        for (WeatherContainerType type : WeatherContainerType.values()) {
            ItemStack stack = new ItemStack(this);
            setWeatherType(stack, type);
            items.add(stack);
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
        EMPTY(null, "empty", ChatFormatting.GRAY, Helpers.RGBToInt(125, 125, 125), Rarity.COMMON),
        /**
         * Clear weather container.
         */
        CLEAR(WeatherType.CLEAR, "clear", ChatFormatting.AQUA, Helpers.RGBToInt(30, 150, 230), Rarity.UNCOMMON),
        /**
         * Rain weather container.
         */
        RAIN(WeatherType.RAIN, "rain", ChatFormatting.DARK_BLUE, Helpers.RGBToInt(0, 0, 255), Rarity.UNCOMMON),
        /**
         * Lightning weather container.
         */
        LIGHTNING(WeatherType.LIGHTNING, "lightning", ChatFormatting.GOLD, Helpers.RGBToInt(255, 215, 0), Rarity.RARE);

        private final WeatherType type;

        private final MutableComponent description;
        private final ChatFormatting damageColor;
        private final int damageRenderColor;
        private final Rarity rarity;

        private WeatherContainerType(WeatherType type, String description, ChatFormatting damageColor, int damageRenderColor, Rarity rarity) {
            this.type = type;

            this.description = Component.translatable("weather_container." + Reference.MOD_ID + "." + description);
            this.damageColor = damageColor;
            this.damageRenderColor = damageRenderColor;
            this.rarity = rarity;
        }

        /**
         * When the actual filling of the container should be called.
         * @param world The world.
         * @param containerStack The weather container that was filled.
         */
        public void onFill(ServerLevel world, ItemStack containerStack) {
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
        public void onUse(ServerLevel world, ItemStack containerStack) {
            if (world.isClientSide())
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
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass > 0 ? 16777215 : getWeatherType(itemStack).damageRenderColor;
        }
    }
}
