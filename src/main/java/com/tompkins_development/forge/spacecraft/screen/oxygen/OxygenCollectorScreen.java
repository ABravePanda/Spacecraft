package com.tompkins_development.forge.spacecraft.screen.oxygen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.screen.renderer.EnergyInfoArea;
import com.tompkins_development.forge.spacecraft.screen.renderer.InfoAreaType;
import com.tompkins_development.forge.spacecraft.screen.renderer.OxygenInfoArea;
import com.tompkins_development.forge.spacecraft.util.MouseUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class OxygenCollectorScreen  extends AbstractContainerScreen<OxygenCollectorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SpacecraftMod.MOD_ID, "textures/gui/oxygen_collector.png");
    private EnergyInfoArea energyInfoArea;
    private OxygenInfoArea oxygenInfoArea;

    public OxygenCollectorScreen(OxygenCollectorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x+93, y+29, menu.blockEntity.getEnergyStorage());
        oxygenInfoArea = new OxygenInfoArea(InfoAreaType.LEFT,x+93, y+19, menu.blockEntity.getOxygenStorage(), 63, 6);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        renderOxygenAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        drawStringNoShadow(pPoseStack, this.font, Component.literal("Energy Input: 20 FE/s"), 19, 53, 0x444444);
        drawStringNoShadow(pPoseStack, this.font, Component.literal("Oxygen Input: " + menu.blockEntity.getOxygenCreationRate(menu.getBlockEntity()) + " O2/s"), 19, 63, 0x444444);
    }

    private void renderOxygenAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 93, 19, 63, 6)) {
            renderTooltip(pPoseStack, oxygenInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 93, 29, 63, 6)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
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

        renderProgressArrow(pPoseStack, x, y);
        energyInfoArea.draw(pPoseStack);
        oxygenInfoArea.draw(pPoseStack);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        //
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

    public void drawStringNoShadow(PoseStack matrixStack, Font fontRenderer, Component text, int x, int y, int color) {
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        fontRenderer.draw(matrixStack, text, x, y, color);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }
}