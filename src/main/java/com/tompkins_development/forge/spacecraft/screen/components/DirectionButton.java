package com.tompkins_development.forge.spacecraft.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Locale;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class DirectionButton extends AbstractButton {

    private final DirectionButton.OnPress press;
    private Direction direction;

    public DirectionButton(int x, int y, int width, int height, Direction direction, DirectionButton.OnPress press) {
        super(x, y, width, height, Component.literal(direction.getName().substring(0,1).toLowerCase(Locale.ROOT)));
        this.press = press;
        this.direction = direction;
    }


    @Override
    public void onPress() {
        this.press.onPress(this);
        this.setFocused(false);
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float delta)  {
        if(active)
            fill(matrix, getX(), getY(),getX()+getWidth(), getY()+getHeight(), Color.GREEN.getRGB());
        super.render(matrix, mouseX, mouseY, delta);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Direction getDirection() {
        return direction;
    }

    @OnlyIn(Dist.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(DirectionButton directionButton);
    }

}
