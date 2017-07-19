package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;

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
public class BiomeExtract extends ConfigurableItem {

    private static final String NBT_BIOMEKEY = "biomeKey";

    private static BiomeExtract _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BiomeExtract getInstance() {
        return _instance;
    }

    public BiomeExtract(ExtendedConfig<ItemConfig> eConfig) {
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
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!world.isRemote && getBiome(itemStack) != null && !BiomeExtractConfig._instance.isUsageBlacklisted(getBiome(itemStack))) {
            world.playSound(player, player.posX, player.posY, player.posZ, new SoundEvent(new ResourceLocation("random.bow")), SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            EntityBiomeExtract entity = new EntityBiomeExtract(world, player, itemStack.copy());
            // Last three params: pitch offset, velocity, inaccuracy
            entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntity(entity);
            itemStack.shrink(1);
        }

        return MinecraftHelpers.successAction(itemStack);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        Biome biome = getBiome(itemStack);
        if(biome != null) {
            list.add(L10NHelpers.localize(getUnlocalizedName() + ".info.content", biome.getBiomeName()));
        }
    }

    public Iterable<Biome> getBiomes() {
        return Biome.REGISTRY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        super.getSubItems(creativeTabs, list);
        if(BiomeExtractConfig.creativeTabVariants) {
            for (Biome biome : getBiomes()) {
                list.add(createItemStack(biome, 1));
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
    public Biome getBiome(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            String biomeName = itemStack.getTagCompound().getString(NBT_BIOMEKEY);
            if(Biome.REGISTRY.containsKey(new ResourceLocation(biomeName))) {
                return Biome.REGISTRY.getObject(new ResourceLocation(biomeName));
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
    public ItemStack createItemStack(Biome biome, int amount) {
        ItemStack itemStack = new ItemStack(getInstance(), amount, biome == null ? 0 : 1);
        if(biome != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString(NBT_BIOMEKEY, Biome.REGISTRY.getNameForObject(biome).toString());
            itemStack.setTagCompound(tag);
        }
        return itemStack;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        Biome biome = getBiome(itemStack);
        if(biome == null) {
            return EnumRarity.COMMON;
        } else {
            return biome.getSpawningChance() <= 0.05F ? EnumRarity.EPIC : (biome.getSpawningChance() <= 0.1F ? EnumRarity.RARE : EnumRarity.UNCOMMON);
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
            if(renderPass == 0 && itemStack.getItemDamage() > 0) {
                Biome biome = BiomeExtract.getInstance().getBiome(itemStack);
                if(biome != null) {
                    return biome.getFoliageColorAtPos(new BlockPos(0, 0, 0));
                } else {
                    return Helpers.RGBToInt(125, 125, 125);
                }
            }
            return 16777215;
        }
    }
}
