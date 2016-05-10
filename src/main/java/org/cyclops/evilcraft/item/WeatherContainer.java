package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

import javax.annotation.Nullable;
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
public class WeatherContainer extends ConfigurableItem {
    
    private static WeatherContainer _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WeatherContainer getInstance() {
        return _instance;
    }

    public WeatherContainer(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }
    
    /**
     * Get the overlay color from a damage value.
     * @param damage The damage value.
     * @return The color.
     */
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int damage) {
        return getWeatherContainerType(damage).damageRenderColor;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote && getWeatherContainerType(itemStack) != WeatherContainerTypes.EMPTY) {
            world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            EntityWeatherContainer entity = new EntityWeatherContainer(world, player, itemStack.copy());
            // Last three params: pitch offset, velocity, inaccuracy
            entity.func_184538_a(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntityInWorld(entity);
            
            itemStack.stackSize--;
        }

        return MinecraftHelpers.successAction(itemStack);
    }
    
    /**
     * When the actual usage of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was thrown.
     */
    public void onUse(World world, ItemStack itemStack) {
        getWeatherContainerType(itemStack).onUse(world, itemStack);
    }
    
    /**
     * When the actual filling of the container should be called.
     * @param world The world.
     * @param itemStack The weather container that was filled.
     */
    public void onFill(World world, ItemStack itemStack) {
        getWeatherContainerType(itemStack).onFill(world, itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        WeatherContainerTypes type = getWeatherContainerType(itemStack);
        list.add(type.damageColor + type.description);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < WeatherContainerTypes.values().length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
    /**
     * Checks wether or not a WeatherContainer is empty (it does not contain any weather)
     * given its item damage
     * 
     * @param itemDamage item damage of the WeatherContainer
     * @return true if the WeatherContainer is empty, false other
     */
    public static boolean isEmpty(int itemDamage) {
        return itemDamage == WeatherContainerTypes.EMPTY.ordinal();
    }
    
    /**
     * Returns the weather container type for a weather container with the given
     * item damage
     * 
     * @param damage Item damage of a weather container
     * @return the weather container type for a weather container with the associated item damage
     */
    public static WeatherContainerTypes getWeatherContainerType(int damage) {
        if (damage > WeatherContainerTypes.values().length)
            return WeatherContainerTypes.EMPTY;
        
        return WeatherContainerTypes.values()[damage];
    }
    
    /**
     * Returns the weather container type for the weather container in the given
     * ItemStack
     * 
     * @param stack ItemStack which holds a WeatherContainer
     * @return weather container type of the weather container in the given ItemStack
     */
    public static WeatherContainerTypes getWeatherContainerType(ItemStack stack) {
        return getWeatherContainerType(stack.getItemDamage());
    }
    
    /**
     * Create a stack of a certain type of weather containers.
     * 
     * @param type The type of weather container to make.
     * @param amount The amount per stack.
     * @return The stack.
     */
    public static ItemStack createItemStack(WeatherContainerTypes type, int amount) {
        return new ItemStack(getInstance(), amount, type.ordinal());
    }
    
    /**
     * Enum containing the data for the different weather container types.
     * New weather containers should be added by adding an entry in this enum
     * 
     * @author immortaleeb
     *
     */
    public enum WeatherContainerTypes {
        /**
         * Empty weather container.
         */
        EMPTY(null, "empty", TextFormatting.GRAY, Helpers.RGBToInt(125, 125, 125)),
        /**
         * Clear weather container.
         */
        CLEAR(WeatherType.CLEAR, "clear", TextFormatting.AQUA, Helpers.RGBToInt(30, 150, 230)),
        /**
         * Rain weather container.
         */
        RAIN(WeatherType.RAIN, "rain", TextFormatting.DARK_BLUE, Helpers.RGBToInt(0, 0, 255)),
        /**
         * Lightning weather container.
         */
        LIGHTNING(WeatherType.LIGHTNING, "lightning", TextFormatting.GOLD, Helpers.RGBToInt(255, 215, 0));
        
        private final WeatherType type;
        
        private final String description;
        private final TextFormatting damageColor;
        private final int damageRenderColor;
        
        private WeatherContainerTypes(WeatherType type, String description, TextFormatting damageColor, int damageRenderColor) {
            this.type = type;
            
            this.description = L10NHelpers.localize("weatherContainer." + Reference.MOD_ID + "." + description);
            this.damageColor = damageColor;
            this.damageRenderColor = damageRenderColor;
        }
        
        /**
         * When the actual filling of the container should be called.
         * @param world The world.
         * @param containerStack The weather container that was filled.
         */
        public void onFill(World world, ItemStack containerStack) {
            WeatherContainerTypes currentWeatherType = EMPTY;
            
            // Find the weather container type who's weather is currently active
            for (WeatherContainerTypes type : values()) {
                if (type.type != null && type.type.isActive(world))
                    currentWeatherType = type;
            }
            
            containerStack.setItemDamage(currentWeatherType.ordinal());
            currentWeatherType.type.deactivate(world);
            
        }
        
        /**
         * When the actual usage of the container should be called.
         * @param world The world.
         * @param containerStack The weather container that was thrown.
         */
        public void onUse(World world, ItemStack containerStack) {
            if (world.isRemote)
                return;
            
            if (type != null)
                type.activate(world);
            
            containerStack.setItemDamage(EMPTY.ordinal());
        }
        
        /**
         * Returns the WeatherContainerType corresponding to the
         * given WeatherType.
         * @param weatherType The WeatherType.
         * @return The corresponding WeatherContainerType, or null in case no match was found.
         */
        public static WeatherContainerTypes getWeatherContainerType(WeatherType weatherType) {
            for (WeatherContainerTypes type : values()) {
                if (type.type == weatherType)
                    return type;
            }
            
            return null;
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return itemStack.getItemDamage() == 0 ? EnumRarity.COMMON :
                (itemStack.getItemDamage() > 2 ? EnumRarity.RARE : EnumRarity.UNCOMMON);
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
            return renderPass > 0 ? 16777215 : WeatherContainer.getInstance().getColorFromDamage(itemStack.getItemDamage());
        }
    }
}
