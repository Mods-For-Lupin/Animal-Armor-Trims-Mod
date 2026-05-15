package io.github.jason13official.animal_armor_trims.mixin;

import io.github.jason13official.animal_armor_trims.impl.event.ModLevelEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class FabricServerLevelMixin {

  @Inject(at = @At("HEAD"), method = "addEntity")
  private void animal_armor_trims$addEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
    ServerLevel level = (ServerLevel) (Object) this;
    ModLevelEvents.JOIN_LEVEL.invoker().onJoinLevel(entity, level);
  }
}
