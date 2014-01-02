package evilcraft.api.config;

import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ConfigurableBlockContainerGui extends ConfigurableBlockContainer {
    
    private int guiID;

    public ConfigurableBlockContainerGui(ExtendedConfig eConfig,
            Material material, Class<? extends TileEntity> tileEntity, int guiID) {
        super(eConfig, material, tileEntity);
        this.guiID = guiID;
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

        if (!world.isRemote) {
            entityplayer.openGui(EvilCraft._instance, guiID, world, x, y, z);
        }

        return true;
    }

}
