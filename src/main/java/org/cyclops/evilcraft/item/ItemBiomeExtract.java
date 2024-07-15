package org.cyclops.evilcraft.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.component.DataComponentBiomeConfig;
import org.cyclops.evilcraft.entity.item.EntityBiomeExtract;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

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

    public ItemBiomeExtract(Properties properties) {
        super(properties);
    }

    public static HolderLookup.RegistryLookup<Biome> getBiomeRegistry(HolderLookup.Provider holders) {
        return holders.lookupOrThrow(Registries.BIOME);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        return super.getDescriptionId(itemStack) + (getBiome(itemStack) == null ? ".empty" : "");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide() && getBiome(itemStack) != null &&
                !ItemBiomeExtractConfig.isUsageBlacklisted(getBiome(itemStack))) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
            EntityBiomeExtract entity = new EntityBiomeExtract(world, player, itemStack.copy());
            // MCP: shoot
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(entity);
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
        }

        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, list, flag);
        Holder<Biome> biome = getBiome(itemStack);
        if (biome != null) {
            // Biome name generation based on CreateBuffetWorldScreen
            ResourceLocation key = biome.unwrapKey().get().location();
            list.add(Component.translatable(getDescriptionId() + ".info.content",
                    Component.translatable("biome." + key.getNamespace() + "." + key.getPath())));
        }
    }

    public Stream<Holder.Reference<Biome>> getBiomes(HolderLookup.Provider holders) {
        return getBiomeRegistry(holders).listElements();
    }

    public boolean isEmpty(ItemStack itemStack) {
        return getBiome(itemStack) == null;
    }

    /**
     * Returns the biome type for the given ItemStack
     *
     * @param itemStack ItemStack which holds a BiomeExtract
     * @return biome type of the given ItemStack
     */
    @Nullable
    public static Holder<Biome> getBiome(ItemStack itemStack) {
        DataComponentBiomeConfig.BiomeHolder holder = itemStack.get(RegistryEntries.COMPONENT_BIOME);
        return holder != null ? holder.getBiome() : null;
    }

    /**
     * Create a stack of a certain type of biome.
     * @param biome The type of biome to make.
     * @param amount The amount per stack.
     * @return The stack.
     */
    public ItemStack createItemStack(Holder<Biome> biome, int amount, HolderGetter<Biome> holderGetter) {
        ItemStack itemStack = new ItemStack(this, amount);
        if(biome != null) {
            itemStack.set(RegistryEntries.COMPONENT_BIOME, new DataComponentBiomeConfig.BiomeHolder(biome.unwrapKey().get().location(), holderGetter));
            itemStack.set(DataComponents.RARITY, getRarity(biome));
        }
        return itemStack;
    }

    protected Rarity getRarity(Holder<Biome> biome) {
        return biome.value().getMobSettings().getCreatureProbability() <= 0.05F
                ? Rarity.EPIC
                : (biome.value().getMobSettings().getCreatureProbability() <= 0.1F ? Rarity.RARE : Rarity.UNCOMMON);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            if(renderPass == 0) {
                Holder<Biome> biome = RegistryEntries.ITEM_BIOME_EXTRACT.get().getBiome(itemStack);
                if(biome != null) {
                    Triple<Float, Float, Float> rgb = Helpers.intToRGB(biome.value().getFoliageColor());
                    return Helpers.RGBAToInt((int) (rgb.getLeft() * 255), (int) (rgb.getMiddle() * 255), (int) (rgb.getRight() * 255), 255);
                } else {
                    return Helpers.RGBAToInt(125, 125, 125, 255);
                }
            }
            return -1;
        }
    }
}
