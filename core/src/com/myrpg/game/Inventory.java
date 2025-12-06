package com.myrpg.game;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<InventoryItem> items = new ArrayList<>();

    public void addItem(ItemType type, int qty) {
        InventoryItem found = items.stream().filter(i -> i.type == type).findFirst().orElse(null);
        if (found == null) {
            items.add(new InventoryItem(type, qty));
        } else {
            found.quantity += qty;
        }
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}
