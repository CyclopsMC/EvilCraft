package org.cyclops.evilcraft.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.UnlistedProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

import java.util.List;
import java.util.Map;

/**
 * A stand for displaying items.
 * @author rubensworks
 *
 */
public class DisplayStand extends ConfigurableBlockContainer implements IInformationProvider {

    private static final String NBT_TYPE = "displayStandType";

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    @BlockProperty
    public static final PropertyBool AXIS_X = PropertyBool.create("axis_x");
    @BlockProperty
    public static final IUnlistedProperty<EnumFacing.AxisDirection> DIRECTION = new UnlistedProperty<EnumFacing.AxisDirection>("direction", EnumFacing.AxisDirection.class);
    @BlockProperty
    public static final IUnlistedProperty<ItemStack> TYPE = new UnlistedProperty<ItemStack>("blocktype", ItemStack.class);

    private static DisplayStand _instance = null;

    public static final Map<EnumFacing, AxisAlignedBB> FACING_BOUNDS = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
            .put(EnumFacing.DOWN, new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 0.5F, 0.625F))
            .put(EnumFacing.UP, new AxisAlignedBB(0.375F, 0.5F, 0.375F, 0.625F, 1.0F, 0.625F))
            .put(EnumFacing.WEST, new AxisAlignedBB(0.0F, 0.375F, 0.375F, 0.5F, 0.625F, 0.625F))
            .put(EnumFacing.EAST, new AxisAlignedBB(0.5F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F))
            .put(EnumFacing.NORTH, new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 0.5F))
            .put(EnumFacing.SOUTH, new AxisAlignedBB(0.375F, 0.375F, 0.5F, 0.625F, 0.625F, 1.0F))
            .build();

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DisplayStand getInstance() {
        return _instance;
    }

    public DisplayStand(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.WOOD, TileDisplayStand.class);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState blockState = (IExtendedBlockState) state;
        TileDisplayStand tile = TileHelpers.getSafeTile(world, pos, TileDisplayStand.class);
        if (tile != null) {
            blockState = blockState.withProperty(DIRECTION, tile.getDirection());
            if (tile.getDisplayStandType() != null) {
                blockState = blockState.withProperty(TYPE, tile.getDisplayStandType());
            }
        }
        return blockState;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FACING_BOUNDS.get(BlockHelpers.getSafeBlockStateProperty(state, FACING, EnumFacing.DOWN));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
        TileDisplayStand tile = TileHelpers.getSafeTile(world, blockPos, TileDisplayStand.class);
        if (tile != null && tile.getStackInSlot(0) != null) {
            return 15;
        }
        return 0;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState blockState = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        blockState = blockState.withProperty(FACING, facing.getOpposite());
        EnumFacing playerFacing = placer.getHorizontalFacing();
        boolean axisX;
        if (facing.getOpposite() == EnumFacing.DOWN || facing.getOpposite() == EnumFacing.UP) {
            axisX = playerFacing.getAxis() == EnumFacing.Axis.X;
        } else {
            axisX = playerFacing.getAxis() != EnumFacing.Axis.X && playerFacing.getAxis() != EnumFacing.Axis.Z;
        }
        blockState = blockState.withProperty(AXIS_X, axisX);
        return blockState;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, blockPos, blockState, entity, stack);
        TileDisplayStand tile = TileHelpers.getSafeTile(world, blockPos, TileDisplayStand.class);
        if(tile != null) {
            tile.setDirection(entity.getHorizontalFacing().getAxisDirection());
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileDisplayStand tile = TileHelpers.getSafeTile(world, pos, TileDisplayStand.class);
        if (tile != null) {
            IBlockState blockState = world.getBlockState(pos);
            if (tile.getDirection() == EnumFacing.AxisDirection.POSITIVE) {
                if (blockState.getValue(AXIS_X)) {
                    world.setBlockState(pos, blockState.withProperty(AXIS_X, false));
                    tile.setDirection(EnumFacing.AxisDirection.POSITIVE);
                } else {
                    world.setBlockState(pos, blockState.withProperty(AXIS_X, true));
                    tile.setDirection(EnumFacing.AxisDirection.NEGATIVE);
                }
            } else {
                if (blockState.getValue(AXIS_X)) {
                    world.setBlockState(pos, blockState.withProperty(AXIS_X, false));
                    tile.setDirection(EnumFacing.AxisDirection.NEGATIVE);
                } else {
                    world.setBlockState(pos, blockState.withProperty(AXIS_X, true));
                    tile.setDirection(EnumFacing.AxisDirection.POSITIVE);
                }
            }
        }
        return super.rotateBlock(world, pos, axis);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (ItemStack plankWoodStack : OreDictionary.getOres("plankWood")) {
            if (plankWoodStack.getItem() instanceof ItemBlock) {
                int plankWoodMeta = plankWoodStack.getItemDamage();
                if (plankWoodMeta == OreDictionary.WILDCARD_VALUE) {
                    List<ItemStack> plankWoodSubItems = Lists.newArrayList();
                    plankWoodStack.getItem().getSubItems(plankWoodStack.getItem(), null, plankWoodSubItems);
                    for (ItemStack plankWoodSubItem : plankWoodSubItems) {
                        IBlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodSubItem);
                        list.add(getTypedDisplayStandItem(plankWoodBlockState));
                    }
                } else {
                    IBlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack);
                    list.add(getTypedDisplayStandItem(plankWoodBlockState));
                }
            }
        }
    }

    public ItemStack getTypedDisplayStandItem(IBlockState blockState) {
        Block block = blockState.getBlock();
        int meta = blockState.getBlock().getMetaFromState(blockState);

        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(itemStack);

        ItemStack blockStack = new ItemStack(block, 1, meta);
        NBTTagCompound blockTag = new NBTTagCompound();
        blockStack.writeToNBT(blockTag);

        tag.setTag(NBT_TYPE, blockTag);
        return itemStack;
    }

    public ItemStack getDisplayStandType(ItemStack displayStandStack) {
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(displayStandStack);
        if (tag.hasKey(NBT_TYPE)) {
            NBTTagCompound blockTag = tag.getCompoundTag(NBT_TYPE);
            return ItemStack.loadItemStackFromNBT(blockTag);
        }
        return null;
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Force allow right clicking with a fluid container passing through to this block
        if (event.getItemStack() != null
                && event.getItemStack().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                && event.getWorld().getBlockState(event.getPos()).getBlock() == this) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileDisplayStand tile = TileHelpers.getSafeTile(world, pos, TileDisplayStand.class);
            if (tile != null) {
                ItemStack tileStack = tile.getStackInSlot(0);
                if ((itemStack == null || (ItemStack.areItemsEqual(itemStack, tileStack) && ItemStack.areItemStackTagsEqual(itemStack, tileStack) && tileStack.stackSize < tileStack.getMaxStackSize())) && tileStack != null) {
                    if(itemStack != null) {
                        tileStack.stackSize += itemStack.stackSize;
                    }
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tileStack);
                    tile.setInventorySlotContents(0, null);
                    tile.sendUpdate();
                    return true;
                } else if (itemStack != null && tile.getStackInSlot(0) == null) {
                    tile.setInventorySlotContents(0, itemStack.splitStack(1));
                    if (itemStack.stackSize <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    tile.sendUpdate();
                    return true;
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        for (EnumFacing facing : DisplayStand.FACING.getAllowedValues()) {
            for (Boolean axisX : DisplayStand.AXIS_X.getAllowedValues()) {
                String blockVariant = String.format("%s=%s,%s=%s",
                        DisplayStand.AXIS_X.getName(), DisplayStand.AXIS_X.getName(axisX),
                        DisplayStand.FACING.getName(), DisplayStand.FACING.getName(facing));
                bakeVariantModel(event, blockVariant, !axisX);
            }
        }
        bakeVariantModel(event, "inventory", false);
    }

    protected void bakeVariantModel(ModelBakeEvent event, String variant, boolean rotated) {
        ModelResourceLocation modelVariantLocation = new ModelResourceLocation(Reference.MOD_ID + ":displayStand", variant);
        try {
            ResourceLocation modelLocation = new ResourceLocation(Reference.MOD_ID, "block/displayStand" + (rotated ? "_rotated" : ""));
            IBakedModel originalBakedModel = event.getModelRegistry().getObject(modelVariantLocation);
            // We actually need to retrieve modelVariantLocation, but that seems to make it a non-IRetexturableModel
            // So instead, we manually apply model rotation in ModelDisplayStand when baking.
            IModel model = ModelLoaderRegistry.getModel(modelLocation);
            if (model instanceof IRetexturableModel && originalBakedModel instanceof IPerspectiveAwareModel) {
                event.getModelRegistry().putObject(modelVariantLocation,
                        new ModelDisplayStand((IPerspectiveAwareModel) originalBakedModel, (IRetexturableModel) model));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInfo(ItemStack itemStack) {
        ItemStack blockType = getDisplayStandType(itemStack);
        if (blockType != null) {
            return blockType.getDisplayName();
        }
        return "";
    }

    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {

    }
}
