package org.cyclops.evilcraft.core.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

/**
 * A dummy implementation of {@link WorldProvider}.
 * @author rubensworks
 *
 */
public class FakeWorldProvider extends WorldProvider {

	@Override
	public String getSaveFolder() {
		return "FakeWorld";
	}

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }
}
