package evilcraft.core.world;

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
	
	private static FakeWorld _instance = null;
	
	/**
	 * Get the unique instance.
	 * @return The unique instance.
	 */
	public static FakeWorld getInstance() {
		if(_instance == null) {
			_instance = new FakeWorld();
		}
		return _instance;
	}

	/**
	 * Make a new instance.
	 */
	public FakeWorld() {
		super(new FakeSaveHandler(), "FakeWorld", new WorldSettings(new FakeWorldInfo()), new FakeWorldProvider(), new Profiler());
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

	@Override
	protected int func_152379_p() {
		return 0;
	}

}
