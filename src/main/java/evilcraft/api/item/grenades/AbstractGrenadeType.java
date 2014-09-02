package evilcraft.api.item.grenades;

import evilcraft.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Abstract class for {@link IGrenadeType}S that contains some
 * common functionality.
 * @author immortaleeb
 *
 */
public abstract class AbstractGrenadeType implements IGrenadeType {
	
	private IIcon icon = null;
	
	@Override
	public String getTextureName() {
		return Reference.MOD_ID + ":" + getName();
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(getTextureName());
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}
}
