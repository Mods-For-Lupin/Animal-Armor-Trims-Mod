package io.github.jason13official.animal_armor_trims;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;

public class AnimalArmorTrimsClient {

  public static Cache<ArmorTrim, Function<MultiBufferSource, VertexConsumer>> HORSE_CACHE = CacheBuilder.newBuilder().build();
  public static Cache<ArmorTrim, Function<MultiBufferSource, VertexConsumer>> WOLF_CACHE = CacheBuilder.newBuilder().build();
  public static Set<ResourceLocation> KNOWN_MISSING = new HashSet<>();

  public static void init() {
  }

  public static void onClientJoinLevel() {
    Minecraft mc = Minecraft.getInstance();

    if (mc.level == null) return;

    Registry<TrimMaterial> materials = mc.level.registryAccess().registryOrThrow(Registries.TRIM_MATERIAL);
    Registry<TrimPattern> patterns = mc.level.registryAccess().registryOrThrow(Registries.TRIM_PATTERN);

    KNOWN_MISSING.clear();
    HORSE_CACHE.invalidateAll();
    WOLF_CACHE.invalidateAll();

    materials.forEach(trimMaterial -> {

      var rl = materials.getKey(trimMaterial);

      if (rl != null) {
        materials.getHolder(rl).ifPresent(materialReference -> {

          patterns.forEach(trimPattern -> {

            var rl2 = patterns.getKey(trimPattern);

            if (rl2 != null) {
              patterns.getHolder(rl2).ifPresent(patternReference -> {

                ResourceLocation horseTex = horseTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());
                if (mc.getResourceManager().getResource(horseTex).isPresent()) {
                  HORSE_CACHE.put(new ArmorTrim(materialReference, patternReference), buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(horseTex)));
                } else {
                  KNOWN_MISSING.add(horseTex);
                }

                ResourceLocation wolfTex = wolfTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());
                if (mc.getResourceManager().getResource(wolfTex).isPresent()) {
                  WOLF_CACHE.put(new ArmorTrim(materialReference, patternReference), buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(wolfTex)));
                } else {
                  KNOWN_MISSING.add(wolfTex);
                }
              });
            }
          });
        });
      }
    });
  }

  public static ResourceLocation horseTextureLocation(String material, String pattern) {

    ResourceLocation returned;

    try {
      returned = ResourceLocation.withDefaultNamespace("textures/trims/entity/horse_body/pattern_material.png".replace("material", material).replace("pattern", pattern));
    }
    catch (ResourceLocationException e) {
      returned = ResourceLocation.withDefaultNamespace("textures/trims/entity/horse_body/coast_diamond.png");
    }

    return returned;
  }

  public static ResourceLocation wolfTextureLocation(String material, String pattern) {

    ResourceLocation returned;

    try {
      returned = ResourceLocation.withDefaultNamespace("textures/trims/entity/wolf_body/pattern_material.png".replace("material", material).replace("pattern", pattern));
    }
    catch (ResourceLocationException e) {
      returned = ResourceLocation.withDefaultNamespace("textures/trims/entity/wolf_body/coast_diamond.png");
    }

    return returned;
  }
}