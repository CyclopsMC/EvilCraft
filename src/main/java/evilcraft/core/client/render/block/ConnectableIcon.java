package evilcraft.core.client.render.block;

import evilcraft.core.DirectionCorner;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A virtual {@link TextureAtlasSprite} that has several icons and needs multiple render passes for
 * making it possible to dynamically render connected textures.
 * Blocks that use this icon must implement {@link IMultiRenderPassBlock}.
 * Five icons are necessary for this.
 * <ul>
 *  <li>An icon that will be used as background, it will always be rendered at the
 *  lowest pass.</li>
 *  <li>An icon that contains one border, it will be rotated four times. Some passes
 *  might be invisible depending on the neighbouring blocks.</li>
 *  <li>An icon that contains one corner, analogously used as the border icon.</li>
 *  <li>An icon that contains one inner corner, analogously used as the border icon.</li>
 *  <li>An icon that with the solely use of rendering inventory blocks.</li>
 * </ul>
 * @author rubensworks
 *
 */
public class ConnectableIcon implements TextureAtlasSprite{
    
    protected static final int LAYERS = 4;
    protected static final int EDGES = 4;
    protected static final EnumFacing sideS = DirectionHelpers.DIRECTIONS.size();
    protected TextureAtlasSprite[] icons = new TextureAtlasSprite[LAYERS];// background; borders; corners; innerCorners
    protected TextureAtlasSprite inventoryBlockIcon; // Icon for inventoryBlock
    
    protected int renderPass = 0; // Current renderpass of the icon
    protected EnumFacing side = 0; // Current side to display icon for
    protected boolean isInventoryBlock = false;
    
    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    protected static final EnumFacing[][] CONNECT_MATRIX = {
        {EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST}, // DOWN
        {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}, // UP
        {EnumFacing.UP, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.EAST}, // NORTH
        {EnumFacing.UP, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.WEST}, // SOUTH
        {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.NORTH}, // WEST
        {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH}, // EAST
    };
    
    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    protected static final DirectionCorner[][] CONNECT_CORNER_MATRIX = {
        {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_SOUTHWEST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_NORTHEAST}, // DOWN
        {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_NORTHEAST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_SOUTHWEST}, // UP
        {DirectionCorner.UPPER_EAST, DirectionCorner.UPPER_WEST, DirectionCorner.LOWER_WEST, DirectionCorner.LOWER_EAST}, // NORTH
        {DirectionCorner.UPPER_WEST, DirectionCorner.UPPER_EAST, DirectionCorner.LOWER_EAST, DirectionCorner.LOWER_WEST}, // SOUTH
        {DirectionCorner.UPPER_NORTH, DirectionCorner.UPPER_SOUTH, DirectionCorner.LOWER_SOUTH, DirectionCorner.LOWER_NORTH}, // WEST
        {DirectionCorner.UPPER_SOUTH, DirectionCorner.UPPER_NORTH, DirectionCorner.LOWER_NORTH, DirectionCorner.LOWER_SOUTH}, // EAST
    };
    
    // Temporary connection booleans, these will update every render call.
    // Which directions relative to this blockState should connect (have same ID for example)
    protected boolean[] connectWithSides = new boolean[DirectionHelpers.DIRECTIONS.size()];
    // Which directions relative to this blockState (with corner) should connect (have same ID for example)
    protected boolean[] connectWithSidesCorner = new boolean[DirectionHelpers.DIRECTIONS_CORNERS.size()];
    
    /**
     * Make a new instance.
     * @param background The background icon.
     * @param borders The border icon.
     * @param corners The corner icon.
     * @param innerCorners The inner corner icon.
     * @param inventoryBlockIcon The inventory blockState icon.
     */
    public ConnectableIcon(TextureAtlasSprite background, TextureAtlasSprite borders, TextureAtlasSprite corners, TextureAtlasSprite innerCorners, TextureAtlasSprite inventoryBlockIcon) {
        this.icons[0] = background;
        this.icons[1] = borders;
        this.icons[2] = corners;
        this.icons[3] = innerCorners;
        this.inventoryBlockIcon = inventoryBlockIcon;
    }
    
    protected void setRenderPass(int renderPass) {
        if(renderPass >= 0 && renderPass < getRequiredPasses())
            this.renderPass = renderPass;
        else
            this.renderPass = 0;
    }
    
    protected void setSide(EnumFacing side) {
        if(side >= 0 && side < SIDES)
            this.side = side;
        else
            this.side = 0;
    }
    
    protected TextureAtlasSprite getInnerIcon() {
        if(isInventoryBlock)
            return inventoryBlockIcon;
        else if(shouldRender(getCurrentLayer()))
            return this.icons[getCurrentLayer()];
        else
            return RenderHelpers.EMPTYICON;
    }
    
