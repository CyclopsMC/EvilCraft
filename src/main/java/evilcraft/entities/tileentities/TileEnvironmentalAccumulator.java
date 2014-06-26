package evilcraft.entities.tileentities;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Coordinate;
import evilcraft.api.Helpers;
import evilcraft.api.degradation.DegradationExecutor;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.inventory.SimpleInventory;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.CustomRecipeResult;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipe;
import evilcraft.api.recipes.EnvironmentalAccumulatorResult;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.entities.tileentities.environmentalaccumulator.IEAProcessingFinishedEffect;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileEnvironmentalAccumulator extends EvilCraftBeaconTileEntity implements IBossDisplayData, IDegradable, IInventory {
    
    private static final int ITEM_MOVE_COOLDOWN_DURATION = 1;
    
    private static final double WEATHER_CONTAINER_MIN_DROP_HEIGHT = 0.0;
    private static final double WEATHER_CONTAINER_MAX_DROP_HEIGHT = 2.0;
    private static final double WEATHER_CONTAINER_SPAWN_HEIGHT = 
            EnvironmentalAccumulatorConfig.defaultProcessItemTickCount * 
            EnvironmentalAccumulatorConfig.defaultProcessItemSpeed + 1;
    
    private static final float ITEM_MIN_SPAWN_HEIGHT = 1.0f;
    
    private static final int DEGRADATION_RADIUS_BASE = 5;
    private static final int DEGRADATION_TICK_INTERVAL = 100;
    
    private DegradationExecutor degradationExecutor;
    // This number rises with the number of uses of the env. accum.
    private int degradation = 0;
    
    /**
     * Holds the state of the environmental accumulator.
     * The following states are possible: idle (the default case), cooling down,
     * processing an item and dropping an item. The different states can be found as
     * public static variables of {@link EnvironmentalAccumulator}.
     */
    private int state = 0;
    private int tick = 0;
    
    private SimpleInventory inventory;
    
    // The recipe we're currently working on
    private EnvironmentalAccumulatorRecipe recipe;
    
    /**
     * Make a new instance.
     */
	public TileEnvironmentalAccumulator() {
	    super();
	    
	    degradationExecutor = new DegradationExecutor(this);
	    
	    inventory = new SimpleInventory(1, EnvironmentalAccumulatorConfig._instance.NAMEDID, 64);
	    
	    if (Helpers.isClientSide()) {
	        setBeamInnerColor(getInnerColorByState(state));
	        setBeamOuterColor(getOuterColorByState(state));
	    }
	}
	
	@SideOnly(Side.CLIENT)
	private Vector4f getInnerColorByState(int state) {
	    if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.05f);
	    if (state == EnvironmentalAccumulator.STATE_IDLE)
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.13f);
	    else
	        return new Vector4f(0, 0, 0, 0.13f);
	        
	}
	
	@SideOnly(Side.CLIENT)
    private Vector4f getOuterColorByState(int state) {
        if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN)
            return new Vector4f(0, 0, 0, 0.13f);
        else
            return new Vector4f(0.30078125F, 0.1875F, 0.08203125F, 0.13f);
    }
	
	/**
	 * Get the maximum cooldown tick for accumulating weather.
	 * @return The maximum cooldown tick.
	 */
	public int getMaxCooldownTick() {
	    EnvironmentalAccumulatorResult result = (recipe == null) ? 
	            null : (EnvironmentalAccumulatorResult)CustomRecipeRegistry.get(recipe);
	    
	    if (result == null)
	        return EnvironmentalAccumulatorConfig.defaultTickCooldown;
	    else
	        return result.getCooldownTime();
	}
	
	/**
	 * Get the Y coordinate of the current moving item.
	 * @return The Y coordinate of the inner item.
	 */
	@SideOnly(Side.CLIENT)
	public float getMovingItemY() {
	    if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
	        return ITEM_MIN_SPAWN_HEIGHT + (getItemMoveDuration() - tick) * getItemMoveSpeed();
	    else
	        return -1;
	}
	
	/**
	 * Get the current recipe we're working on.
	 * @return Returns the recipe being processed, or null in case we're
	 *         not processing anything at the moment.
	 */
	public EnvironmentalAccumulatorRecipe getRecipe() {
	    return recipe;
	}
	
	private int getItemMoveDuration() {
	    if (recipe == null)
	        return EnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
	    else
	        return recipe.getDuration();
	}
	
	private float getItemMoveSpeed() {
	    if (recipe == null)
	        return (float) EnvironmentalAccumulatorConfig.defaultProcessItemSpeed;
	    else
	        return (float) recipe.getProcessingSpeed();
	}
	
	@Override
	public void updateEntity() {
	    // Keep ticking if necessary
	    if (tick > 0)
	        tick--;
	    
	    if (state == EnvironmentalAccumulator.STATE_IDLE) {
            updateEnvironmentalAccumulatorIdle();
        } // Are we processing an item?
        else if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM) {
            // Are we done moving the item?
            if (tick == 0) {
                dropItemStack();
                activateFinishedProcessingItemState();
            }
        } // Have we just finished processing an item?
        else if (state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
            // We stay in this state for a while so the client gets some time to 
            // show the corresponding effect when an item is finished processing
            
            // Are we done waiting for the client to update?
            if (tick == 0) {
                activateCooldownState();
                
                // Remove the items in our inventory
                this.decrStackSize(0, this.getInventoryStackLimit());
            }
        } // Are we cooling down?
        else if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN) {
	        // TODO: in the rewrite of this tile entity, it should be ensured that the
	        // random effect is equal on client and server side?
	        degradationExecutor.runRandomEffect(worldObj.isRemote);
	        
	        // Are we done cooling down?
	        if (tick == 0)
	            activateIdleState();
	    }
	}
	
	private void updateEnvironmentalAccumulatorIdle() {
        // Look for items thrown into the beam
        @SuppressWarnings("rawtypes")
        List entityItems = worldObj.getEntitiesWithinAABB(EntityItem.class, 
                AxisAlignedBB.getBoundingBox(
                        this.xCoord, this.yCoord + WEATHER_CONTAINER_MIN_DROP_HEIGHT, this.zCoord, 
                        this.xCoord + 1.0, this.yCoord + WEATHER_CONTAINER_MAX_DROP_HEIGHT, this.zCoord + 1.0)
                );
        
        // Get all environmental accumulator recipes
        Map<CustomRecipe, CustomRecipeResult> recipes = CustomRecipeRegistry.getRecipesForFactory(EnvironmentalAccumulator.getInstance());
        
        // Loop over all recipes until we find an item dropped in the accumulator that matches a recipe
        for (Entry<CustomRecipe, CustomRecipeResult> entry : recipes.entrySet()) {
            EnvironmentalAccumulatorRecipe recipe = (EnvironmentalAccumulatorRecipe) entry.getKey();
            ItemStack recipeStack = recipe.getItemStack();
            WeatherType weatherType = recipe.getWeatherType();
            
            // Loop over all dropped items
            for (Object obj : entityItems) {
                EntityItem entityItem = (EntityItem) obj;
                ItemStack stack = entityItem.getEntityItem();
                
                if (recipeStack.getItem() == stack.getItem() 
                        && recipeStack.getItemDamage() == stack.getItemDamage() 
                        && recipeStack.stackSize <= stack.stackSize
                        && (weatherType == null || weatherType.isActive(worldObj))) {
                    
                    // Save the required input items in the inventory
                    ItemStack inputStack = recipeStack.copy();
                    this.setInventorySlotContents(0, inputStack);
                    
                    // Save the recipe
                    this.recipe = recipe;
                    
                    if (!worldObj.isRemote) {
                        decreaseStackSize(entityItem, recipeStack);
                    }
                    
                    activateProcessingItemState();
                    
                    return;
                }
                
            }
        }
	}
	
	private void decreaseStackSize(EntityItem entityItem, ItemStack stack) {
	    entityItem.getEntityItem().stackSize -= stack.stackSize;
	    
	    if (entityItem.getEntityItem().stackSize == 0)
	        entityItem.setDead();
	}
	
	private void dropItemStack() {
	    if (!worldObj.isRemote) {
	        // EntityItem that will contain the dropped itemstack
	        EntityItem entity = new EntityItem(worldObj, this.xCoord, this.yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT, this.zCoord);
	        
	        if (recipe == null) {
	            // No recipe found, throw the item stack in the inventory back
	            // (NOTE: this can be caused because of weather changes)
	            entity.setEntityItemStack(this.getStackInSlot(0));
	        } else {
	            // Recipe found, throw back the result
	            EnvironmentalAccumulatorResult result = (EnvironmentalAccumulatorResult)CustomRecipeRegistry.get(recipe);
	            entity.setEntityItemStack(result.getItemResult().copy());
	            
	            // Change the weather to the resulting weather
	            WeatherType weatherResult = result.getWeatherResult();
	            if (weatherResult != null)
	                weatherResult.activate(worldObj);
	        }
	        
    	    // Drop the items on the ground
    	    worldObj.spawnEntityInWorld(entity);
	    }
	}
	
	private void activateIdleState() {
        tick = 0;
        state = EnvironmentalAccumulator.STATE_IDLE;
        
        if (!worldObj.isRemote)
            sendUpdate();
    }
	
	private void activateProcessingItemState() {
	    // Set the duration for processing the item
	    if (recipe == null)
	        tick = EnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
	    else
	        tick = recipe.getDuration();
	    
	    state = EnvironmentalAccumulator.STATE_PROCESSING_ITEM;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	private void activateFinishedProcessingItemState() {
	    tick = ITEM_MOVE_COOLDOWN_DURATION;
	    state = EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	private void activateCooldownState() {
	    degradation++;
	    degradationExecutor.setTickInterval(DEGRADATION_TICK_INTERVAL / degradation);
	    
	    tick = getMaxCooldownTick();
	    state = EnvironmentalAccumulator.STATE_COOLING_DOWN;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	@Override
	public void onUpdateReceived() {
	    // If we receive an update from the server and our new state is the
	    // finished processing item state, show the corresponding effect
	    if (worldObj.isRemote && state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
	        EnvironmentalAccumulatorResult result = (recipe == null) ? null : (EnvironmentalAccumulatorResult)CustomRecipeRegistry.get(recipe);
	        
	        // Show an effect indicating the item finished processing.
	        IEAProcessingFinishedEffect effect = (result == null) ? null : result.getFinishedProcessingEffect();
	        
	        if (effect == null)    // fall back to default case
	            this.worldObj.playAuxSFX(2002, (int)Math.round(xCoord), (int)Math.round(yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT), (int)Math.round(zCoord), 16428);
	        else
	            effect.executeEffect(this, recipe, result);
	    }
	    
	    // Change the beam colors if we receive an update
	    setBeamColors(state);
	}
	
	/**
	 * Set the beam colors.
	 * @param state The state to base the colors on.
	 */
	public void setBeamColors(int state) {
        if (worldObj.isRemote) { 
    	    setBeamInnerColor(getInnerColorByState(state));
    	    setBeamOuterColor(getOuterColorByState(state));
        }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    
	    degradation = compound.getInteger("degradation");
	    tick = compound.getInteger("tick");
	    state = compound.getInteger("state");
	    
	    String recipeId = compound.getString("recipe");
	    if (recipeId != null)
	        recipe = (EnvironmentalAccumulatorRecipe) CustomRecipeRegistry.get(recipeId);
	    
	    degradationExecutor.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    
	    compound.setInteger("degradation", degradation);
	    compound.setInteger("tick", tick);
	    compound.setInteger("state", state);
	    
	    String recipeId = (recipe == null) ? null : recipe.getNamedId();
	    if (recipeId != null)
	        compound.setString("recipe", recipeId);
	    
	    degradationExecutor.writeToNBT(compound);
	}
    
    @Override
    public float getMaxHealth() {
        if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return getItemMoveDuration();
        
        if (state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM)
            return 0;
        
        return getMaxCooldownTick();
    }

    @Override
    public float getHealth() {
        if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return tick;
        
        if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN)
            return getMaxCooldownTick() - tick;
        
        return getMaxCooldownTick();
    }

	@Override
	public IChatComponent func_145748_c_() {
		String message = StatCollector.translateToLocalFormatted("chat.bossDisplay.charge",
				new Object[]{StatCollector.translateToLocal(
						EnvironmentalAccumulator.getInstance().getUnlocalizedName() + ".name")});
		return new ChatComponentText(message);
	}

    @Override
    public Coordinate getLocation() {
        return new Coordinate(xCoord, yCoord, zCoord);
    }

    @Override
    public int getRadius() {
        return DEGRADATION_RADIUS_BASE + degradation / 10;
    }

    @Override
    public List<Entity> getAreaEntities() {
        return Helpers.getEntitiesInArea(getWorld(), xCoord, yCoord, zCoord, getRadius());
    }

    @Override
    public double getDegradation() {
        return this.degradation;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(i >= getSizeInventory() || i < 0)
            return null;
        return inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return inventory.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return inventory.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        inventory.setInventorySlotContents(i, itemstack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public void openInventory() {
    
    }

    @Override
    public void closeInventory() {
        
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }
}
