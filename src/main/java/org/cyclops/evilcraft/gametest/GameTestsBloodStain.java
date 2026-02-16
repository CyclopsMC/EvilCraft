package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsBloodStain {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodStainPlacement(GameTestHelper helper) {
        // Place a block first (blood stain needs to rest on something)
        helper.setBlock(POS, Blocks.STONE);

        // Place blood stain above
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_BLOOD_STAIN.get());

        helper.succeedWhen(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_BLOOD_STAIN.get(), POS.above());
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodStainCanSurvive(GameTestHelper helper) {
        // Place a block first
        helper.setBlock(POS, Blocks.STONE);

        // Place blood stain above
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_BLOOD_STAIN.get());

        helper.succeedWhen(() -> {
            // Verify the blood stain survived placement on stone
            helper.assertBlockPresent(RegistryEntries.BLOCK_BLOOD_STAIN.get(), POS.above());
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodStainReplaceable(GameTestHelper helper) {
        // Place a block first
        helper.setBlock(POS, Blocks.STONE);

        // Place blood stain above
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_BLOOD_STAIN.get());

        // Blood stain should be replaceable - place stone over it
        helper.setBlock(POS.above(), Blocks.STONE);

        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.STONE, POS.above());
        });
    }
}
