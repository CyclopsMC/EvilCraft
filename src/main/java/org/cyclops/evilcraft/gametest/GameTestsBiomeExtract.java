package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.component.DataComponentBiomeConfig;

public class GameTestsBiomeExtract {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 150)
    public void testBiomeExtractThrow(GameTestHelper helper) {
        // Let player throw biome extract
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(90F);
        ItemStack biomeExtract = new ItemStack(RegistryEntries.ITEM_BIOME_EXTRACT);
        biomeExtract.set(RegistryEntries.COMPONENT_BIOME, new DataComponentBiomeConfig.BiomeHolder(Identifier.fromNamespaceAndPath("minecraft", "beach"), helper.getLevel().holderLookup(Registries.BIOME)));
        player.setItemInHand(InteractionHand.MAIN_HAND, biomeExtract);
        player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(helper.getLevel().getBiome(helper.absolutePos(POS)).getRegisteredName(), "minecraft:beach", Component.literal("Biome was not changed"));
        });
    }

}
