package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockRarityProvider;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.EntityHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.core.world.FakeWorld;
import evilcraft.entity.monster.VengeanceSpirit;
import evilcraft.tileentity.TileBoxOfEternalClosure;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * A box that can hold beings from higher dimensions.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosure extends ConfigurableBlockContainer implements IInformationProvider, IBlockRarityProvider {
	
	private static final int LIGHT_LEVEL = 6;
	
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
     * Get the ID of an inner spirit, can be -1.
     * @param itemStack The item stack.
     * @return The ID or -1.
     */
    public int getSpiritID(ItemStack itemStack) {
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT);
			if(spiritTag != null) {
				String innerEntity = spiritTag.getString(VengeanceSpirit.NBTKEY_INNER_SPIRIT);
				if(innerEntity != null && !innerEntity.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(innerEntity);
                        if(!VengeanceSpirit.canSustainClass(clazz)) return -1;
						Integer ret = ObfuscationHelpers.getClassToID().get(clazz);
						if(ret == null) {
							return -1;
						} else {
							return ret;
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return -1;
    }
    
    /**
     * Get the name of an inner spirit, can be null.
     * @param itemStack The item stack.
     * @return The name.
     */
    public String getSpiritName(ItemStack itemStack) {
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT);
			if(spiritTag != null && !spiritTag.hasNoTags()) {
				String innerEntity = spiritTag.getString(VengeanceSpirit.NBTKEY_INNER_SPIRIT);
				if(innerEntity != null && !innerEntity.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(innerEntity);
                        if(!VengeanceSpirit.canSustainClass(clazz)) return null;
						return (String) EntityList.classToStringMapping.get(clazz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					return VengeanceSpirit.DEFAULT_L10N_KEY;
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
		String id = getSpiritName(itemStack);
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
    	if(world.getTileEntity(x, y, z) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(x, y, z);
	    	if(tile.getSpiritInstance() != null) {
	    		world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.chestopen",
	    				0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	    		if(!world.isRemote) {
	    			tile.releaseSpirit();
	    		}
	    		return true;
	    	}
    	}
    	return super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
    	if(world.getTileEntity(x, y, z) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(x, y, z);
	    	if(tile.getLidAngle() > 0) {
	    		return LIGHT_LEVEL;
	    	}
    	}
        return super.getLightValue(world, x, y, z);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item));
        ItemStack swarmStack = new ItemStack(item);
        setVengeanceSwarmContent(swarmStack);
        list.add(swarmStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        if(world.getTileEntity(x, y, z) != null) {
            TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(x, y, z);
            if(tile.getSpiritInstance() != null) {
                return 15;
            }
        }
        return super.getComparatorInputOverride(world, x, y, z, side);
    }

}
