package evilcraft.core.world;

import net.minecraft.world.WorldProvider;

/**
 * A dummy implementation of {@link WorldProvider}.
 * @author rubensworks
 *
 */
public class FakeWorldProvider extends WorldProvider {

	@Override
	public String getDimensionName() {
		return "FakeWorld";
	}

    @Override
    public String getInternalNameSuffix() {
        return "FakeWorld";
    }

}
