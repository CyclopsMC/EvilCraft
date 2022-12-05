package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;

import java.util.List;
import java.util.function.Function;

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
        return super.getDescriptionId(itemStack) + (getBiomeClient(itemStack) == null ? ".empty" : "");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Registry<Biome> registry = world.registryAccess().registry(ForgeRegistries.Keys.BIOMES).get();
        if(!world.isClientSide() && getBiomeClient(itemStack) != null &&
                !ItemBiomeExtractConfig.isUsageBlacklisted(registry, getBiomeServer(registry, itemStack))) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(), new SoundEvent(new ResourceLocation("random.bow")), SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
            EntityBiomeExtract entity = new EntityBiomeExtract(world, player, itemStack.copy());
            // MCP: shoot
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
            itemStack.shrink(1);
        }

        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        Biome biome = getBiomeClient(itemStack);
        if(biome != null) {
            // Biome name generation based on CreateBuffetWorldScreen
            ResourceLocation key = ForgeRegistries.BIOMES.getKey(biome);
            list.add(Component.translatable(getDescriptionId() + ".info.content",
                    Component.translatable("biome." + key.getNamespace() + "." + key.getPath())));
        }
    }

    public Iterable<Biome> getBiomes() {
        return ForgeRegistries.BIOMES.getValues();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void fillItemCategory(CreativeModeTab creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        super.fillItemCategory(creativeTabs, list);
        if(ItemBiomeExtractConfig.creativeTabVariants) {
            for (Biome biome : getBiomes()) {
                list.add(createItemStack(ForgeRegistries.BIOMES::getKey, biome, 1));
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
        return getBiomeClient(itemStack) == null;
    }

    /**
     * Returns the biome type for the given ItemStack
     *
     * @param itemStack ItemStack which holds a BiomeExtract
     * @return biome type of the given ItemStack
     */
    public static Biome getBiomeClient(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            String biomeName = itemStack.getTag().getString(NBT_BIOMEKEY);
            if(ForgeRegistries.BIOMES.containsKey(new ResourceLocation(biomeName))) {
                return ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
            }
        }
        return null;
    }

    public static Biome getBiomeServer(Registry<Biome> registry, ItemStack itemStack) {
        if(itemStack.hasTag()) {
            String biomeName = itemStack.getTag().getString(NBT_BIOMEKEY);
            if(registry.containsKey(new ResourceLocation(biomeName))) {
                return registry.get(new ResourceLocation(biomeName));
            }
        }
        return null;
    }

    /**
     * Create a stack of a certain type of biome.
     *
     *
     * @param biomeKeyFunction A function to retrieve the biome's key.
     * @param biome The type of biome to make.
     * @param amount The amount per stack.
     * @return The stack.
     */
    public ItemStack createItemStack(Function<Biome, ResourceLocation> biomeKeyFunction, Biome biome, int amount) {
        ItemStack itemStack = new ItemStack(this, amount);
        if(biome != null) {
            CompoundTag tag = new CompoundTag();
            tag.putString(NBT_BIOMEKEY, biomeKeyFunction.apply(biome).toString());
            itemStack.setTag(tag);
        }
        return itemStack;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        Biome biome = getBiomeClient(itemStack);
        if(biome == null) {
            return Rarity.COMMON;
        } else {
            return biome.getMobSettings().getCreatureProbability() <= 0.05F
                    ? Rarity.EPIC
                    : (biome.getMobSettings().getCreatureProbability() <= 0.1F ? Rarity.RARE : Rarity.UNCOMMON);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            if(renderPass == 0) {
                Biome biome = RegistryEntries.ITEM_BIOME_EXTRACT.getBiomeClient(itemStack);
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
