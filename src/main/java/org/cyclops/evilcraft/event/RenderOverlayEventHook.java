package org.cyclops.evilcraft.event;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.item.ItemBroomConfig;

/**
 * @author rubensworks
 */
public class RenderOverlayEventHook {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 51;
    protected static final ResourceLocation BROOM_OVERLAY = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/overlay.png");
    protected static final ResourceLocation BLOOD_OVERLAY = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/overlay.png");

    private int filledHeight = -1;

    @SubscribeEvent
    public void onRenderOverlayEvent(RenderGuiEvent.Post event) {
        renderBroomOverlay(event);
        renderBloodOverlay(event);
    }

    public void renderBroomOverlay(RenderGuiEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player.getVehicle() instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) player.getVehicle();
            ItemStack broomStack = broom.getBroomStack();
            Window resolution = Minecraft.getInstance().getWindow();
            int height = 21;
            int width = 21;
            RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                    Mth.clamp(ItemBroomConfig.guiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
            int x = overlayPosition.getX(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetX;
            int y = overlayPosition.getY(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetY;

            // Render slot
            event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, BROOM_OVERLAY, x, y, 11, 0, 24, 24, 256, 256);

            // Render item
            event.getGuiGraphics().renderItem(broomStack, x + 3, y + 3);
            event.getGuiGraphics().renderItemDecorations(
                    Minecraft.getInstance().gui.getFont(), broomStack, x + 3, y + 3, "");
        }
    }

    public void renderBloodOverlay(RenderGuiEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (GeneralConfig.bloodGuiOverlay) {
            if (filledHeight < 0 || IModHelpers.get().getWorldHelpers().efficientTick(player.level(), 50)) {
                Wrapper<Integer> amount = new Wrapper<Integer>(0);
                Wrapper<Integer> capacity = new Wrapper<Integer>(1);
                PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
                while (it.hasNext()) {
                    ItemStack itemStack = it.next();
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
                    if (!itemStack.isEmpty() && fluidHandler != null) {
                        FluidStack fluidStack = IModHelpersNeoForge.get().getFluidHelpers().getFluid(fluidHandler);
                        if (!fluidStack.isEmpty() && BloodFluidConverter.getInstance().canConvert(fluidStack.getFluid())) {
                            amount.set(amount.get() + fluidStack.getAmount());
                        }
                        if (fluidStack.isEmpty() || BloodFluidConverter.getInstance().canConvert(fluidStack.getFluid())) {
                            capacity.set(capacity.get() + IModHelpersNeoForge.get().getFluidHelpers().getCapacity(fluidHandler));
                        }
                    }
                }
                filledHeight = (int) Math.floor(((float) HEIGHT) * ((float) amount.get() / (float) capacity.get()));
            }

            if (filledHeight > 0) {
                RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                        Mth.clamp(GeneralConfig.bloodGuiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
                Window resolution = Minecraft.getInstance().getWindow();
                int x = overlayPosition.getX(resolution, WIDTH, HEIGHT) + GeneralConfig.bloodGuiOverlayPositionOffsetX;
                int y = overlayPosition.getY(resolution, WIDTH, HEIGHT) + GeneralConfig.bloodGuiOverlayPositionOffsetY;

                event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, BLOOD_OVERLAY, x, y, 0, 0, WIDTH, HEIGHT, 256, 256);
                event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, BLOOD_OVERLAY, x, y + (HEIGHT - filledHeight), WIDTH, HEIGHT - filledHeight, WIDTH, filledHeight, 256, 256);
            }
        }
    }

    public static enum OverlayPosition {

        NE {
            @Override
            public int getX(Window resolution, int width, int height) {
                return resolution.getGuiScaledWidth() - width;
            }

            @Override
            public int getY(Window resolution, int width, int height) {
                return 0;
            }
        },
        SE {
            @Override
            public int getX(Window resolution, int width, int height) {
                return resolution.getGuiScaledWidth() - width;
            }

            @Override
            public int getY(Window resolution, int width, int height) {
                return resolution.getGuiScaledHeight() - height;
            }
        },
        SW {
            @Override
            public int getX(Window resolution, int width, int height) {
                return 0;
            }

            @Override
            public int getY(Window resolution, int width, int height) {
                return resolution.getGuiScaledHeight() - height;
            }
        },
        NW {
            @Override
            public int getX(Window resolution, int width, int height) {
                return 0;
            }

            @Override
            public int getY(Window resolution, int width, int height) {
                return 0;
            }
        };

        public abstract int getX(Window resolution, int width, int height);
        public abstract int getY(Window resolution, int width, int height);

    }
}
