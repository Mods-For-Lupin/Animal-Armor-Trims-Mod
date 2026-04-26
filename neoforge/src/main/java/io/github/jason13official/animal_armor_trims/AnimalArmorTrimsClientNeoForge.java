package io.github.jason13official.animal_armor_trims;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class AnimalArmorTrimsClientNeoForge {

  public AnimalArmorTrimsClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> AnimalArmorTrimsClient.init());

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {

      Entity entity = event.getEntity();
      if (Minecraft.getInstance().player != null && entity == Minecraft.getInstance().player) {
        AnimalArmorTrimsClient.onClientJoinLevel();
      }
    });
  }
}
