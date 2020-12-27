package org.cyclops.evilcraft.event;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.lwjgl.opengl.GL11;

/**
 * @author rubensworks
 */
public class RenderOverlayEventHook {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 51;
    protected static final ResourceLocation BLOOD_OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/gui/overlay.png");

    private int filledHeight = -1;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderOverlayEvent(RenderGameOverlayEvent.Post event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (GeneralConfig.bloodGuiOverlay && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (filledHeight < 0 || WorldHelpers.efficientTick(player.world, 50)) {
                Wrapper<Integer> amount = new Wrapper<Integer>(0);
                Wrapper<Integer> capacity = new Wrapper<Integer>(1);
                PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
                while (it.hasNext()) {
                    ItemStack itemStack = it.next();
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
                    if (!itemStack.isEmpty() && fluidHandler != null) {
                        FluidStack fluidStack = FluidHelpers.getFluid(fluidHandler);
                        if (!fluidStack.isEmpty() && BloodFluidConverter.getInstance().canConvert(fluidStack.getFluid())) {
                            amount.set(amount.get() + fluidStack.getAmount());
                        }
                        if (fluidStack.isEmpty() || BloodFluidConverter.getInstance().canConvert(fluidStack.getFluid())) {
                            capacity.set(capacity.get() + FluidHelpers.getCapacity(fluidHandler));
                        }
                    }
                }
                filledHeight = (int) Math.floor(((float) HEIGHT) * ((float) amount.get() / (float) capacity.get()));
            }

            if (filledHeight > 0) {
                RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                        MathHelper.clamp(GeneralConfig.bloodGuiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
                MainWindow resolution = event.getWindow();
                int x = overlayPosition.getX(resolution, WIDTH, HEIGHT) + GeneralConfig.bloodGuiOverlayPositionOffsetX;
                int y = overlayPosition.getY(resolution, WIDTH, HEIGHT) + GeneralConfig.bloodGuiOverlayPositionOffsetY;

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelpers.bindTexture(BLOOD_OVERLAY);

                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), x, y, 0, 0, WIDTH, HEIGHT);
                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), x, y + (HEIGHT - filledHeight), WIDTH, HEIGHT - filledHeight, WIDTH, filledHeight);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public static enum OverlayPosition {

        NE {
            @Override
            public int getX(MainWindow resolution, int width, int height) {
                return resolution.getScaledWidth() - width;
            }

            @Override
            public int getY(MainWindow resolution, int width, int height) {
                return 0;
            }
        },
        SE {
            @Override
            public int getX(MainWindow resolution, int width, int height) {
                return resolution.getScaledWidth() - width;
            }

            @Override
            public int getY(MainWindow resolution, int width, int height) {
                return resolution.getScaledHeight() - height;
            }
        },
        SW {
            @Override
            public int getX(MainWindow resolution, int width, int height) {
                return 0;
            }

            @Override
            public int getY(MainWindow resolution, int width, int height) {
                return resolution.getScaledHeight() - height;
            }
        },
        NW {
            @Override
            public int getX(MainWindow resolution, int width, int height) {
                return 0;
            }

            @Override
            public int getY(MainWindow resolution, int width, int height) {
                return 0;
            }
        };

        public abstract int getX(MainWindow resolution, int width, int height);
        public abstract int getY(MainWindow resolution, int width, int height);

    }
}
