package evilcraft.items;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.api.helpers.L10NHelpers;
import evilcraft.api.helpers.RenderHelpers;
import evilcraft.api.weather.WeatherType;
import evilcraft.entities.item.EntityWeatherContainer;

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
    
    private IIcon overlay;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new WeatherContainer(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WeatherContainer getInstance() {
        return _instance;
    }

    private WeatherContainer(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.bow;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @Override
    public int getRenderPasses(int metadata) {
        return 2;
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
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        return renderPass > 0 ? 16777215 : this.getColorFromDamage(itemStack.getItemDamage());
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote && getWeatherContainerType(itemStack) != WeatherContainerTypes.EMPTY) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(new EntityWeatherContainer(world, player, itemStack.copy()));
            
            itemStack.stackSize--;
        }

        return itemStack;
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
        overlay = iconRegister.registerIcon(getIconString() + "_overlay");
    }
    
    @Override
    public IIcon getIconFromDamageForRenderPass(int meta, int renderpass) {
        return renderpass == 0 ? this.overlay : super.getIconFromDamageForRenderPass(meta, renderpass);
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
        EMPTY(null, "empty", EnumChatFormatting.GRAY, RenderHelpers.RGBToInt(125, 125, 125)),
        /**
         * Clear weather container.
         */
        CLEAR(WeatherType.CLEAR, "clear", EnumChatFormatting.AQUA, RenderHelpers.RGBToInt(30, 150, 230)),  
        /**
         * Rain weather container.
         */
        RAIN(WeatherType.RAIN, "rain", EnumChatFormatting.DARK_BLUE, RenderHelpers.RGBToInt(0, 0, 255)),
        /**
         * Lightning weather container.
         */
        LIGHTNING(WeatherType.LIGHTNING, "lightning", EnumChatFormatting.GOLD, RenderHelpers.RGBToInt(255, 215, 0));
        
        private final WeatherType type;
        
        private final String description;
        private final EnumChatFormatting damageColor;
        private final int damageRenderColor;
        
        private WeatherContainerTypes(WeatherType type, String description, EnumChatFormatting damageColor, int damageRenderColor) {
            this.type = type;
            
            this.description = L10NHelpers.localize("weatherContainer." + description);
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
}
