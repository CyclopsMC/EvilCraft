package evilcraft.infobook;

/**
 * Registry for the Origins of Darkness info.
 * @author rubensworks
 */
public class InfoBookRegistry {

    private static InfoBookRegistry _instance;
    private InfoSection main;

    private InfoBookRegistry() {
        main = InfoBookParser.initializeInfoBook();
    }

    public static InfoBookRegistry getInstance() {
        if(_instance == null) {
            _instance = new InfoBookRegistry();
        }
        return _instance;
    }

    public InfoSection getRoot() {
        return main;
    }

}
