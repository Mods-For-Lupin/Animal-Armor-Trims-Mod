package io.github.jason13official.animal_armor_trims;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class AnimalArmorTrimsClientForge {

  public AnimalArmorTrimsClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> AnimalArmorTrimsClient.init());

    MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {

      Entity entity = event.getEntity();
      if (Minecraft.getInstance().player != null && entity == Minecraft.getInstance().player) {
        AnimalArmorTrimsClient.onClientJoinLevel();
      }
    });
  }
}
