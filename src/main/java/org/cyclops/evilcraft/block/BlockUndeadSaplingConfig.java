package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.world.gen.TreeUndead;

/**
 * Config for the Undead Sapling.
 * @author rubensworks
 *
 */
public class BlockUndeadSaplingConfig extends BlockConfig {

    public BlockUndeadSaplingConfig() {
        super(
                EvilCraft._instance,
            "undead_sapling",
                eConfig -> new SaplingBlock(new TreeUndead(), Block.Properties.create(Material.PLANTS)
                        .doesNotBlockMovement()
                        .tickRandomly()
                        .hardnessAndResistance(0)
                        .sound(SoundType.PLANT)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ComposterBlock.CHANCES.put(getItemInstance(), 0.3F);
    }
    
}
