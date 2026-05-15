package io.github.jason13official.animal_armor_trims;

import io.github.jason13official.animal_armor_trims.platform.Services;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class AnimalArmorTrimsClientNeoForge {

  public AnimalArmorTrimsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> AnimalArmorTrimsClient.init());

    if (!Services.PLATFORM.isDevelopmentEnvironment()) {
      return;
    }

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {

      if (event.getEntity() != Minecraft.getInstance().player) {
        return;
      }

      Level level = event.getLevel();

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
