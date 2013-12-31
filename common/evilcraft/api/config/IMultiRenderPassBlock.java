package evilcraft.api.config;

import net.minecraft.util.Icon;

public interface IMultiRenderPassBlock {
    
    public Icon getIcon(int side, int meta, int renderPass);
    public int getRenderPasses();
    public void setRenderPass(int pass);
    
}
