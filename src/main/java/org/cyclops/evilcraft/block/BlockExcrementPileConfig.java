package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityBloodChest;
import org.cyclops.evilcraft.item.ItemExcrementPile;

/**
 * Config for the {@link BlockExcrementPile}.
 * @author rubensworks
 *
 */
public class BlockExcrementPileConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "If Excrement can also poison any mob next to players.", isCommandable = true)
    public static boolean poisonEntities = false;

    @ConfigurableProperty(category = "block", comment = "The relative effectiveness when compared to bonemeal if shift right click using.", isCommandable = true)
    public static int effectiveness = 3;

    public BlockExcrementPileConfig() {
        super(
                EvilCraft._instance,
            "excrement_pile",
                eConfig -> new BlockExcrementPile(Block.Properties.create(Material.CLAY)
                        .tickRandomly()),
                (eConfig, block) -> new ItemExcrementPile(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> RenderItemStackTileEntityBloodChest::new))
        );
    }
    
}
