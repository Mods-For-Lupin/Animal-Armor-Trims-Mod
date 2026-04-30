package io.github.jason13official.animal_armor_trims.mixin;

import io.github.jason13official.animal_armor_trims.Constants;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class FabricClientPacketListenerMixin {

  @Shadow
  private LayeredRegistryAccess<ClientRegistryLayer> registryAccess;

  @Inject(at = @At("TAIL"), method = "handleUpdateTags")
  private void animal_armor_trims$injected(ClientboundUpdateTagsPacket packet, CallbackInfo ci) {
    if (FabricLoader.getInstance().isModLoaded("vanillabackport")) {
      Constants.LOG.info("adding wolf armor to trimmable armor tag, cause: Mod Id 'vanillabackport' loaded");
      this.registryAccess.compositeAccess().registry(ItemTags.TRIMMABLE_ARMOR.registry()).ifPresent(reg -> {
        reg.bindTags(Map.of(ItemTags.TRIMMABLE_ARMOR, List.of(BuiltInRegistries.ITEM.wrapAsHolder(com.blackgear.vanillabackport.common.registries.ModItems.WOLF_ARMOR.get()))));
      });
    }
  }
}
