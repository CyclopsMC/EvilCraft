package evilcraft.api.config;

import net.minecraft.util.Icon;

public interface MultiRenderPassBlock {
    
    public Icon getIcon(int side, int meta, int renderPass);
    
}
