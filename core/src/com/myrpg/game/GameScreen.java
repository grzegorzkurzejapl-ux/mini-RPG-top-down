package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends InputAdapter implements com.badlogic.gdx.Screen {
    private final MyRpgGame game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Player player;
    private final MapManager mapManager;
    private final FishingSystem fishingSystem;
    private final FarmingSystem farmingSystem;
    private final Inventory inventory;
    private final BitmapFont font;
    private String message = "Bienvenue dans le mini RPG !";
    private float messageTimer = 0f;
    private boolean inventoryOpen = false;

    private final Rectangle btnUp = new Rectangle(60, 120, 60, 60);
    private final Rectangle btnDown = new Rectangle(60, 20, 60, 60);
    private final Rectangle btnLeft = new Rectangle(0, 70, 60, 60);
    private final Rectangle btnRight = new Rectangle(120, 70, 60, 60);
    private final Rectangle btnAction = new Rectangle(700, 40, 80, 80);
    private final Rectangle btnInventory = new Rectangle(700, 140, 80, 60);

    public GameScreen(MyRpgGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        mapManager = new MapManager();
        player = new Player(200, 200);
        fishingSystem = new FishingSystem();
        farmingSystem = new FarmingSystem(mapManager);
        inventory = new Inventory();
        font = new BitmapFont();
        SaveManager.load(player, inventory, farmingSystem, mapManager);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        handleInput(delta);
        player.update(delta);
        farmingSystem.update(delta);
        String fishMsg = fishingSystem.update(delta, inventory);
        if (fishMsg != null) setMessage(fishMsg);

        camera.position.set(player.getPosition().x + 16, player.getPosition().y + 16, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapManager.render(batch);
        farmingSystem.render(batch);
        batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y, player.width, player.height);
        batch.end();

        batch.setProjectionMatrix(camera.projection);
        batch.begin();
        font.draw(batch, "HP 100", 10, 470);
        font.draw(batch, "Action: pêche / planter / récolter", 10, 450);
        if (message != null) {
            font.draw(batch, message, 10, 430);
            messageTimer += delta;
            if (messageTimer > 3f) message = null;
        }
        drawButtons();
        if (inventoryOpen) drawInventory();
        batch.end();
    }

    private void drawInventory() {
        batch.draw(Assets.getInventoryPanel(), 250, 80, 300, 300);
        font.draw(batch, "Inventaire", 270, 370);
        int y = 340;
        for (InventoryItem item : inventory.getItems()) {
            String name = switch (item.type) {
                case FISH_COMMON -> "Poisson commun";
                case FISH_RARE -> "Poisson rare";
                case CARROT -> "Carotte";
                case SEED -> "Graine";
            };
            font.draw(batch, name + " x" + item.quantity, 270, y);
            TextureRegion icon = switch (item.type) {
                case FISH_COMMON -> Assets.getTile("fish_common");
                case FISH_RARE -> Assets.getTile("fish_rare");
                case CARROT -> Assets.getTile("carrot");
                case SEED -> Assets.getTile("seed_item");
            };
            batch.draw(icon, 430, y - 26, 24, 24);
            y -= 30;
        }
    }

    private void drawButtons() {
        font.draw(batch, "^", btnUp.x + 25, btnUp.y + 40);
        font.draw(batch, "v", btnDown.x + 25, btnDown.y + 40);
        font.draw(batch, "<", btnLeft.x + 25, btnLeft.y + 40);
        font.draw(batch, ">", btnRight.x + 25, btnRight.y + 40);
        font.draw(batch, "Action", btnAction.x + 5, btnAction.y + 45);
        font.draw(batch, "Inv", btnInventory.x + 10, btnInventory.y + 35);
    }

    private void handleInput(float delta) {
        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.move(Player.Direction.UP, delta, mapManager);
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.move(Player.Direction.DOWN, delta, mapManager);
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(Player.Direction.LEFT, delta, mapManager);
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(Player.Direction.RIGHT, delta, mapManager);
            moved = true;
        }
        if (!moved) player.stop();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        if (btnUp.contains(touch)) player.move(Player.Direction.UP, Gdx.graphics.getDeltaTime(), mapManager);
        if (btnDown.contains(touch)) player.move(Player.Direction.DOWN, Gdx.graphics.getDeltaTime(), mapManager);
        if (btnLeft.contains(touch)) player.move(Player.Direction.LEFT, Gdx.graphics.getDeltaTime(), mapManager);
        if (btnRight.contains(touch)) player.move(Player.Direction.RIGHT, Gdx.graphics.getDeltaTime(), mapManager);
        if (btnAction.contains(touch)) {
            performAction();
        }
        if (btnInventory.contains(touch)) inventoryOpen = !inventoryOpen;
        return true;
    }

    private void performAction() {
        int tx = (int) (player.getPosition().x / MapManager.TILE_SIZE);
        int ty = (int) (player.getPosition().y / MapManager.TILE_SIZE);
        String result;
        if (mapManager.isWater(player.getPosition().x, player.getPosition().y) ||
                mapManager.isWater(player.getPosition().x + MapManager.TILE_SIZE, player.getPosition().y) ||
                mapManager.isWater(player.getPosition().x, player.getPosition().y + MapManager.TILE_SIZE)) {
            result = fishingSystem.tryFish(mapManager, player, inventory);
            setMessage(result);
            return;
        }
        if (farmingSystem.harvest(tx, ty, inventory)) {
            setMessage("Vous récoltez une carotte.");
            return;
        }
        if (farmingSystem.plant(tx, ty)) {
            inventory.addItem(ItemType.SEED, 0);
            setMessage("Graine plantée.");
            return;
        }
        setMessage("Rien à faire ici.");
    }

    private void setMessage(String msg) {
        message = msg;
        messageTimer = 0f;
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
        SaveManager.save(player, inventory, farmingSystem, mapManager);
    }

    @Override
    public void resume() {
        SaveManager.load(player, inventory, farmingSystem, mapManager);
    }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
