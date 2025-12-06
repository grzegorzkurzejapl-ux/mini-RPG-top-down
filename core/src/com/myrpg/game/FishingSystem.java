package com.myrpg.game;

import com.badlogic.gdx.math.MathUtils;

public class FishingSystem {
    private float castTimer;
    private boolean fishing;

    public String tryFish(MapManager map, Player player, Inventory inventory) {
        float px = player.getPosition().x;
        float py = player.getPosition().y;
        float edgeX = px;
        float edgeY = py;
        switch (player.getFacing()) {
            case UP -> edgeY += MapManager.TILE_SIZE;
            case DOWN -> edgeY -= 4;
            case LEFT -> edgeX -= 4;
            case RIGHT -> edgeX += MapManager.TILE_SIZE;
        }
        if (!map.isWater(edgeX, edgeY)) {
            fishing = false;
            castTimer = 0;
            return "Vous devez être près de l'eau pour pêcher.";
        }
        fishing = true;
        castTimer = 0;
        return "Vous lancez la ligne...";
    }

    public String update(float delta, Inventory inventory) {
        if (!fishing) return null;
        castTimer += delta;
        if (castTimer > 2f) {
            fishing = false;
            float roll = MathUtils.random();
            if (roll < 0.2f) {
                inventory.addItem(ItemType.FISH_RARE, 1);
                return "(!) Poisson rare attrapé !";
            } else if (roll < 0.7f) {
                inventory.addItem(ItemType.FISH_COMMON, 1);
                return "Vous avez pêché un poisson commun.";
            } else {
                return "Rien n'a mordu...";
            }
        }
        return null;
    }
}
