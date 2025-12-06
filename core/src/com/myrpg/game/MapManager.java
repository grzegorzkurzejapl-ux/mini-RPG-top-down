package com.myrpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapManager {
    public static final int TILE_SIZE = 32;
    private final int width = 40;
    private final int height = 40;

    // 0 grass,1 dirt,2 water,3 tree,4 farmSoil
    private final int[][] tiles = new int[width][height];
    private final boolean[][] farmable = new boolean[width][height];

    public MapManager() {
        generateMap();
    }

    private void generateMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = 0; // grass
                if (x < 3 || y < 3 || x > width - 4 || y > height - 4) {
                    tiles[x][y] = 3; // border trees as collision walls
                }
            }
        }
        // lake area
        for (int x = 10; x < 20; x++) {
            for (int y = 5; y < 12; y++) {
                tiles[x][y] = 2;
            }
        }
        // dirt path area
        for (int x = 5; x < 35; x++) {
            tiles[x][20] = 1;
        }
        // field area
        for (int x = 25; x < 35; x++) {
            for (int y = 25; y < 33; y++) {
                tiles[x][y] = 4;
                farmable[x][y] = true;
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion grass = Assets.getTile("grass");
        TextureRegion dirt = Assets.getTile("dirt");
        TextureRegion water = Assets.getTile("water");
        TextureRegion tree = Assets.getTile("tree");
        TextureRegion soil = Assets.getTile("soil");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TextureRegion region = grass;
                switch (tiles[x][y]) {
                    case 1 -> region = dirt;
                    case 2 -> region = water;
                    case 3 -> region = tree;
                    case 4 -> region = soil;
                }
                batch.draw(region, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public boolean isWater(float x, float y) {
        int tx = (int) (x / TILE_SIZE);
        int ty = (int) (y / TILE_SIZE);
        if (!isInside(tx, ty)) return false;
        return tiles[tx][ty] == 2;
    }

    public boolean isFarmable(int tx, int ty) {
        if (!isInside(tx, ty)) return false;
        return farmable[tx][ty];
    }

    private boolean isInside(int tx, int ty) {
        return tx >= 0 && ty >= 0 && tx < width && ty < height;
    }

    public boolean isBlocked(float px, float py, float w, float h) {
        int left = (int) (px / TILE_SIZE);
        int right = (int) ((px + w - 1) / TILE_SIZE);
        int bottom = (int) (py / TILE_SIZE);
        int top = (int) ((py + h - 1) / TILE_SIZE);
        for (int x = left; x <= right; x++) {
            for (int y = bottom; y <= top; y++) {
                if (!isInside(x, y)) return true;
                int tile = tiles[x][y];
                if (tile == 2 || tile == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
