package evilcraft.items;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.monster.Werewolf;
import evilcraft.entities.monster.WerewolfConfig;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class WerewolfBone extends ConfigurableItem {
    
    private static WerewolfBone _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new WerewolfBone(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static WerewolfBone getInstance() {
        return _instance;
    }

    private WerewolfBone(ExtendedConfig eConfig) {
        super(eConfig);
    }
    
    // Disabled for now
    /*@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {           
        if(!world.isRemote) {
            stack.stackSize--;
            Helpers.spawnCreature(world, WerewolfConfig._instance.ID, x, y + 1, z);
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }*/

}
