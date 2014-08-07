package evilcraft.modcompat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import evilcraft.entities.monster.VengeanceSpirit;

/**
 * Waila data provider for vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceSpiritDataProvider implements IWailaEntityProvider {

	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip,
			IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip,
			IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		if(accessor instanceof VengeanceSpirit && config.getConfig(Waila.getVengeanceSpiritConfigID())) {
			String name = ((VengeanceSpirit) entity).getLocalizedInnerEntityName();
            currenttip.add(name);
		}
		return null;
	}

	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip,
			IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

}
