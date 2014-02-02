package evilcraft.api.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;

public class ConfigurableBlockContainerGui extends ConfigurableBlockContainer {
    
    private int guiID;
    @SideOnly(Side.SERVER)
    private Class<? extends Container> container;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiContainer> gui;

    public ConfigurableBlockContainerGui(ExtendedConfig eConfig,
            Material material, Class<? extends EvilCraftTileEntity> tileEntity, int guiID) {
        super(eConfig, material, tileEntity);
        this.guiID = guiID;
        this.hasGui = true;
    }
    
    public int getGuiID() {
        return this.guiID;
    }
    
    @SideOnly(Side.CLIENT)
    protected void setGUI(Class<? extends GuiContainer> gui) {
        this.gui = gui;
    }
    
    @SideOnly(Side.SERVER)
    protected void setContainer(Class<? extends Container> container) {
        this.container = container;
    }
    
    @SideOnly(Side.SERVER)
    public Class<? extends Container> getContainer() {
        return container;
    }
    
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getGUI() {
        return gui;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);

        // Drop through if the player is sneaking
        if (entityplayer.isSneaking()) {
            return false;
        }

        if (!world.isRemote && hasGui()) {
            entityplayer.openGui(EvilCraft._instance, guiID, world, x, y, z);
        }

        return true;
    }

}
