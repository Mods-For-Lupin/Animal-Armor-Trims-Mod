package io.github.jason13official.animal_armor_trims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.animal_armor_trims.impl.client.api.renderer.entity.state.HorseArmorRenderStateAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class FabricLivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
    extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

  protected FabricLivingEntityRendererMixin(Context context) {
    super(context);
  }

  @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V")
  private void animal_armor_trims$extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {

    if (!(entity instanceof Horse horse) || !(state instanceof HorseRenderState horseRenderState)) return;

    HolderLookup.RegistryLookup<TrimPattern> trimPatterns = entity.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);
    HolderLookup.RegistryLookup<TrimMaterial> trimMaterials = entity.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL);
    HolderLookup.RegistryLookup<Enchantment> enchantments = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
    ArmorTrim flowTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.FLOW));
    ArmorTrim boltTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.BOLT));


    HorseArmorRenderStateAccessor armorAccessor = (HorseArmorRenderStateAccessor) horseRenderState;

    if (horse.getBodyArmorItem().has(DataComponents.TRIM) && horse.getBodyArmorItem().has(DataComponents.PROVIDES_TRIM_MATERIAL) ) {

      // get trim from bodyArmor or default to quartz material + sentry pattern
      armorAccessor.animal_armor_trims$setArmorTrim(horse.getBodyArmorItem().getOrDefault(DataComponents.TRIM, new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.QUARTZ), trimPatterns.getOrThrow(TrimPatterns.SENTRY))));
    }
  }

  @Inject(at = @At("TAIL"), method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V")
  private void animal_armor_trims$submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {

    if (!(state instanceof HorseRenderState horseRenderState)) return;

    HorseArmorRenderStateAccessor armorAccessor = (HorseArmorRenderStateAccessor) horseRenderState;
  }
}
