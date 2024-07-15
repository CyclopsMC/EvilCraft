package org.cyclops.evilcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;

import java.util.List;

public class ItemBlockBoxOfEternalClosure extends ItemBlockNBT {

    public ItemBlockBoxOfEternalClosure(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        if (tile instanceof BlockEntityBoxOfEternalClosure) {
            ((BlockEntityBoxOfEternalClosure) tile).setSpiritTag(itemStack.getOrDefault(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, new CompoundTag()));
            ((BlockEntityBoxOfEternalClosure) tile).setPlayerId(itemStack.getOrDefault(RegistryEntries.COMPONENT_BOX_PLAYER_ID, ""));
            ((BlockEntityBoxOfEternalClosure) tile).setPlayerName(itemStack.getOrDefault(RegistryEntries.COMPONENT_BOX_PLAYER_NAME, ""));
            ((BlockEntityBoxOfEternalClosure) tile).initializeState();
        }
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(getInfo(stack));
        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    public Component getInfo(ItemStack itemStack) {
        Component content = Component.translatable("general." + Reference.MOD_ID + ".info.empty")
                .withStyle(ChatFormatting.ITALIC);
        if(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.hasPlayer(itemStack)) {
            content = Component.literal(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.getPlayerName(itemStack));
        } else {
            EntityType<?> spiritType = org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack);
            if (spiritType != null) {
                content = spiritType.getDescription();
            }
        }
        return Component.translatable(getDescriptionId() + ".info.content")
                .withStyle(ChatFormatting.LIGHT_PURPLE)
                .append(content);
    }
}
