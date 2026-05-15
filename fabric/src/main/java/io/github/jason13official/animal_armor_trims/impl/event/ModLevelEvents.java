package io.github.jason13official.animal_armor_trims.impl.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModLevelEvents {

  public static final Event<JoinLevel> JOIN_LEVEL = EventFactory.createArrayBacked(JoinLevel.class, callbacks -> (entity, level) -> {
    for (JoinLevel join : callbacks) {
      join.onJoinLevel(entity, level);
    }
  });

  @FunctionalInterface
  public interface JoinLevel {

    void onJoinLevel(Entity entity, Level level);
  }
}
