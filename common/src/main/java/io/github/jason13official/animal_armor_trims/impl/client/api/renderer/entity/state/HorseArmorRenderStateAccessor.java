package io.github.jason13official.animal_armor_trims.impl.client.api.renderer.entity.state;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public interface HorseArmorRenderStateAccessor {

  Optional<ArmorTrim> animal_armor_trims$getArmorTrim();
  void animal_armor_trims$setArmorTrim(ArmorTrim armorTrim);

}
