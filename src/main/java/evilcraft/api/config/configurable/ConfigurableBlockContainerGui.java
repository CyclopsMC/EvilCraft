package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.Helpers.IDType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;

/**
 * Block with a tile entity with a GUI that can hold ExtendedConfigs.
 * The container and GUI must be set inside the constructor of the extension.
 * @author rubensworks
 *
 */
public class ConfigurableBlockContainerGui extends ConfigurableBlockContainer {
    
    private int guiID;

    private Class<? extends Container> container;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiContainer> gui;

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The class of the tile entity this block holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainerGui(ExtendedConfig eConfig,
            Material material, Class<? extends EvilCraftTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
        this.guiID = Helpers.getNewId(IDType.GUI);
        this.hasGui = true;
    }
    
    /**
     * Get the unique ID for the GUI this block has.
     * @return the GUI ID.
     */
    public int getGuiID() {
        return this.guiID;
    }
    
    @SideOnly(Side.CLIENT)
    protected void setGUI(Class<? extends GuiContainer> gui) {
        this.gui = gui;
    }
    
    protected void setContainer(Class<? extends Container> container) {
        this.container = container;
    }
    
    /**
     * Get the container for this block.
     * @return The container class.
     */
    public Class<? extends Container> getContainer() {
        return container;
    }
    
    /**
     * Get the GUI for this block.
     * @return The GUI class.
     */
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
