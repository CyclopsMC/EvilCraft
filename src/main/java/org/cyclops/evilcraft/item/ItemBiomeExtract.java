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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;

import java.util.List;

import net.minecraft.item.Item.Properties;

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
public class ItemBiomeExtract extends Item {

    private static final String NBT_BIOMEKEY = "biomeKey";

    public ItemBiomeExtract(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return super.getDescriptionId(itemStack) + (getBiome(itemStack) == null ? ".empty" : "");
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide() && getBiome(itemStack) != null && !ItemBiomeExtractConfig.isUsageBlacklisted(getBiome(itemStack))) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(), new SoundEvent(new ResourceLocation("random.bow")), SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            EntityBiomeExtract entity = new EntityBiomeExtract(world, player, itemStack.copy());
            // MCP: shoot
            entity.shootFromRotation(player, player.xRot, player.yRot, -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
            itemStack.shrink(1);
        }

        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        Biome biome = getBiome(itemStack);
        if(biome != null) {
            // Biome name generation based on CreateBuffetWorldScreen
            list.add(new TranslationTextComponent(getDescriptionId() + ".info.content",
                    new TranslationTextComponent("biome." + biome.getRegistryName().getNamespace() + "." + biome.getRegistryName().getPath())));
        }
    }

    public Iterable<Biome> getBiomes() {
        return ForgeRegistries.BIOMES.getValues();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void fillItemCategory(ItemGroup creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        super.fillItemCategory(creativeTabs, list);
        if(ItemBiomeExtractConfig.creativeTabVariants) {
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
    public static Biome getBiome(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            String biomeName = itemStack.getTag().getString(NBT_BIOMEKEY);
            if(ForgeRegistries.BIOMES.containsKey(new ResourceLocation(biomeName))) {
                return ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
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
        ItemStack itemStack = new ItemStack(this, amount);
        if(biome != null) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString(NBT_BIOMEKEY, biome.getRegistryName().toString());
            itemStack.setTag(tag);
        }
        return itemStack;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        Biome biome = getBiome(itemStack);
        if(biome == null) {
            return Rarity.COMMON;
        } else {
            return biome.getMobSettings().getCreatureProbability() <= 0.05F
                    ? Rarity.EPIC
                    : (biome.getMobSettings().getCreatureProbability() <= 0.1F ? Rarity.RARE : Rarity.UNCOMMON);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            if(renderPass == 0) {
                Biome biome = RegistryEntries.ITEM_BIOME_EXTRACT.getBiome(itemStack);
                if(biome != null) {
                    return biome.getFoliageColor();
                } else {
                    return Helpers.RGBToInt(125, 125, 125);
                }
            }
            return 16777215;
        }
    }
}
