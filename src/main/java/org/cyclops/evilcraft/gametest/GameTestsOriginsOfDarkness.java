package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Game tests for the Origins of Darkness book production mechanic.
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsOriginsOfDarkness {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testOriginsOfDarknessPig(GameTestHelper helper) {
        // Enclose the pig in oak fences
        helper.setBlock(POS.offset(-1, 0, -1), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset( 0, 0, -1), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset( 1, 0, -1), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset(-1, 0,  0), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset( 1, 0,  0), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset(-1, 0,  1), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset( 0, 0,  1), Blocks.OAK_FENCE);
        helper.setBlock(POS.offset( 1, 0,  1), Blocks.OAK_FENCE);

        // Spawn a pig inside the enclosure
        net.minecraft.world.entity.animal.Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, POS.above());

        // Simulate feeding the pig a darkened apple by firing the player interact event
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND,
                new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("evilcraft:darkened_apple"))));
        NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(player, InteractionHand.MAIN_HAND, pig));

        // Drop a book near the pig shortly before it dies (tick 8), so it is still elevated when the spirit
        // portal spawns at tick ~10 and falls within the portal's book-detection range.
        helper.runAfterDelay(8, () -> helper.spawnItem(Items.BOOK, POS.above().above()));

        // Verify the pig has died and the book was converted into an Origins of Darkness
        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.PIG);
            helper.assertItemEntityNotPresent(Items.BOOK);
            helper.assertItemEntityPresent(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS.get());
        });
    }

}
