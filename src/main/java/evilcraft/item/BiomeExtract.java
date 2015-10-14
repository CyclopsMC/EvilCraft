package evilcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.entity.item.EntityBiomeExtract;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

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
public class BiomeExtract extends ConfigurableItem {

    private static final String NBT_BIOMEID = "biomeId";

    private static BiomeExtract _instance = null;

    private IIcon overlay;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BiomeExtract(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BiomeExtract getInstance() {
        return _instance;
    }

    private BiomeExtract(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack) + (itemStack.getItemDamage() == 0 ? ".empty" : "");
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
        return metadata == 0 ? 1 : 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        if(renderPass == 0 && itemStack.getItemDamage() > 0) {
            BiomeGenBase biome = getBiome(itemStack);
            if(biome != null) {
                return biome.color;
            } else {
                return RenderHelpers.RGBToInt(125, 125, 125);
            }
        }
        return 16777215;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote && getBiome(itemStack) != null) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(new EntityBiomeExtract(world, player, itemStack.copy()));
            itemStack.stackSize--;
        }

        return itemStack;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        BiomeGenBase biome = getBiome(itemStack);
        if(biome != null) {
            list.add(L10NHelpers.localize(getUnlocalizedName() + ".info.content", biome.biomeName));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
        overlay = iconRegister.registerIcon(getIconString() + "_overlay");
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int meta, int renderpass) {
        return renderpass == 0 && meta > 0 ? this.overlay : super.getIconFromDamageForRenderPass(meta, renderpass);
    }

    public BiomeGenBase[] getBiomes() {
        return BiomeGenBase.getBiomeGenArray();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubItems(item, creativeTabs, list);
        if(BiomeExtractConfig.creativeTabVariants) {
            BiomeGenBase[] biomes = getBiomes();
            for (int i = 0; i < biomes.length; i++) {
                if (biomes[i] != null) {
                    list.add(createItemStack(biomes[i], 1));
                }
            }
        }
    }

    /**
     * Checks wether or not a BiomeExtract is empty (it does not contain any biome)
     * given its item damage
     *
     * @param itemStack Item stack
     * @return true if the BiomeExtract is empty, false other
     */
    public boolean isEmpty(ItemStack itemStack) {
        return getBiome(itemStack) == null;
    }

    /**
     * Returns the biome type for the given ItemStack
     *
     * @param itemStack ItemStack which holds a BiomeExtract
     * @return biome type of the given ItemStack
     */
    public BiomeGenBase getBiome(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            int biomeId = itemStack.getTagCompound().getInteger(NBT_BIOMEID);
            BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
            if(biomeId < biomes.length) {
                return biomes[biomeId];
            }
        }
        return null;
    }

    /**
     * Create a stack of a certain type of biome.
     *
     * @param biome The type ofbopme to make.
     * @param amount The amount per stack.
     * @return The stack.
     */
    public ItemStack createItemStack(BiomeGenBase biome, int amount) {
        ItemStack itemStack = new ItemStack(getInstance(), amount, biome == null ? 0 : 1);
        if(biome != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(NBT_BIOMEID, biome.biomeID);
            itemStack.setTagCompound(tag);
        }
        return itemStack;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        BiomeGenBase biome = getBiome(itemStack);
        if(biome == null) {
            return EnumRarity.common;
        } else {
            return biome.getSpawningChance() <= 0.05F ? EnumRarity.epic : (biome.getSpawningChance() <= 0.1F ? EnumRarity.rare : EnumRarity.uncommon);
        }
    }
}
