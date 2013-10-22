package evilcraft.items;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.BloodStainedDirtConfig;
import evilcraft.liquids.Blood;
import evilcraft.render.EntityBloodSplashFX;

public class BloodExtractor extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodExtractor _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodExtractor(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static BloodExtractor getInstance() {
        return _instance;
    }

    private BloodExtractor(ExtendedConfig eConfig) {
        super(eConfig, 5000, Blood.getInstance());
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int blockID = world.getBlockId(x, y, z);
        if(!world.isRemote && blockID == BloodStainedDirtConfig._instance.ID) {
            Random random = new Random();
            
            // Fill the extractor a bit
            int toFill = BloodExtractorConfig.minMB + random.nextInt(BloodExtractorConfig.maxMB - BloodExtractorConfig.minMB);
            ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
            int filled = container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
            
            // Transform bloody dirt into regular dirt if we used some of the blood
            if(filled > 0) {
                world.setBlock(x, y, z, Block.dirt.blockID);
                
                // Init particles
                EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 5, 1 + random.nextInt(2));
            }
        }
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

}
