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
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigurableItem;
import evilcraft.api.config.ExtendedConfig;

public class WeatherContainer extends ConfigurableItem {
    
    private static WeatherContainer _instance = null;
    
    private Icon[] icons = new Icon[5];
    private Icon overlay;
    private static final String[] DAMAGENAMES = new String[]{
        "Empty",
        /*"Day",
        "Night",
        "Rain",
        "Thunder"*/
        "My only sunshine",
        "Darkness",
        "When the rain begins to fall",
        "Thunderstruck",
    };
    private static final EnumChatFormatting[] DAMAGECOLORS = new EnumChatFormatting[]{
        EnumChatFormatting.GRAY,
        EnumChatFormatting.AQUA,
        EnumChatFormatting.BLUE,
        EnumChatFormatting.DARK_BLUE,
        EnumChatFormatting.GOLD
    };
    private static final Integer[] DAMAGERENDERCOLORS = new Integer[]{
        Helpers.RGBToInt(125, 125, 125),
        Helpers.RGBToInt(30, 150, 230),
        Helpers.RGBToInt(100, 100, 255),
        Helpers.RGBToInt(0, 0, 255),
        Helpers.RGBToInt(255, 215, 0),
    };
    private static final Runnable[] AFTERFILLTO = new Runnable[]{
        new Runnable() {
            @Override
            public void run(World world) {
                // Not possible
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                Helpers.setDay(world, false);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                Helpers.setDay(world, true);
                //world.getWorldInfo().setWorldTime(world.getWorldTime() + Helpers.MINECRAFT_DAY/2);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                world.getWorldInfo().setRaining(false);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                world.getWorldInfo().setThundering(false);
            }
        },
    };
    private static final Runnable[] ONEMPTY = new Runnable[]{
        new Runnable() {
            @Override
            public void run(World world) {
                // Not possible
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                Helpers.setDay(world, true);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                Helpers.setDay(world, false);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                world.getWorldInfo().setRaining(true);
            }
        },
        new Runnable() {
            @Override
            public void run(World world) {
                world.getWorldInfo().setThundering(true);
            }
        },
    };
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new WeatherContainer(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static WeatherContainer getInstance() {
        return _instance;
    }

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
    
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int damage)
    {
        return DAMAGERENDERCOLORS[damage];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int par2)
    {
        return par2 > 0 ? 16777215 : this.getColorFromDamage(itemStack.getItemDamage());
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if(!world.isRemote) {
            // Play evil sounds at the players in that world
            for(Object o : world.playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) o;
                world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                world.playSoundAtEntity(entityPlayer, "mob.ghast.moan", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                world.playSoundAtEntity(entityPlayer, "mob.wither.death", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            }
            if(this.getDamage(itemStack) == 0) {
                int newDamage = 0;
                if(world.isThundering()) {
                    newDamage=4;
                } else if(world.isRaining()) {
                    newDamage=3;
                } else if(!Helpers.isDay(world)) {
                    newDamage=2;
                } else {
                    newDamage=1;
                }
                AFTERFILLTO[newDamage].run(world);
                world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                this.setDamage(itemStack, newDamage);
            } else {
                ONEMPTY[this.getDamage(itemStack)].run(world);
                itemStack.stackSize--;
            }
        }

        return itemStack;
    }
    
    /**
     * Display the contained weather
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        //String s1 = StatCollector.translateToLocal("potion.empty").trim();
        int damage = itemStack.getItemDamage();
        list.add(DAMAGECOLORS[damage] + DAMAGENAMES[damage]);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        Icon icon = iconRegister.registerIcon(getIconString());
        this.icons[0] = icon;
        this.icons[1] = icon;
        this.icons[2] = icon;
        this.icons[3] = icon;
        this.icons[4] = icon;
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
     * Gets an icon index based on an item's damage value
     */
    @Override
    public Icon getIconFromDamage(int par1)
    {
        //EvilCraft.log("dmg:"+par1);
        return this.icons[par1];
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs creativeTabs, List list)
    {
        for(int i = 0; i < DAMAGENAMES.length; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

    interface Runnable {
        public void run(World world);
    }
    
}
