package evilcraft.api.config.configurable;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.worldgen.WorldGeneratorUndeadTree;

/**
 * Block that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableBlockSapling extends BlockSapling implements Configurable{

    protected ExtendedConfig eConfig = null;

    public static ElementType TYPE = ElementType.BLOCK;

    private WorldGeneratorUndeadTree treeGenerator;

    public ConfigurableBlockSapling(ExtendedConfig eConfig, Material material) {
        super(eConfig.ID);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        treeGenerator = new WorldGeneratorUndeadTree(true, this);
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }

    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }

    public boolean isEntity() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }

    @Override
    public Icon getIcon(int par1, int par2) {
        return this.blockIcon;
    }

    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(id, 1, 0));
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
        return par1World.getBlockId(par2, par3, par4) == this.blockID && (par1World.getBlockMetadata(par2, par3, par4)) == par5;
    }

    @Override
    public void growTree(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) {
            return;
        }

        world.setBlockToAir(x, y, z);

        if(!treeGenerator.growTree(world, rand, x, y, z)) {
            world.setBlock(x, y, z, blockID, 0, 4);
        }
    }

}
