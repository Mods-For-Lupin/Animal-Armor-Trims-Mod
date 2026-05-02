package io.github.jason13official.animal_armor_trims.mixin;

import com.blackgear.vanillabackport.client.level.entities.layer.WolfArmorLayer;
import com.blackgear.vanillabackport.client.util.LazyModel;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.animal_armor_trims.AnimalArmorTrimsClient;
import java.util.concurrent.ExecutionException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfArmorLayer.class)
public class ForgeWolfArmorLayerMixin {

    @Shadow @Final private LazyModel<Wolf, WolfModel<Wolf>> model;

    @Inject(remap = false, method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V", at = @At(value = "RETURN"))
    private void animalArmorTrims$onRenderWolfArmorLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Wolf wolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {

        ItemStack armor = wolf.getItemBySlot(EquipmentSlot.CHEST);
        if (!(armor.getItem() instanceof WolfArmorItem animalArmor)) return;

        ArmorTrim trim = ArmorTrim.getTrim(Minecraft.getInstance().level.registryAccess(), armor).get(); //armor.get(DataComponents.TRIM);

        if (trim == null) {
            return;
        }

        try {

            String material = trim.material().value().assetName();
            String pattern = trim.pattern().value().assetId().getPath();
            String cacheKey = pattern + "_" + material;
            var rl = AnimalArmorTrimsClient.wolfTextureLocation(material, pattern);
            var mc = Minecraft.getInstance();
            var effectiveRl = mc.getResourceManager().getResource(rl).isPresent() ? rl : AnimalArmorTrimsClient.wolfTextureLocation(material, "coast");
            VertexConsumer vertexConsumer = AnimalArmorTrimsClient.WOLF_CACHE.get(cacheKey, () -> buf -> buf.getBuffer(RenderType.armorCutoutNoCull(effectiveRl))).apply(buffer);

            if (vertexConsumer != null) {
                this.model.get().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        } catch (ExecutionException e) {
            System.out.println("failed to render wolf armor trim...");
        }
    }
}
