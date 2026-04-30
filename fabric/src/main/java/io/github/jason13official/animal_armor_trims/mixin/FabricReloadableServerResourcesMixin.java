package io.github.jason13official.animal_armor_trims.mixin;

import io.github.jason13official.animal_armor_trims.Constants;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadableServerResources.class)
public class FabricReloadableServerResourcesMixin {

  @Inject(at = @At("TAIL"), method = "updateRegistryTags(Lnet/minecraft/core/RegistryAccess;)V")
  private void animal_armor_trims$injected(RegistryAccess registryAccess, CallbackInfo ci) {
    if (FabricLoader.getInstance().isModLoaded("vanillabackport")) {
      Constants.LOG.info("adding wolf armor to trimmable armor tag, cause: Mod Id 'vanillabackport' loaded");
      registryAccess.registry(ItemTags.TRIMMABLE_ARMOR.registry()).ifPresent(reg -> {
        reg.bindTags(Map.of(ItemTags.TRIMMABLE_ARMOR, List.of(BuiltInRegistries.ITEM.wrapAsHolder(com.blackgear.vanillabackport.common.registries.ModItems.WOLF_ARMOR.get()))));
      });
    }
  }
}
