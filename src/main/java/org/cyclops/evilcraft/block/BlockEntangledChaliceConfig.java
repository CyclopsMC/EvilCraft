package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

/**
 * Config for the {@link BlockEntangledChalice}.
 * @author rubensworks
 *
 */
public class BlockEntangledChaliceConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    public BlockEntangledChaliceConfig() {
        super(
                EvilCraft._instance,
                "entangled_chalice",
                eConfig -> new BlockEntangledChalice(Block.Properties.create(Material.IRON)
                        .hardnessAndResistance(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemEntangledChalice(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> RenderItemStackTileEntityEntangledChalice::new))
        );
    }
}
