package evilcraft.api.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class CustomRenderBlocks extends RenderBlocks{

    /**
     * Set the IBlockAccess world.
     * @param world Set the world to apply to.
     */
    public void setWorld(IBlockAccess world) {
        this.blockAccess = world;
    }
    
}
