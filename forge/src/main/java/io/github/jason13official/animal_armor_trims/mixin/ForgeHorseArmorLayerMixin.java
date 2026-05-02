package io.github.jason13official.animal_armor_trims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.animal_armor_trims.AnimalArmorTrimsClient;
import java.util.concurrent.ExecutionException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class ForgeHorseArmorLayerMixin {

  @Shadow
  @Final
  private HorseModel<Horse> model;

  @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At(value = "RETURN"))
  private void animalArmorTrims$onRenderHorseArmorLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Horse horse, float limbSwing, float limbSwingAmount, float partialTicks,
      float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {

    ItemStack armor = horse.getArmor();
    if (!(armor.getItem() instanceof HorseArmorItem) || !(armor.is(ItemTags.TRIMMABLE_ARMOR) && armor.getTag() != null && armor.getTag().contains("Trim"))) {
      return;
    }

    // we can assume?? level is not null because we are rendering in the world
    ArmorTrim trim = ArmorTrim.getTrim(Minecraft.getInstance().level.registryAccess(), armor).get(); //armor.get(DataComponents.TRIM);

    if (trim == null) {
      return;
    }

    try {

      String material = trim.material().value().assetName();
      String pattern = trim.pattern().value().assetId().getPath();
      String cacheKey = pattern + "_" + material;
      var rl = AnimalArmorTrimsClient.horseTextureLocation(material, pattern);
      var mc = Minecraft.getInstance();
      var effectiveRl = mc.getResourceManager().getResource(rl).isPresent() ? rl : AnimalArmorTrimsClient.horseTextureLocation(material, "coast");
      VertexConsumer vertexConsumer = AnimalArmorTrimsClient.HORSE_CACHE.get(cacheKey, () -> buf -> buf.getBuffer(RenderType.armorCutoutNoCull(effectiveRl))).apply(buffer);

      if (vertexConsumer != null) {
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
      }
    } catch (ExecutionException e) {
      System.out.println("failed to render horse armor trim...");
    }
  }
}
