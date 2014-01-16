package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGui;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.Gui;

public class BloodInfuser extends ConfigurableBlockContainerGui {
    
    private static BloodInfuser _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodInfuser(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodInfuser getInstance() {
        return _instance;
    }

    private BloodInfuser(ExtendedConfig eConfig) {
        super(eConfig, Material.ground, TileBloodInfuser.class, Gui.BLOOD_INFUSER);
        this.setRotatable(true);
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return BloodInfuserConfig._instance.ID;
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

}
