package evilcraft.infobook;

import lombok.Getter;

/**
 * A link wrapper targeted at other sections.
 * @author rubensworks
 */
public class HyperLink {

    @Getter private int x, y;
    @Getter private InfoSection target;
    @Getter private String unlocalizedName;

    public HyperLink(int x, int y, InfoSection target, String unlocalizedName) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.unlocalizedName = unlocalizedName;
    }

}
