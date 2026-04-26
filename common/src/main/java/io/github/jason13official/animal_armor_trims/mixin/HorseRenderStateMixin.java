package io.github.jason13official.animal_armor_trims.mixin;

import io.github.jason13official.animal_armor_trims.impl.client.api.renderer.entity.state.HorseArmorRenderStateAccessor;
import java.util.Optional;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HorseRenderState.class)
public class HorseRenderStateMixin implements HorseArmorRenderStateAccessor {

  @Unique
  private ArmorTrim animal_armor_trims$horseArmorTrim = null;

  @Override
  public Optional<ArmorTrim> animal_armor_trims$getArmorTrim() {
    ArmorTrim trim = animal_armor_trims$horseArmorTrim;
    return trim != null ? Optional.of(trim) : Optional.empty();
  }

  @Override
  public void animal_armor_trims$setArmorTrim(ArmorTrim armorTrim) {
    this.animal_armor_trims$horseArmorTrim = armorTrim;
  }
}
