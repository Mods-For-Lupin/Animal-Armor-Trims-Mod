package io.github.jason13official.animal_armor_trims;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.client.Minecraft;

public class AnimalArmorTrimsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    AnimalArmorTrimsClient.init();

    ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
      if (Minecraft.getInstance().player != null && entity == Minecraft.getInstance().player) {
        AnimalArmorTrimsClient.onClientJoinLevel();
      }
    });
  }
}
