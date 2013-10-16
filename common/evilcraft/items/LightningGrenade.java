package evilcraft.items;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableItem;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.item.EntityLightningGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightningGrenade extends ConfigurableItem {
    
    private static LightningGrenade _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LightningGrenade(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static LightningGrenade getInstance() {
        return _instance;
    }

    private LightningGrenade(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }
        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(new EntityLightningGrenade(par2World, par3EntityPlayer));
        }

        return par1ItemStack;
    }

}
