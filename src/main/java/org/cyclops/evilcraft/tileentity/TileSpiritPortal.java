package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.client.particle.EntityBlurFX;
import org.cyclops.evilcraft.item.OriginsOfDarkness;
import org.cyclops.evilcraft.item.OriginsOfDarknessConfig;

import java.util.List;
import java.util.Random;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileSpiritPortal extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    private Float progress = 0f;

	@SuppressWarnings("unchecked")
    @Override
	public void updateTileEntity() {
		super.updateTileEntity();
        progress += 0.005f;
        if(progress > 1) {
            worldObj.setBlockToAir(getPos());
        }
        if(worldObj.isRemote) {
            int progressModifier = (int) (getProgress() * 40f) + 1;
            if(worldObj.rand.nextInt(5) == 0) {
                EvilCraft.proxy.playSound(getPos().getX() + 0.5F, getPos().getY() + 0.5F, getPos().getZ() + 0.5F,
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundCategory.AMBIENT,
                        0.5F + worldObj.rand.nextFloat() * 0.2F, 1.0F);
            }
            for(int i = 0; i < worldObj.rand.nextInt(progressModifier); i++) {
                showNewBlurParticle();
            }
        }

        // transform book if thrown in.
        if(!worldObj.isRemote && Configs.isEnabled(OriginsOfDarknessConfig.class)) {
            for (EntityItem entityItem : (List<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class,
                    new AxisAlignedBB(
                            this.getPos().getX() - 0.5D, this.getPos().getY() - 0.5D, this.getPos().getZ() - 0.5D,
                            this.getPos().getX() + 1.5D, this.getPos().getY() + 1.5D, this.getPos().getZ() + 1.5D))) {
                if (entityItem.getEntityItem().getItem() instanceof ItemBook) {
                    Entity entity = new EntityItem(worldObj, entityItem.posX, entityItem.posY, entityItem.posZ,
                            new ItemStack(OriginsOfDarkness.getInstance(), entityItem.getEntityItem().stackSize));
                    entity.motionX = entityItem.motionX;
                    entity.motionY = entityItem.motionY;
                    entity.motionZ = entityItem.motionZ;
                    entityItem.setDead();
                    worldObj.spawnEntityInWorld(entity);
                }
            }
        }
	}

    @SideOnly(Side.CLIENT)
    private void showNewBlurParticle() {
        Random rand = worldObj.rand;
        float scale = 0.6F - rand.nextFloat() * 0.3F;
        float red = rand.nextFloat() * 0.03F + 0.01F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 6.5D + 10D);

        EntityBlurFX blur = new EntityBlurFX(worldObj, getPos().getX() + 0.5F, getPos().getY() + 0.5F, getPos().getZ() + 0.5F, scale,
                rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F,
                red, green, blue, ageMultiplier);
        Minecraft.getMinecraft().effectRenderer.addEffect(blur);
    }

    public float getProgress() {
        return progress;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

}
