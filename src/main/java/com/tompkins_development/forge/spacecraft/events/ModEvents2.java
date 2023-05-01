package com.tompkins_development.forge.spacecraft.events;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.entity.ModEntities;
import com.tompkins_development.forge.spacecraft.entity.custom.MoonWolfEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpacecraftMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents2 {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOON_WOLF.get(), MoonWolfEntity.setAttributes());
    }
}
