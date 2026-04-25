package io.github.jason13official.animal_armor_trims;

import net.minecraft.resources.Identifier;

public class AnimalArmorTrims {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}