    protected boolean shouldConnect(EnumFacing side, int rotation) {
        return getConnectWithSides()[CONNECT_MATRIX[side][rotation].ordinal()];
    }
    
    protected boolean shouldConnectCorner(EnumFacing side, int rotation) {
        return getConnectWithSidesCorner()[CONNECT_CORNER_MATRIX[side][rotation].ordinal()];
    }
    
    protected boolean shouldRender(int layer) {
        if(layer == Layer.BACKGROUND.ordinal()) {
            return true;
        } else if(layer == Layer.BORDERS.ordinal()) {
            return !shouldConnect(getCurrentSide(), getCurrentRotation());
        } else if(layer == Layer.CORNERS.ordinal()) {
            // Fix for mirrored DOWN rotation
            int incr = -1;
            if(this.getCurrentSide() == EnumFacing.DOWN.ordinal())
                incr = 1;
            return !shouldConnect(getCurrentSide(), getCurrentRotation())
                    && !shouldConnect(getCurrentSide(), (getCurrentRotation() + EDGES + incr) % EDGES);
        } else {
            // Fix for mirrored DOWN rotation
            int incr = -1;
            if(this.getCurrentSide() == EnumFacing.DOWN.ordinal())
                incr = 1;
            return (shouldConnect(getCurrentSide(), getCurrentRotation())
                    && shouldConnect(getCurrentSide(), (getCurrentRotation() + EDGES + incr) % EDGES)
                    )
                    && !(shouldConnectCorner(getCurrentSide(), getCurrentRotation()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return getInnerIcon().getIconWidth();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return getInnerIcon().getIconHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinU() {
        return getInnerIcon().getMinU();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxU() {
        return getInnerIcon().getMaxU();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedU(double d0) {
        return getInnerIcon().getInterpolatedU(d0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinV() {
        return getInnerIcon().getMinV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxV() {
        return getInnerIcon().getMaxV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedV(double d0) {
        return getInnerIcon().getInterpolatedV(d0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getIconName() {
        return getInnerIcon().getIconName();
    }
    
    protected int getCurrentLayer() {
        return (renderPass - getCurrentRotation()) / LAYERS;
    }
    
    protected int getCurrentRotation() {
        return renderPass % LAYERS;
    }
    
    protected int getCurrentSide() {
        return side;
    }
    
    /**
     * Get the required render passes for rendering all the possible connections.
     * @return The render passes.
     */
    public int getRequiredPasses() {
        if(isInventoryBlock)
            return 1;
        return LAYERS * EDGES;
    }

    /**
     * Will be called before rendering the icon to set the correct inner icon.
     * @param side The side that will be rendered.
     * @param renderPass The render pass for that side.
     * @param renderBlocks The {@link RenderBlocks} instance.
     */
    public void prepareIcon(EnumFacing side, int renderPass, RenderBlocks renderBlocks) {
        this.setRenderPass(renderPass);
        this.setSide(side);
        int rotation = getCurrentRotation();
        EnumFacing renderSide = EnumFacing.getOrientation(side);
        RenderHelpers.setRenderBlocksUVRotation(renderBlocks, renderSide, rotation);
    }
    
    /**
     * Define whether or not the current rendering is for an inventory blockState.
     * @param isInventoryBlock The new value
     */
    public void setInventoryBlock(boolean isInventoryBlock) {
        this.isInventoryBlock = isInventoryBlock;
    }
    
    enum Layer {
        BACKGROUND,
        BORDERS,
        CORNERS,
        INNERCORNERS;
    }
    
    /**
     * Get the array that is indexed by the {@link EnumFacing} ordinal values with
     * their respective value for whether or not this blockState should connect to that blockState.
     * @return The connect with sides boolean array.
     */
    public boolean[] getConnectWithSides() {
        return connectWithSides;
    }
    
    /**
     * Get the array that is indexed by the {@link DirectionCorner} ordinal values with
     * their respective value for whether or not this blockState should connect to that blockState.
     * @return The connect with sides boolean array.
     */
    public boolean[] getConnectWithSidesCorner() {
        return connectWithSidesCorner;
    }
    
    /**
     * Set the connection to a certain {@link EnumFacing}.
     * @param direction The direction to enable/disable the connection to.
     * @param connect If the connection for the given direction should be enabled.
     */
    public void connect(EnumFacing direction, boolean connect) {
        this.connectWithSides[direction.ordinal()] = connect;
    }
    
    /**
     * Set the connection to a certain {@link DirectionCorner}.
     * @param direction The direction to enable/disable the connection to.
     * @param connect If the connection for the given direction should be enabled.
     */
    public void connectCorner(DirectionCorner direction, boolean connect) {
        this.connectWithSidesCorner[direction.ordinal()] = connect;
    }

}
