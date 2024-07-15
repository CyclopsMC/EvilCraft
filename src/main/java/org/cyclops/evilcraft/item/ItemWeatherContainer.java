package org.cyclops.evilcraft.item;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

import java.util.List;
import java.util.Locale;

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
        return itemStack.getOrDefault(RegistryEntries.COMPONENT_WEATHER_CONTAINER_TYPE, WeatherContainerType.EMPTY);
    }

    public static void setWeatherType(ItemStack itemStack, WeatherContainerType type) {
        itemStack.set(RegistryEntries.COMPONENT_WEATHER_CONTAINER_TYPE, type);
        itemStack.set(DataComponents.RARITY, type.rarity);
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
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
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
        EMPTY(null, "empty", ChatFormatting.GRAY, Helpers.RGBAToInt(125, 125, 125, 255), Rarity.COMMON),
        /**
         * Clear weather container.
         */
        CLEAR(WeatherType.CLEAR, "clear", ChatFormatting.AQUA, Helpers.RGBAToInt(30, 150, 230, 255), Rarity.UNCOMMON),
        /**
         * Rain weather container.
         */
        RAIN(WeatherType.RAIN, "rain", ChatFormatting.DARK_BLUE, Helpers.RGBAToInt(0, 0, 255, 255), Rarity.UNCOMMON),
        /**
         * Lightning weather container.
         */
        LIGHTNING(WeatherType.LIGHTNING, "lightning", ChatFormatting.GOLD, Helpers.RGBToInt(255, 215, 0), Rarity.RARE);

        public static final Codec<WeatherContainerType> CODEC = Codec.STRING.xmap(
                name -> {
                    WeatherContainerType weatherType = WeatherContainerType.valueOf(name);
                    if(weatherType == null) {
                        throw new JsonSyntaxException(String.format("Could not found the weather '%s'", name));
                    }
                    return weatherType;
                }, (weatherType) -> weatherType.toString().toUpperCase(Locale.ENGLISH));
        public static final StreamCodec<ByteBuf, WeatherContainerType> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
                .map(
                        name -> {
                            WeatherContainerType weatherType = WeatherContainerType.valueOf(name);
                            if(weatherType == null) {
                                throw new JsonSyntaxException(String.format("Could not found the weather '%s'", name));
                            }
                            return weatherType;
                        }, (weatherType) -> weatherType.toString().toUpperCase(Locale.ENGLISH)
                );

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

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass > 0 ? -1 : getWeatherType(itemStack).damageRenderColor;
        }
    }
}
