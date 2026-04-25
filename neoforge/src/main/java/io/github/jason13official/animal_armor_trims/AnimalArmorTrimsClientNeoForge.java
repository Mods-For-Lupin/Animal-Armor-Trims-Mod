package io.github.jason13official.animal_armor_trims;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class AnimalArmorTrimsClientNeoForge {

  public AnimalArmorTrimsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> AnimalArmorTrimsClient.init());
  }
}
