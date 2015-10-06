package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.client.gui.container.GuiColossalBloodChest;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.Size;
import evilcraft.core.block.CubeDetector.IDetectionListener;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.inventory.container.ContainerColossalBloodChest;
import evilcraft.tileentity.TileColossalBloodChest;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 *
 * @author rubensworks
 */
public class ColossalBloodChest extends ConfigurableBlockContainerGuiTankInfo implements IDetectionListener {

    private static ColossalBloodChest _instance = null;

    /**
     * Initialise the configurable.
     *
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if (_instance == null)
            _instance = new ColossalBloodChest(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     *
     * @return The instance.
     */
    public static ColossalBloodChest getInstance() {
        return _instance;
    }

    private ColossalBloodChest(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileColossalBloodChest.class);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeWood);
        this.setHarvestLevel("axe", 2); // Iron tier
        this.setRotatable(false);

        if (MinecraftHelpers.isClientSide())
            setGUI(GuiColossalBloodChest.class);
        setContainer(ContainerColossalBloodChest.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return meta == 1 ? RenderHelpers.EMPTYICON : super.getIcon(side, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        return !TileColossalBloodChest.canWork(world, new Location(x, y, z)) ||
                super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
    }

    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritFurnace.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }

    private void triggerDetector(World world, int x, int y, int z, boolean valid) {
        TileColossalBloodChest.detector.detect(world, new Location(x, y, z), valid, true);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        triggerDetector(world, x, y, z, true);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        triggerDetector(world, x, y, z, false);
        super.onBlockPreDestroy(world, x, y, z, meta);
    }

    @Override
    public void onDetect(World world, ILocation location, Size size, boolean valid, ILocation originCorner) {
        Block block = LocationHelpers.getBlock(world, location);
        if (block == this) {
            TileColossalBloodChest.detectStructure(world, location, size, valid);
            TileEntity tile = LocationHelpers.getTile(world, location);
            if (tile != null) {
                ((TileColossalBloodChest) tile).setSize(valid ? size : Size.NULL_SIZE);
                ((TileColossalBloodChest) tile).setCenter(originCorner.copy().subtract(new Location(-1, -1, -1)));
            }
        }
    }

}
