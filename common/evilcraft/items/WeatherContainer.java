package evilcraft.items;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
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
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new WeatherContainer(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static WeatherContainer getInstance() {
        return _instance;
    }
    
    private Icon overlay;

    private WeatherContainer(ExtendedConfig eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return EnumAction.bow;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
    
    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int damage)
    {
        return getWeatherContainerType(damage).damageRenderColor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass)
    {
        return renderPass > 0 ? 16777215 : this.getColorFromDamage(itemStack.getItemDamage());
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if(!world.isRemote && getWeatherContainerType(itemStack) != WeatherContainerTypes.EMPTY) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(new EntityWeatherContainer(world, player, itemStack));
            
            itemStack.stackSize--;
        }

        return itemStack;
    }
    
    public void onUse(World world, ItemStack itemStack) {
        getWeatherContainerType(itemStack).onUse(world, itemStack);
    }
    
    public void onFill(World world, ItemStack itemStack) {
        getWeatherContainerType(itemStack).onFill(world, itemStack);
    }
    
    /**
     * Display the contained weather
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        //String s1 = StatCollector.translateToLocal("potion.empty").trim();
        WeatherContainerTypes type = getWeatherContainerType(itemStack);
        
        list.add(type.damageColor + type.description);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(getIconString());
        overlay = iconRegister.registerIcon(getIconString() + "_overlay");
    }
    
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 0 ? this.overlay : super.getIconFromDamageForRenderPass(par1, par2);
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs creativeTabs, List list)
    {
        for(int i = 0; i < WeatherContainerTypes.values().length; i++) {
            list.add(new ItemStack(itemId, 1, i));
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
        EMPTY(null, "Empty", EnumChatFormatting.GRAY, Helpers.RGBToInt(125, 125, 125)),
        CLEAR(WeatherType.CLEAR, "My only sunshine", EnumChatFormatting.AQUA, Helpers.RGBToInt(30, 150, 230)),  
        RAIN(WeatherType.RAIN, "When the rain begins to fall", EnumChatFormatting.DARK_BLUE, Helpers.RGBToInt(0, 0, 255)),
        LIGHTNING(WeatherType.LIGHTNING, "Thunderstruck", EnumChatFormatting.GOLD, Helpers.RGBToInt(255, 215, 0));
        
        private final WeatherType type;
        
        private final String description;
        private final EnumChatFormatting damageColor;
        private final int damageRenderColor;
        
        private WeatherContainerTypes(WeatherType type, String description, EnumChatFormatting damageColor, int damageRenderColor) {
            this.type = type;
            
            this.description = description;
            this.damageColor = damageColor;
            this.damageRenderColor = damageRenderColor;
        }
        
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
        
        public void onUse(World world, ItemStack containerStack) {
            if (type != null)
                type.activate(world);
            
            containerStack.setItemDamage(EMPTY.ordinal());
        }
    }
}
