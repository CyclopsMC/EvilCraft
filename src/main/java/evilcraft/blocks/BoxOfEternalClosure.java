package evilcraft.blocks;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.EntityHelpers;
import evilcraft.api.IInformationProvider;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.api.world.FakeWorld;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.entities.tileentities.TileBoxOfEternalClosure;

/**
 * A box that can hold beings from higher dimensions.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosure extends ConfigurableBlockContainer implements IInformationProvider {
    
    private static BoxOfEternalClosure _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BoxOfEternalClosure(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BoxOfEternalClosure getInstance() {
        return _instance;
    }

    private BoxOfEternalClosure(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileBoxOfEternalClosure.class);
        
        this.setHardness(2.5F);
        this.setStepSound(soundTypePiston);
        this.setRotatable(true);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        // This is ONLY used for the block breaking/broken particles
        // Since the ender chest looks very similar, we use that icon.
        return Blocks.ender_chest.getIcon(0, 0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
    	EvilCraftTileEntity tile = (EvilCraftTileEntity) world.getTileEntity(x, y, z);
        if(tile.getRotation() == ForgeDirection.EAST || tile.getRotation() == ForgeDirection.WEST) {
        	setBlockBounds(0.2F, 0F, 0.0F, 0.8F, 0.43F, 1.0F);
        } else {
        	setBlockBounds(0.0F, 0F, 0.2F, 1.0F, 0.43F, 0.8F);
        }
    }
    
    @Override
    public int getRenderType() {
    	return -1;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    	return false;
    }
    
    /**
     * Get the ID of an inner spirit, can be null.
     * @param itemStack The item stack.
     * @return The ID.
     */
    public String getSpiritId(ItemStack itemStack) {
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT);
			if(spiritTag != null) {
				String innerEntity = spiritTag.getString(VengeanceSpirit.NBTKEY_INNER_SPIRIT);
				if(innerEntity != null && !innerEntity.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(innerEntity);
						return (String) EntityList.classToStringMapping.get(clazz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					return "vengeanceSpirit";
				}
			}
		}
		return null;
    }
    
    /**
     * Put a vengeance swarm inside the given box.
     * @param itemStack The box.
     */
    public static void setVengeanceSwarmContent(ItemStack itemStack) {
    	NBTTagCompound tag = new NBTTagCompound();
    	NBTTagCompound spiritTag = new NBTTagCompound();
    	
    	VengeanceSpirit spirit = new VengeanceSpirit(FakeWorld.getInstance());
    	spirit.setGlobalVengeance(true);
    	spirit.setIsSwarm(true);
    	spirit.writeToNBT(spiritTag);
    	String entityId = EntityList.getEntityString(spirit);
    	
		spiritTag.setString(EntityHelpers.NBTTAG_ID, entityId);
		tag.setTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
		itemStack.setTagCompound(tag);
    }

	@Override
	public String getInfo(ItemStack itemStack) {
		String content = EnumChatFormatting.ITALIC + L10NHelpers.localize("general.info.empty");
		String id = getSpiritId(itemStack);
		if(id != null) {
			content = L10NHelpers.getLocalizedEntityName(id);
		}
		return EnumChatFormatting.BOLD + L10NHelpers.localize(getUnlocalizedName() + ".info.content",
				new Object[]{EnumChatFormatting.RESET + content});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4) {
		
	}

    @Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(!canPlaceBlockAt(world, x, y, z)) {
        	dropBlockAsItem(world, x, y, z, 0, 0);
        	world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
    	if(!world.isRemote && world.getTileEntity(x, y, z) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(x, y, z);
	    	if(tile.getSpiritInstance() != null) {
	    		tile.releaseSpirit();
	    		return true;
	    	}
    	}
    	return super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item));
        ItemStack swarmStack = new ItemStack(item);
        setVengeanceSwarmContent(swarmStack);
        list.add(swarmStack);
    }

}
