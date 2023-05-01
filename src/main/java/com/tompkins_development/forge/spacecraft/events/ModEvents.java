package com.tompkins_development.forge.spacecraft.events;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.entity.ModEntities;
import com.tompkins_development.forge.spacecraft.entity.custom.MoonWolfEntity;
import com.tompkins_development.forge.spacecraft.oxygen.PlayerOxygen;
import com.tompkins_development.forge.spacecraft.oxygen.PlayerOxygenProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpacecraftMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerOxygenProvider.PLAYER_OXYGEN).isPresent()) {
                event.addCapability(new ResourceLocation(SpacecraftMod.MOD_ID, "properties"), new PlayerOxygenProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerOxygenProvider.PLAYER_OXYGEN).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerOxygenProvider.PLAYER_OXYGEN).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerOxygen.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerOxygenProvider.PLAYER_OXYGEN).ifPresent(thirst -> {
                if(thirst.getOxygen() == 0) {
                    //event.player.hurt(null, 1);
                    //event.player.sendSystemMessage(Component.literal("Your Oxygen is at 0"));
                    thirst.addOxygen(5);
                }
                if(thirst.getOxygen() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                    thirst.subOxygen(1);
                    //event.player.sendSystemMessage(Component.literal("Subtracted Thirst : " + thirst.getOxygen()));
                }
            });
        }
    }
}
