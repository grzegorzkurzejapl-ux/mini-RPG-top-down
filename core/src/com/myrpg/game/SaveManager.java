package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    private static final String SAVE_FILE = "save.json";

    public static class SaveData {
        public float playerX;
        public float playerY;
        public Map<ItemType, Integer> inventory = new HashMap<>();
        public int[][] cropStage;
        public boolean[][] cropPlanted;
    }

    public static void save(Player player, Inventory inventory, FarmingSystem farming, MapManager map) {
        SaveData data = new SaveData();
        data.playerX = player.getPosition().x;
        data.playerY = player.getPosition().y;
        for (InventoryItem item : inventory.getItems()) {
            data.inventory.put(item.type, item.quantity);
        }
        data.cropStage = new int[map.getWidth()][map.getHeight()];
        data.cropPlanted = new boolean[map.getWidth()][map.getHeight()];
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                FarmingSystem.CropTile tile = farming.get(x, y);
                if (tile != null) {
                    data.cropStage[x][y] = tile.stage;
                    data.cropPlanted[x][y] = tile.planted;
                }
            }
        }
        Json json = new Json();
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        handle.writeString(json.prettyPrint(data), false);
    }

    public static void load(Player player, Inventory inventory, FarmingSystem farming, MapManager map) {
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        if (!handle.exists()) return;
        Json json = new Json();
        SaveData data = json.fromJson(SaveData.class, handle.readString());
        player.getPosition().set(data.playerX, data.playerY);
        inventory.clear();
        for (Map.Entry<ItemType, Integer> entry : data.inventory.entrySet()) {
            inventory.addItem(entry.getKey(), entry.getValue());
        }
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (data.cropPlanted != null && x < data.cropPlanted.length && y < data.cropPlanted[x].length) {
                    if (data.cropPlanted[x][y]) {
                        farming.plant(x, y);
                        FarmingSystem.CropTile tile = farming.get(x, y);
                        tile.stage = data.cropStage[x][y];
                    }
                }
            }
        }
    }
}
