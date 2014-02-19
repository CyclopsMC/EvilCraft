package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.entities.monster.Netherfish;
import evilcraft.entities.monster.NetherfishConfig;

/**
 * A block that spawns a {@link Netherfish} when the block breaks.
 * @author rubensworks
 *
 */
public class NetherfishSpawn extends ConfigurableBlockWithInnerBlocks {
    
    private static NetherfishSpawn _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new NetherfishSpawn(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NetherfishSpawn getInstance() {
        return _instance;
    }

    private NetherfishSpawn(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay);
        this.setHardness(0.0F);
    }
    
    @Override
    protected Block[] makeInnerBlockList() {
        return new Block[]{
                Blocks.netherrack,
                Blocks.nether_brick, 
                Blocks.soul_sand
                };
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {}
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (!world.isRemote && NetherfishConfig._instance.isEnabled()) {
            Netherfish netherfish = new Netherfish(world);
            netherfish.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
            world.spawnEntityInWorld(netherfish);
            netherfish.spawnExplosionParticle();
        }

        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    /**
     * Does the given metadata correspond to an inner block?
     * @param meta Metadata for the (inner) block
     * @return if the metadata corresponds to an inner block.
     */
    public boolean getPosingIdByMetadata(int meta) {
        return getBlockFromMetadata(meta) != null;
    }

}
