package io.github.jason13official.animal_armor_trims;

import io.github.jason13official.animal_armor_trims.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class AnimalArmorTrimsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    AnimalArmorTrimsClient.init();


    if (!Services.PLATFORM.isDevelopmentEnvironment()) {
      return;
    }

    // dev environment events for debugging

    ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> {

      if (entity != Minecraft.getInstance().player) {
        return;
      }

      RegistryAccess access = level.registryAccess();
      access.lookup(Registries.TRIM_PATTERN).ifPresent(patterns -> {
        access.lookup(Registries.TRIM_MATERIAL).ifPresent(materials -> {

          patterns.forEach(pattern -> {
            materials.forEach(material -> {

              Identifier patternId = pattern.assetId();
              String materialSuffix = material.assets().base().suffix();

              String possibleLocation = "textures/trims/entity/horse_body/pattern_material.png";
              possibleLocation= possibleLocation.replace("pattern", patternId.getPath());
              possibleLocation= possibleLocation.replace("material", materialSuffix);

              Identifier possibleHorseTextureId = Identifier.withDefaultNamespace(possibleLocation);


              Constants.LOG.info("Trying p: {} m: {}", patternId, materialSuffix);
              if (Minecraft.getInstance().getResourceManager().getResource(possibleHorseTextureId).isEmpty()) {
                throw new IllegalStateException("Failed to build a texture from " + possibleLocation);
              }
            });
          });
        });
      });
    });
  }
}
