package evilcraft.api.render;

import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

/**
 * A component to be used in Blocks. Depending on the amount of alternateIcons,
 * this component will make sure that depending on the location of the block, different
 * icons will be displayed. Icons must be available in the following format:
 * 'textureNameBase_{0-(alternateIcons-1)}'
 * 
 * To use this the following methods should be called:
 *  * Constructor should only be called once in Block.
 *  * registerIcons should be called from registerIcons in the Block, super call is not needed.
 *  * getAlternateIcon should be called from getBlockTexture in Block.
 *  * getBaseIcon should be called from getIcon in Block, since this call is now only used by inventory blocks.
 * @author rubensworks
 *
 */
public class AlternatingBlockIconComponent {
    
    private Icon[] alternateIcons;
    private Random random = new Random();
    
    public AlternatingBlockIconComponent(int alternateIcons) {
        this.alternateIcons =  new Icon[alternateIcons];
    }
    
    public void registerIcons(String textureNameBase, IconRegister iconRegister) {
        for(int i = 0; i < getAlternateIcons().length; i++) {
            alternateIcons[i] = iconRegister.registerIcon(textureNameBase + "_" + i);
        }
    }

    public Icon[] getAlternateIcons() {
        return alternateIcons;
    }
    
    public Icon getAlternateIcon(IBlockAccess world, int x, int y, int z, int side) {
        random.setSeed(String.format("%s:%s:%s:%s", x, y, z, side).hashCode());
        int randomIndex = random.nextInt(getAlternateIcons().length);
        return alternateIcons[randomIndex];
    }

    public Icon getBaseIcon() {
        return alternateIcons[0];
    }
}
