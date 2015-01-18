package evilcraft.infobook;

import lombok.Getter;

/**
 * @author rubensworks
 */
public class HyperLink {

    @Getter private int x, y;
    @Getter private InfoSection target;

    public HyperLink(int x, int y, InfoSection target) {
        this.x = x;
        this.y = y;
        this.target = target;
    }

}
