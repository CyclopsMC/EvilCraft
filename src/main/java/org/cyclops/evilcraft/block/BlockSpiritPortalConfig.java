package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntitySpiritPortal;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;

/**
 * Config for the {@link BlockSpiritPortal}.
 * @author rubensworks
 *
 */
public class BlockSpiritPortalConfig extends BlockConfig {

	public BlockSpiritPortalConfig() {
		super(
				EvilCraft._instance,
				"spirit_portal",
				eConfig -> new BlockSpiritPortal(Block.Properties.of(Material.METAL)
						.strength(50.0F, 6000000.0F)
						.sound(SoundType.WOOL)
						.lightLevel((state) -> 8)),
				getDefaultItemConstructor(EvilCraft._instance)
		);
	}

}