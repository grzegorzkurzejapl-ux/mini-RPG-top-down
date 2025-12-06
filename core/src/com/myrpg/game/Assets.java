package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static TextureAtlas playerAtlas;
    private static final Map<String, TextureRegion> tiles = new HashMap<>();
    private static Texture inventoryPanel;

    public static void load() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        tiles.put("grass", new TextureRegion(new Texture(Gdx.files.internal("tiles/grass.png"))));
        tiles.put("dirt", new TextureRegion(new Texture(Gdx.files.internal("tiles/dirt.png"))));
        tiles.put("water", new TextureRegion(new Texture(Gdx.files.internal("tiles/water.png"))));
        tiles.put("tree", new TextureRegion(new Texture(Gdx.files.internal("tiles/tree.png"))));
        tiles.put("soil", new TextureRegion(new Texture(Gdx.files.internal("tiles/soil.png"))));
        tiles.put("seed", new TextureRegion(new Texture(Gdx.files.internal("tiles/crop_seed.png"))));
        tiles.put("grow", new TextureRegion(new Texture(Gdx.files.internal("tiles/crop_grow.png"))));
        tiles.put("ready", new TextureRegion(new Texture(Gdx.files.internal("tiles/crop_ready.png"))));
        tiles.put("fish_common", new TextureRegion(new Texture(Gdx.files.internal("items/fish_common.png"))));
        tiles.put("fish_rare", new TextureRegion(new Texture(Gdx.files.internal("items/fish_rare.png"))));
        tiles.put("carrot", new TextureRegion(new Texture(Gdx.files.internal("items/carrot.png"))));
        tiles.put("seed_item", new TextureRegion(new Texture(Gdx.files.internal("items/seeds.png"))));
        inventoryPanel = new Texture(Gdx.files.internal("ui/inventory_bg.png"));
    }

    public static TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    public static TextureRegion getTile(String name) {
        return tiles.get(name);
    }

    public static Texture getInventoryPanel() { return inventoryPanel; }

    public static void dispose() {
        playerAtlas.dispose();
        inventoryPanel.dispose();
        tiles.values().forEach(region -> region.getTexture().dispose());
    }
}
