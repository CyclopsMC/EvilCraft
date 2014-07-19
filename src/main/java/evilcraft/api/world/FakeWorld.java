package evilcraft.api.world;

import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * A fake world, use with caution.
 * @author rubensworks
 *
 */
public class FakeWorld extends World {

	/**
	 * Make a new instance.
	 */
	public FakeWorld() {
		super(new FakeSaveHandler(), "FakeWorld", new FakeWorldProvider(), new WorldSettings(new FakeWorldInfo()), new Profiler());
		this.isRemote = false;
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		return new FakeChunkProvider();
	}

	@Override
	public Entity getEntityByID(int var1) {
		return null;
	}

}
