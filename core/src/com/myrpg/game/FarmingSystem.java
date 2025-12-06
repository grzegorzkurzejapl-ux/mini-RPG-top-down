package com.myrpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FarmingSystem {
    public static class CropTile {
        public boolean planted;
        public int stage; // 0 seed,1 growing,2 ready
        public float timer;
    }

    private final CropTile[][] crops;
    private final MapManager map;

    public FarmingSystem(MapManager map) {
        this.map = map;
        crops = new CropTile[map.getWidth()][map.getHeight()];
    }

    public boolean plant(int tx, int ty) {
        if (!map.isFarmable(tx, ty)) return false;
        CropTile crop = getOrCreate(tx, ty);
        if (crop.planted) return false;
        crop.planted = true;
        crop.stage = 0;
        crop.timer = 0;
        return true;
    }

    public boolean harvest(int tx, int ty, Inventory inventory) {
        CropTile crop = get(tx, ty);
        if (crop != null && crop.planted && crop.stage >= 2) {
            crop.planted = false;
            crop.stage = 0;
            inventory.addItem(ItemType.CARROT, 1);
            return true;
        }
        return false;
    }

    public void update(float delta) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                CropTile crop = crops[x][y];
                if (crop != null && crop.planted && crop.stage < 2) {
                    crop.timer += delta;
                    if (crop.timer > 5f) {
                        crop.stage++;
                        crop.timer = 0f;
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion seed = Assets.getTile("seed");
        TextureRegion grow = Assets.getTile("grow");
        TextureRegion ready = Assets.getTile("ready");
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                CropTile crop = crops[x][y];
                if (crop != null && crop.planted) {
                    TextureRegion tex = seed;
                    if (crop.stage == 1) tex = grow;
                    if (crop.stage >= 2) tex = ready;
                    batch.draw(tex, x * MapManager.TILE_SIZE, y * MapManager.TILE_SIZE, MapManager.TILE_SIZE, MapManager.TILE_SIZE);
                }
            }
        }
    }

    public CropTile get(int tx, int ty) {
        if (tx < 0 || ty < 0 || tx >= map.getWidth() || ty >= map.getHeight()) return null;
        return crops[tx][ty];
    }

    public CropTile[][] getCrops() {
        return crops;
    }

    private CropTile getOrCreate(int tx, int ty) {
        if (crops[tx][ty] == null) crops[tx][ty] = new CropTile();
        return crops[tx][ty];
    }
}
