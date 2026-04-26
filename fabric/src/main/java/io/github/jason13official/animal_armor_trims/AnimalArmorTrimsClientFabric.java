package io.github.jason13official.animal_armor_trims;

import net.fabricmc.api.ClientModInitializer;

public class AnimalArmorTrimsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    AnimalArmorTrimsClient.init();
  }
}
