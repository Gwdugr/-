package com.example.goldendandelion;

import com.example.goldendandelion.item.GoldenDandelionItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoldenDandelionMod implements ModInitializer {
    public static final String MOD_ID = "goldendandelion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item GOLDEN_DANDELION = new GoldenDandelionItem(
        new Item.Settings()
            .rarity(Rarity.RARE)
            .maxCount(16)
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Golden Dandelion Mod!");

        Registry.register(Registries.ITEM,
            new Identifier(MOD_ID, "golden_dandelion"),
            GOLDEN_DANDELION);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(GOLDEN_DANDELION);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(GOLDEN_DANDELION);
        });
    }
}
