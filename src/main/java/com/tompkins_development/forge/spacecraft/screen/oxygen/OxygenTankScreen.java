package com.tompkins_development.forge.spacecraft.screen.oxygen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.block.OxygenTankBlock;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.networking.packet.TankStateSyncC2SPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.TankStateUpdateCableC2SPacket;
import com.tompkins_development.forge.spacecraft.screen.components.DirectionButton;
import com.tompkins_development.forge.spacecraft.screen.renderer.InfoAreaType;
import com.tompkins_development.forge.spacecraft.screen.renderer.OxygenInfoArea;
import com.tompkins_development.forge.spacecraft.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OxygenTankScreen extends AbstractContainerScreen<OxygenTankMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SpacecraftMod.MOD_ID, "textures/gui/oxygen_tank.png");
    private OxygenInfoArea oxygenInfoArea;
    private final int[] oxygenInfoAreaXYWH = {8,53, 159, 20};
    private List<DirectionButton> inputButtons;
    private List<DirectionButton> outputButtons;

    public OxygenTankScreen(OxygenTankMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        createButtons();
        renderButtons();
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        oxygenInfoArea = new OxygenInfoArea(InfoAreaType.LEFT, x+oxygenInfoAreaXYWH[0], y+oxygenInfoAreaXYWH[1], menu.blockEntity.getOxygenStorage(), oxygenInfoAreaXYWH[2], oxygenInfoAreaXYWH[3]);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderOxygenAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);

        drawStringNoShadow(pPoseStack, Minecraft.getInstance().font, Component.literal("Extract"), 5, 10, Color.WHITE.getRGB());
        drawStringNoShadow(pPoseStack, Minecraft.getInstance().font, Component.literal("Inject"), 45, 10, Color.WHITE.getRGB());
    }


    private void renderOxygenAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, oxygenInfoAreaXYWH[0], oxygenInfoAreaXYWH[1], oxygenInfoAreaXYWH[2], oxygenInfoAreaXYWH[3])) {
            renderTooltip(pPoseStack, oxygenInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }



    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        oxygenInfoArea.draw(pPoseStack);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    private void createButtons() {
        int height = 18;
        int width = 15;
        int offset = 290;
        int x = this.leftPos-110;
        inputButtons = new ArrayList<>();
        outputButtons = new ArrayList<>();

        int index = 0;
        for(Direction direction : Direction.values()) {
            outputButtons.add(new DirectionButton(x, offset+(height*index), width, height, direction, (button) -> {
                resetColor(false);
                resetActive(false);
                button.setFGColor(Color.GREEN.getRGB());
                disableSimilar(direction, false);
                ModMessages.sendToServer(new TankStateSyncC2SPacket(button.getDirection(), false, menu.blockEntity.getBlockPos()));
                menu.blockEntity.getLevel().setBlock(menu.getBlockEntity().getBlockPos(), menu.blockEntity.getBlockState().setValue(OxygenTankBlock.outputDirection, button.getDirection()), 2);
                ModMessages.sendToServer(new TankStateUpdateCableC2SPacket(menu.blockEntity.getBlockPos()));
            }));
            inputButtons.add(new DirectionButton(x+(width+3), offset+(height*index), width, height, direction, (button) -> {
                resetColor(true);
                resetActive(true);
                button.setFGColor(Color.GREEN.getRGB());
                disableSimilar(direction, true);
                ModMessages.sendToServer(new TankStateSyncC2SPacket(button.getDirection(), true, menu.blockEntity.getBlockPos()));
                menu.blockEntity.getLevel().setBlock(menu.getBlockEntity().getBlockPos(), menu.blockEntity.getBlockState().setValue(OxygenTankBlock.inputDirection, button.getDirection()), 2);
                ModMessages.sendToServer(new TankStateUpdateCableC2SPacket(menu.blockEntity.getBlockPos()));
            }));
            index++;
        }
    }

    private void renderButtons() {
        for(DirectionButton btn : inputButtons)
            this.addRenderableWidget(btn);
        for(DirectionButton btn : outputButtons)
            this.addRenderableWidget(btn);
    }

    private void resetActive(boolean input) {
        if(!input) {
            for (DirectionButton btn : inputButtons) {
                btn.active = true;
            }
        } else {
            for (DirectionButton btn : outputButtons) {
                btn.active = true;
            }
        }
    }

    private void resetColor(boolean input) {
        if(input) {
            for (DirectionButton btn : inputButtons) {
                btn.setFGColor(Color.WHITE.getRGB());
            }
        } else {
            for (DirectionButton btn : outputButtons) {
                btn.setFGColor(Color.WHITE.getRGB());
            }
        }
    }

    private void disableSimilar(Direction direction, boolean input) {
        if(!input) {
            for (DirectionButton btn : inputButtons)
                if (btn.getDirection() == direction)
                    btn.active = false;
        }
        else {
            for (DirectionButton btn : outputButtons)
                if (btn.getDirection() == direction)
                    btn.active = false;
        }

    }

    private void drawStringNoShadow(PoseStack matrixStack, Font fontRenderer, Component text, int x, int y, int color) {
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        fontRenderer.draw(matrixStack, text, x, y, color);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }


//    public ImageButton(int x, int y, int width, int height, int textureX, int textureY, int textureWidth, ResourceLocation texture, Button.OnPress onPress) {
//        // ...
//    }
}