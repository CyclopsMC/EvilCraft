package evilcraft.tileentity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.core.algorithm.Location;
import evilcraft.core.helper.MathHelpers;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;
import java.util.List;

/**
 * Tile for the {@link evilcraft.block.FireBlaster}.
 *
 * @author rubensworks
 */
public class TileFireBlaster extends EvilCraftTileEntity {

    private int tick = 0;

    public TileFireBlaster() {
        setRotatable(true);
    }

    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        tick = (tick + 1) % 10;

        int redstone = worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord);
        if (redstone > 0) {
            float strength = 0.05F * redstone;
            int blockStrength = redstone;
            float amount = (float) redstone / 11;
            if (worldObj.isRemote) {
                showEffect(amount, strength);
            } else {
                fireDamage(new Location(xCoord, yCoord, zCoord).offset(getRotation().getOpposite()), getRotation().getOpposite(), blockStrength);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void showEffect(float amount, float strength) {
        int actualAmount = MathHelpers.factorToBursts(amount, tick);
        ForgeDirection facing = getRotation().getOpposite();
        for (int i = 0; i < actualAmount; i++) {
            double particleX = xCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleY = yCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleZ = zCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;

            double particleMotionX = -0.01D + (double) facing.offsetX * strength + worldObj.rand.nextFloat() * 0.02F;
            double particleMotionY = -0.01D + (double) facing.offsetY * strength + worldObj.rand.nextFloat() * 0.02F;
            double particleMotionZ = -0.01D + (double) facing.offsetZ * strength + worldObj.rand.nextFloat() * 0.02F;

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityFlameFX(worldObj, particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void fireDamage(ILocation location, ForgeDirection direction, int strength) {
        int[] c = location.getCoordinates();
        if(!worldObj.getBlock(c[0], c[1], c[2]).isNormalCube()) {
            // Entity damage
            List entities = worldObj.getEntitiesWithinAABB(Entity.class,
                    AxisAlignedBB.getBoundingBox(
                            c[0], c[1], c[2],
                            c[0] + 1, c[1] + 1, c[2] + 1)
            );
            if(!entities.isEmpty()) {
                for(Entity entity : (List<Entity>) entities) {
                    //entity.attackEntityFrom(DamageSource.inFire, 0.2F);
                    entity.setFire(strength / 3);
                }
            }
            if (strength > 1) {
                fireDamage(location.offset(direction), direction, strength - 1);
            }
        } else {
            // Block damage
            Block block = worldObj.getBlock(c[0], c[1], c[2]);
            if(block.isFlammable(worldObj, c[0], c[1], c[2], direction.getOpposite())) {
                int[] co = location.offset(direction.getOpposite()).getCoordinates();
                worldObj.setBlock(co[0], co[1], co[2], Blocks.fire); // TODO: sided fire
            }
        }
    }

}
