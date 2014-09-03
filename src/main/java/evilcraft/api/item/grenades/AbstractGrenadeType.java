package evilcraft.api.item.grenades;

import evilcraft.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Abstract class for {@link IGrenadeType}S that contains some
 * common functionality.
 * @author immortaleeb
 *
 */
public abstract class AbstractGrenadeType implements IGrenadeType {
	
	private IIcon icon = null;
	private final int id;
	
	/**
	 * Creates a new instance of this class.
	 * @param id The unique id for this grenade.
	 */
	public AbstractGrenadeType(int id) {
		this.id = id;
	}
	
	@Override
	public String getTextureName() {
		return Reference.MOD_ID + ":" + getName();
	}
	
	@Override
	public ResourceLocation getTextureLocation() {
		return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ITEMS + getName() + ".png");
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(getTextureName());
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}
	
	@Override
	public final int getId() {
		return id;
	}
	
	/**
     * Returns the name of this grenade type. This function is used to
     * locate the icon of the current grenade type.
     * @return The name of this grenade type.
     */
	public abstract String getName();
}
