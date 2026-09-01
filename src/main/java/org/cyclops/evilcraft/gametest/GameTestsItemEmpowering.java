package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;

public class GameTestsItemEmpowering {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    protected EntityItemEmpowerable dropInvertedPotentia(GameTestHelper helper, int count) {
        // Make sure the item has something to rest on
        helper.setBlock(POS.below(), Blocks.STONE);

        // Drop the item like it would be dropped in-world, via the item's custom entity
        Vec3 pos = helper.absolutePos(POS).getBottomCenter();
        ItemEntity original = new ItemEntity(helper.getLevel(), pos.x(), pos.y(), pos.z(),
                new ItemStack(RegistryEntries.ITEM_INVERTED_POTENTIA, count));
        EntityItemEmpowerable entity = new EntityItemEmpowerable(helper.getLevel(), original);
        helper.getLevel().addFreshEntity(entity);
        return entity;
    }

    protected void strikeLightning(GameTestHelper helper) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
        lightning.snapTo(helper.absolutePos(POS).getBottomCenter());
        helper.getLevel().addFreshEntity(lightning);
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEmpowerInvertedPotentiaSingle(GameTestHelper helper) {
        EntityItemEmpowerable entity = dropInvertedPotentia(helper, 1);
        strikeLightning(helper);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(entity.getItem().getItem(), RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED.get(),
                    Component.literal("Empowered item"));
            helper.assertValueEqual(entity.getItem().getCount(), 1, Component.literal("Empowered stack size"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEmpowerInvertedPotentiaStack(GameTestHelper helper) {
        EntityItemEmpowerable entity = dropInvertedPotentia(helper, 16);
        strikeLightning(helper);

        helper.succeedWhen(() -> {
            helper.assertValueEqual(entity.getItem().getItem(), RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED.get(),
                    Component.literal("Empowered item"));
            helper.assertValueEqual(entity.getItem().getCount(), 16, Component.literal("Empowered stack size"));
        });
    }

}
