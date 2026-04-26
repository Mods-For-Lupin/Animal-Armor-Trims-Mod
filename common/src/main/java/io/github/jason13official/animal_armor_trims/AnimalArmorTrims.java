package io.github.jason13official.animal_armor_trims;

import net.minecraft.resources.ResourceLocation;

public class AnimalArmorTrims {

  public static void init() {
  }

  public static ResourceLocation identifier(final String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}