package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsBloodChest {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodChestRepair(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_CHEST.get());
        BlockEntityBloodChest chest = helper.getBlockEntity(POS);
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        sword.set(DataComponents.DAMAGE, 10);
        chest.getInventory().setItem(0, sword);
        chest.getInventory().setItem(BlockEntityBloodChest.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above());
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(chest.getInventory().getItem(0).getDamageValue(), 0, "Sword has not been repaired");
        });
    }

}
