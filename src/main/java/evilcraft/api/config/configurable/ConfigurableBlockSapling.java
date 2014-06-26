package evilcraft.api.config.configurable;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.worldgen.WorldGeneratorUndeadTree;

/**
 * Block extending from a sapling that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockSapling extends BlockSapling implements Configurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;

    private WorldGeneratorUndeadTree treeGenerator;

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockSapling(ExtendedConfig eConfig, Material material) {
        this.setConfig(eConfig);
        this.setBlockName(this.getUniqueName());
        treeGenerator = new WorldGeneratorUndeadTree(true, this);
    }

    @Override
    public void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }

    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return this.blockIcon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    //isSameSapling
    @Override
    public boolean func_149880_a(World world, int x, int y, int z, int meta) {
        return world.getBlock(x, y, z) == this && (world.getBlockMetadata(x, y, z)) == meta;
    }

    //growTree
    @Override
    public void func_149878_d(World world, int x, int y, int z, Random random) {
        if (world.isRemote) {
            return;
        }

        world.setBlockToAir(x, y, z);

        if(!treeGenerator.growTree(world, random, x, y, z)) {
            world.setBlock(x, y, z, this, 0, 4);
        }
    }

}
