package io.github.jason13official.animal_armor_trims.mixin;

import io.github.jason13official.animal_armor_trims.impl.event.ModLevelEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class FabricClientLevelMixin {

  @Inject(at = @At("HEAD"), method = "addEntity")
  private void animal_armor_trims$addEntity(Entity entity, CallbackInfo ci) {
    ClientLevel level = (ClientLevel) (Object) this;
    ModLevelEvents.JOIN_LEVEL.invoker().onJoinLevel(entity, level);
  }
}
