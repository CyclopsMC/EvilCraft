package evilcraft.api.render;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * A component to be used in Blocks. Depending on the amount of alternateIcons,
 * this component will make sure that depending on the location of the block, different
 * icons will be displayed. Icons must be available in the following format:
 * 'textureNameBase_{0-(alternateIcons-1)}'
 * 
 * To use this the following methods should be called:
 * <ul>
 * <li>Constructor should only be called once in Block.</li>
 * <li>registerIcons should be called from registerIcons in the Block, super call is not needed.</li>
 * <li>getAlternateIcon should be called from getBlockTexture in Block.</li>
 * <li>getBaseIcon should be called from getIcon in Block, since this call is now only used by inventory blocks.</li>
 *  </ul>
 * @author rubensworks
 *
 */
public class AlternatingBlockIconComponent {
    
    private IIcon[] alternateIcons;
    private Random random = new Random();
    
    /**
     * Make a new instance.
     * @param alternateIcons The amount of icons to alternate.
     */
    public AlternatingBlockIconComponent(int alternateIcons) {
        this.alternateIcons =  new IIcon[alternateIcons];
    }
    
    /**
     * Register icons
     * @param textureNameBase The base texture name.
     * @param iconRegister The {@link IIconRegister}.
     */
    public void registerIcons(String textureNameBase, IIconRegister iconRegister) {
        for(int i = 0; i < getAlternateIcons().length; i++) {
            alternateIcons[i] = iconRegister.registerIcon(textureNameBase + "_" + i);
        }
    }

    /**
     * The array of alternate icons.
     * @return The icon array.
     */
    public IIcon[] getAlternateIcons() {
        return alternateIcons;
    }
    
    /**
     * Get one from the alternate icons depending on the coordinates and side of the block.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param side The side of the block that will be rendered.
     * @return The icon to render.
     */
    public IIcon getAlternateIcon(IBlockAccess world, int x, int y, int z, int side) {
        random.setSeed(String.format("%s:%s:%s:%s", x, y, z, side).hashCode());
        int randomIndex = random.nextInt(getAlternateIcons().length);
        return alternateIcons[randomIndex];
    }

    /**
     * Get the first/base icon.
     * @return The base icon.
     */
    public IIcon getBaseIcon() {
        return alternateIcons[0];
    }
}
