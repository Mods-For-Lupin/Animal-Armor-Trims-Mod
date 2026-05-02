package io.github.jason13official.animal_armor_trims;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

public class AnimalArmorTrimsClient {

  public static Cache<String, Function<MultiBufferSource, VertexConsumer>> HORSE_CACHE = CacheBuilder.newBuilder().build();
  public static Cache<String, Function<MultiBufferSource, VertexConsumer>> WOLF_CACHE = CacheBuilder.newBuilder().build();
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
      patterns.forEach(trimPattern -> {
        String cacheKey = trimPattern.assetId().getPath() + "_" + trimMaterial.assetName();

        ResourceLocation horseTex = horseTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());
        Constants.LOG.info("Attempting to create full location of {}", horseTex);
        boolean horseFound = mc.getResourceManager().getResource(horseTex).isPresent();
        Constants.LOG.info("resource manager located? {}", horseFound);
        ResourceLocation effectiveHorseTex = horseFound ? horseTex : horseTextureLocation(trimMaterial.assetName(), "coast");
        Constants.LOG.info("effective texture? {}", effectiveHorseTex);
        HORSE_CACHE.put(cacheKey, buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(effectiveHorseTex)));

        ResourceLocation wolfTex = wolfTextureLocation(trimMaterial.assetName(), trimPattern.assetId().getPath());
        ResourceLocation effectiveWolfTex = mc.getResourceManager().getResource(wolfTex).isPresent() ? wolfTex : wolfTextureLocation(trimMaterial.assetName(), "coast");
        WOLF_CACHE.put(cacheKey, buffer -> buffer.getBuffer(RenderType.armorCutoutNoCull(effectiveWolfTex)));
      });
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