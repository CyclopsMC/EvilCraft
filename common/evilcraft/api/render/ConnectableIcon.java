package evilcraft.api.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.DirectionCorner;
import evilcraft.api.Helpers;
import evilcraft.api.RenderHelpers;
import evilcraft.api.entities.tileentitites.TileConnectedTexture;

public class ConnectableIcon implements Icon{
    
    protected static final int LAYERS = 4;
    protected static final int EDGES = 4;
    protected static final int SIDES = Helpers.DIRECTIONS.size();
    protected Icon[] icons = new Icon[LAYERS];// background; borders; corners; innerCorners
    
    protected int renderPass = 0; // Current renderpass of the icon
    protected int side = 0; // Current side to display icon for
    
    protected TileConnectedTexture tileConnectedTexture = null;
    
    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    public static final ForgeDirection[][] CONNECT_MATRIX = {
        {ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.SOUTH, ForgeDirection.EAST}, // DOWN
        {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST}, // UP
        {ForgeDirection.UP, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.EAST}, // NORTH
        {ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.WEST}, // SOUTH
        {ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.NORTH}, // WEST
        {ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.SOUTH}, // EAST
    };
    
    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    public static final DirectionCorner[][] CONNECT_CORNER_MATRIX = {
        {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_SOUTHWEST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_NORTHEAST}, // DOWN
        {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_NORTHEAST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_SOUTHWEST}, // UP
        {DirectionCorner.UPPER_EAST, DirectionCorner.UPPER_WEST, DirectionCorner.LOWER_WEST, DirectionCorner.LOWER_EAST}, // NORTH
        {DirectionCorner.UPPER_WEST, DirectionCorner.UPPER_EAST, DirectionCorner.LOWER_EAST, DirectionCorner.LOWER_WEST}, // SOUTH
        {DirectionCorner.UPPER_NORTH, DirectionCorner.UPPER_SOUTH, DirectionCorner.LOWER_SOUTH, DirectionCorner.LOWER_NORTH}, // WEST
        {DirectionCorner.UPPER_SOUTH, DirectionCorner.UPPER_NORTH, DirectionCorner.LOWER_NORTH, DirectionCorner.LOWER_SOUTH}, // EAST
    };
    
    public ConnectableIcon(Icon background, Icon borders, Icon corners, Icon innerCorners) {
        this.icons[0] = background;
        this.icons[1] = borders;
        this.icons[2] = corners;
        this.icons[3] = innerCorners;
    }
    
    public void setTileConnectedTexture(TileConnectedTexture tileConnectedTexture) {
        this.tileConnectedTexture = tileConnectedTexture;
    }
    
    protected void setRenderPass(int renderPass) {
        if(renderPass >= 0 && renderPass < getRequiredPasses())
            this.renderPass = renderPass;
        else
            this.renderPass = 0;
    }
    
    protected void setSide(int side) {
        if(side >= 0 && side < SIDES)
            this.side = side;
        else
            this.side = 0;
    }
    
    protected Icon getInnerIcon() {
        if(shouldRender(getCurrentLayer()))
            return this.icons[getCurrentLayer()];
        else
            return RenderHelpers.EMPTYICON;
    }
    
    protected boolean shouldConnect(int side, int rotation) {
        
        return tileConnectedTexture.getConnectWithSides()[CONNECT_MATRIX[side][rotation].ordinal()];
    }
    
    protected boolean shouldConnectCorner(int side, int rotation) {
        return tileConnectedTexture.getConnectWithSidesCorner()[CONNECT_CORNER_MATRIX[side][rotation].ordinal()];
    }
    
    protected boolean shouldRender(int layer) {
        //return this.getCurrentRotation() == 0;
        if(layer == Layer.BACKGROUND.ordinal()) {
            return true;
        } else if(layer == Layer.BORDERS.ordinal()) {
            return !shouldConnect(getCurrentSide(), getCurrentRotation());
        } else if(layer == Layer.CORNERS.ordinal()) {
            // Fix for mirrored DOWN rotation
            int incr = -1;
            if(this.getCurrentSide() == ForgeDirection.DOWN.ordinal())
                incr = 1;
            return !shouldConnect(getCurrentSide(), getCurrentRotation())
                    && !shouldConnect(getCurrentSide(), (getCurrentRotation() + EDGES + incr) % EDGES);
        } else {
            // Fix for mirrored DOWN rotation
            int incr = -1;
            if(this.getCurrentSide() == ForgeDirection.DOWN.ordinal())
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
    
    public int getRequiredPasses() {
        return LAYERS * EDGES;
    }

    public void prepareIcon(int side, int renderPass, RenderBlocks renderBlocks) {
        this.setRenderPass(renderPass);
        this.setSide(side);
        int rotation = getCurrentRotation();
        ForgeDirection renderSide = ForgeDirection.getOrientation(side);
        RenderHelpers.setRenderBlocksUVRotation(renderBlocks, renderSide, rotation);
    }
    
    enum Layer {
        BACKGROUND,
        BORDERS,
        CORNERS,
        INNERCORNERS;
    }

}
