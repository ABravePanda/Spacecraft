package com.tompkins_development.forge.spacecraft.item.custom;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OxygenTankItem extends Item {

    public OxygenTankItem(int durability) {
        super(new Properties().stacksTo(1).durability(durability));
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int oxygen = getOxygen(stack);
        double percentage = (double) oxygen / getCapacity(stack);
        int barWidth = (int) Math.ceil(percentage * 13); // 13 is the default width of the durability bar
        return barWidth;
    }

    public void setOxygen(ItemStack stack, int oxygen) {
        stack.getOrCreateTag().putInt("oxygen", Math.min(oxygen, getCapacity(stack)));
    }

    public void addOxygen(ItemStack stack, int oxygenToAdd) {
        int oxygen = getOxygen(stack);
        if(oxygen + oxygenToAdd > getCapacity(stack)) return;
        setOxygen(stack, oxygen + oxygenToAdd);
    }

    public int getOxygen(ItemStack stack) {
        return stack.getOrCreateTag().getInt("oxygen");
    }


    public int getCapacity(ItemStack stack) {
        return stack.getMaxDamage();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int oxygen = getOxygen(stack);
        list.add(Component.translatable("item." + SpacecraftMod.MOD_ID + ".oxygen_tank.lore", Component.literal(oxygen + ""), Component.literal(getCapacity(stack) + "")));
        super.appendHoverText(stack, level, list, flag);
    }


    @Override
    public int getBarColor(ItemStack stack) {
        int oxygen = getOxygen(stack);
        float f = Math.max(0.0F, (getCapacity(stack) - (float) oxygen) / getCapacity(stack));
        return Mth.hsvToRgb((1.0F - f) / 3.0F, 1.0F, 1.0F);
    }
}
