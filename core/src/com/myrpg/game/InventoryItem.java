package com.myrpg.game;

enum ItemType {
    FISH_COMMON,
    FISH_RARE,
    CARROT,
    SEED
}

public class InventoryItem {
    public ItemType type;
    public int quantity;

    public InventoryItem(ItemType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }
}
