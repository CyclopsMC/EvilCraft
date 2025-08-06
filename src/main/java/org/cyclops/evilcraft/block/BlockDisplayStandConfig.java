package org.cyclops.evilcraft.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * Config for the {@link BlockDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockDisplayStandConfig extends BlockConfigCommon<ModBaseNeoForge<?>> {

    public BlockDisplayStandConfig() {
        super(
                EvilCraft._instance,
            "display_stand",
                (eConfig, properties) -> new BlockDisplayStand(properties
                                .requiresCorrectToolForDrops()),
                (eConfig, block) -> new BlockItem(block, eConfig.createDefaultItemProperties()) {
                    @Override
                    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
                        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
                        ItemStack blockType = RegistryEntries.BLOCK_DISPLAY_STAND.get().getDisplayStandType(stack);
                        if (blockType != null) {
                            tooltipAdder.accept(((MutableComponent) blockType.getHoverName())
                                    .withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public @Nullable BlockClientConfig<ModBaseNeoForge<?>> constructBlockClientConfig() {
        return new BlockDisplayStandConfigClient(this);
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockDisplayStand) getInstance()).fillItemCategory(list);
        return list;
    }

}
