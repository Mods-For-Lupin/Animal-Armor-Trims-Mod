package io.github.jason13official.animal_armor_trims;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

public class AnimalArmorTrimsClient {

  public static Cache<ArmorTrim, Function<MultiBufferSource, VertexConsumer>> HORSE_CACHE = CacheBuilder.newBuilder().build();
  public static Cache<ArmorTrim, Function<MultiBufferSource, VertexConsumer>> WOLF_CACHE = CacheBuilder.newBuilder().build();
  public static final ResourceLocation HORSE_FALLBACK_RL = horseTextureLocation("diamond", "coast");
  public static final ResourceLocation WOLF_FALLBACK_RL = wolfTextureLocation("diamond", "coast");

  public static void init() {
  }

  public static void onClientJoinLevel() {
    Minecraft mc = Minecraft.getInstance();

    if (mc.level == null) return;

    Registry<TrimMaterial> materials = mc.level.registryAccess().registryOrThrow(Registries.TRIM_MATERIAL);
    Registry<TrimPattern> patterns = mc.level.registryAccess().registryOrThrow(Registries.TRIM_PATTERN);

    HORSE_CACHE.invalidateAll();
    WOLF_CACHE.invalidateAll();

    materials.forEach(trimMaterial -> {

      ResourceLocation materialLocation = materials.getKey(trimMaterial);

      if (materialLocation != null) {
        materials.getOptional(materialLocation).ifPresent(materialReference -> {

          patterns.forEach(trimPattern -> {

            ResourceLocation patternLocation = patterns.getKey(trimPattern);

            if (patternLocation != null) {
              patterns.getOptional(patternLocation).ifPresent(patternReference -> {


                ResourceKey<TrimMaterial> matKey = ResourceKey.create(Registries.TRIM_MATERIAL, materialLocation);
                ResourceKey<TrimPattern> patKey = ResourceKey.create(Registries.TRIM_PATTERN, patternLocation);
                Optional<Reference<TrimMaterial>> optionalMaterial = materials.getHolder(matKey);
                Optional<Reference<TrimPattern>> optionalPattern = patterns.getHolder(patKey);

                if (optionalMaterial.isPresent() && optionalPattern.isPresent()) {
                  ArmorTrim armorTrim = new ArmorTrim(optionalMaterial.get(), optionalPattern.get());

                  ResourceLocation horseTex = horseTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());

                  System.out.println("Attempting to create full location of " + horseTex.toString());
                  Constants.LOG.info("Attempting to create full location of {}", horseTex.toString());

                  System.out.println("resource manager located? " + String.valueOf(mc.getResourceManager().getResource(horseTex).isPresent()));
                  Constants.LOG.info("resource manager located? {}", String.valueOf(mc.getResourceManager().getResource(horseTex).isPresent()));

                  ResourceLocation effectiveHorseTex = mc.getResourceManager().getResource(horseTex).isPresent() ? horseTex : horseTextureLocation(trimMaterial.assetName(), "coast");

                  System.out.println("effective texture? " + effectiveHorseTex.toString());
                  Constants.LOG.info("effective texture? {}", effectiveHorseTex.toString());

                  HORSE_CACHE.put(armorTrim, buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(effectiveHorseTex)));

                  ResourceLocation wolfTex = wolfTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());
                  ResourceLocation effectiveWolfTex = mc.getResourceManager().getResource(wolfTex).isPresent() ? wolfTex : wolfTextureLocation(trimMaterial.assetName(), "coast");
                  WOLF_CACHE.put(armorTrim, buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(effectiveWolfTex)));
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
      returned = new ResourceLocation("textures/trims/entity/horse_body/pattern_material.png".replace("material", material).replace("pattern", pattern));
    }
    catch (ResourceLocationException e) {
      returned = new ResourceLocation("textures/trims/entity/horse_body/coast_diamond.png");
    }

    return returned;
  }

  public static ResourceLocation wolfTextureLocation(String material, String pattern) {

    ResourceLocation returned;

    try {
      returned = new ResourceLocation("textures/trims/entity/wolf_body/pattern_material.png".replace("material", material).replace("pattern", pattern));
    }
    catch (ResourceLocationException e) {
      returned = new ResourceLocation("textures/trims/entity/wolf_body/coast_diamond.png");
    }

    return returned;
  }
}