package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
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
    public static final BlockPos POS = BlockPos.ZERO.offset(4, 0, 4);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 300)
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
        net.minecraft.world.entity.animal.Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, POS);

        // Simulate feeding the pig a darkened apple by firing the player interact event.
        // With paling (amplifier 4 = 5 dmg/hit) and 10-tick invulnerability, the pig (10 HP) dies at ~tick 11.
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND,
                new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("evilcraft:darkened_apple"))));
        NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(player, InteractionHand.MAIN_HAND, pig));

        // The spirit portal is placed at a shuffled random position within 1 block of the pig's
        // death position (pig.blockPosition() + (0,1,0) = POS.above().above()).
        // With paling amplifier 4 = 1 dmg/hit and 10-tick invulnerability, the pig (10 HP) dies at ~tick 100.
        // To guarantee the book is within the portal's asymmetric detection AABB [pos-0.5, pos+1.5],
        // we wait until the portal has spawned (tick 200), then scan for it and spawn the book
        // directly at the portal's block position.
        helper.runAfterDelay(200, () -> {
            boolean found = false;
            BlockPos center = POS.above().above();
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos portalPos = center.offset(dx, dy, dz);
                        if (helper.getBlockState(portalPos).is(RegistryEntries.BLOCK_SPIRIT_PORTAL.get())) {
                            found = true;
                            helper.spawnItem(Items.BOOK, portalPos);
                        }
                    }
                }
            }
            if (!found) {
                throw new GameTestAssertException("Spirit portal not found within 2 blocks of pig's death position.");
            }
        });

        // Verify the pig has died and the book was converted into an Origins of Darkness
        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.PIG);
            helper.assertItemEntityNotPresent(Items.BOOK);
            helper.assertItemEntityPresent(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS.get());
        });
    }

}
