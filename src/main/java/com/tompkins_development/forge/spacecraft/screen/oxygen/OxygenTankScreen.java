package com.tompkins_development.forge.spacecraft.screen.oxygen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.screen.renderer.InfoAreaType;
import com.tompkins_development.forge.spacecraft.screen.renderer.OxygenInfoArea;
import com.tompkins_development.forge.spacecraft.util.MouseUtil;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class OxygenTankScreen extends AbstractContainerScreen<OxygenTankMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SpacecraftMod.MOD_ID, "textures/gui/oxygen_tank.png");
    private OxygenInfoArea oxygenInfoArea;
    private final int[] oxygenInfoAreaXYWH = {8,53, 159, 20};

    public OxygenTankScreen(OxygenTankMenu menu, Inventory inventory, Component component) {
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

        oxygenInfoArea = new OxygenInfoArea(InfoAreaType.LEFT, x+oxygenInfoAreaXYWH[0], y+oxygenInfoAreaXYWH[1], menu.blockEntity.getOxygenStorage(), oxygenInfoAreaXYWH[2], oxygenInfoAreaXYWH[3]);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderOxygenAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
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
}