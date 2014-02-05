package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodChest;
import evilcraft.gui.container.ContainerBloodChest;

public class BloodChest extends ConfigurableBlockContainerGuiTankInfo {
    
    private static BloodChest _instance = null;
    
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodChest(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodChest getInstance() {
        return _instance;
    }

    private BloodChest(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.wood, TileBloodChest.class, Reference.GUI_BLOOD_CHEST);
        
        this.setHardness(2.5F);
        this.setStepSound(soundWoodFootstep);
        this.setRotatable(true);
        setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        
        if (Helpers.isClientSide())
            setGUI(GuiBloodChest.class);
        
        setContainer(ContainerBloodChest.class);
    }
    
    @Override
    public int getRenderType() {
        return 22;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return BloodChestConfig._instance.ID;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getBlockTileEntity(x, y, z);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(Helpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public String getTankNBTName() {
        return TileBloodChest.TANKNAME;
    }

    @Override
    public int getTankCapacity() {
        return TileBloodChest.LIQUID_PER_SLOT;
    }

}
