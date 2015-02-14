package evilcraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.EvilCraft;
import evilcraft.client.particle.EntityBlurFX;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.core.tileentity.NBTPersist;
import evilcraft.item.OriginsOfDarkness;
import evilcraft.item.OriginsOfDarknessConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;
import java.util.Random;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileSpiritPortal extends EvilCraftTileEntity {

    @NBTPersist private Float progress = 0f;

	@Override
	public void updateTileEntity() {
		super.updateTileEntity();
        progress += 0.005f;
        if(progress > 1) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
        if(worldObj.isRemote) {
            int progressModifier = (int) (getProgress() * 40f) + 1;
            if(worldObj.rand.nextInt(5) == 0) {
                EvilCraft.proxy.playSound(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F,
                        "vengeanceBeam", 0.5F + worldObj.rand.nextFloat() * 0.2F, 1.0F);
            }
            for(int i = 0; i < worldObj.rand.nextInt(progressModifier); i++) {
                showNewBlurParticle();
            }
        }

        // transform book if thrown in.
        if(!worldObj.isRemote && Configs.isEnabled(OriginsOfDarknessConfig.class)) {
            for (EntityItem entityItem : (List<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class,
                    AxisAlignedBB.getBoundingBox(
                            this.xCoord - 0.5D, this.yCoord - 0.5D, this.zCoord - 0.5D,
                            this.xCoord + 1.5D, this.yCoord + 1.5D, this.zCoord + 1.5D))) {
                if (entityItem.getEntityItem().getItem() instanceof ItemBook) {
                    Entity entity = new EntityItem(worldObj, entityItem.posX, entityItem.posY, entityItem.posZ,
                            new ItemStack(OriginsOfDarkness.getInstance(), entityItem.getEntityItem().stackSize));
                    entity.setVelocity(entityItem.motionX, entityItem.motionY, entityItem.motionZ);
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

        EntityBlurFX blur = new EntityBlurFX(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, scale,
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
