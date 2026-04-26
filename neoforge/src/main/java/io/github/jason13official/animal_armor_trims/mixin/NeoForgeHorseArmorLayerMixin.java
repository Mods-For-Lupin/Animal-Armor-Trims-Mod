package io.github.jason13official.animal_armor_trims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.animal_armor_trims.AnimalArmorTrimsClient;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class NeoForgeHorseArmorLayerMixin {

  @Shadow
  @Final
  private HorseModel<Horse> model;

  @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At(value = "RETURN"))
  private void animalArmorTrims$onRenderHorseArmorLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Horse horse, float limbSwing, float limbSwingAmount, float partialTicks,
      float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {

    ItemStack armor = horse.getBodyArmorItem();
    if (!(armor.getItem() instanceof AnimalArmorItem animalArmor && animalArmor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN)) {
      return;
    }

    ArmorTrim trim = armor.get(DataComponents.TRIM);

    if (trim == null) {
      return;
    }

    try {
      // VertexConsumer vertexConsumer = HorseRenderLayerHelper.createVertexConsumer(trim, buffer);
      var rl = ResourceLocation.tryBuild("minecraft",
          AnimalArmorTrimsClient.horseTextureLocation(trim.material().getRegisteredName().replace("minecraft:", ""), trim.pattern().getRegisteredName().replace("minecraft:", "")).getPath());

      if (rl == null) {
        return;
      }

      // default if invalid
      VertexConsumer vertexConsumer = AnimalArmorTrimsClient.HORSE_CACHE.get(trim, () -> buf -> buf.getBuffer(RenderType.armorCutoutNoCull(rl))).apply(buffer);

      if (vertexConsumer != null) {
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
      }
    } catch (ExecutionException e) {
      System.out.println("failed to render horse armor trim...");
    }
  }
}
