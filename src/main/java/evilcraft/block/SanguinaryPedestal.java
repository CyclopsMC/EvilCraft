package evilcraft.block;
import net.minecraft.block.material.Material;
import evilcraft.client.render.block.RenderSanguinaryPedestal;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.tileentity.TileSanguinaryPedestal;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class SanguinaryPedestal extends ConfigurableBlockContainer {
    
    private static SanguinaryPedestal _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SanguinaryPedestal(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryPedestal getInstance() {
        return _instance;
    }

    private SanguinaryPedestal(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileSanguinaryPedestal.class);
    }

    /*@SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB area, List collisionBoxes, Entity entity) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBoundsForItemRender();
    }*/
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderSanguinaryPedestal.ID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

}
