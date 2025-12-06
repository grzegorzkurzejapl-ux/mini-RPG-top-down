package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public enum Direction {UP, DOWN, LEFT, RIGHT}

    private float speed = 80f;
    private Direction facing = Direction.DOWN;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private float stateTime;
    private boolean moving;

    public Player(float x, float y) {
        super(x, y, 32, 32);
        loadAnimations();
    }

    private void loadAnimations() {
        TextureAtlas atlas = Assets.getPlayerAtlas();
        walkDown = new Animation<>(0.15f, atlas.findRegions("down"), Animation.PlayMode.LOOP);
        walkUp = new Animation<>(0.15f, atlas.findRegions("up"), Animation.PlayMode.LOOP);
        walkLeft = new Animation<>(0.15f, atlas.findRegions("left"), Animation.PlayMode.LOOP);
        walkRight = new Animation<>(0.15f, atlas.findRegions("right"), Animation.PlayMode.LOOP);
    }

    public void move(Direction direction, float delta, MapManager mapManager) {
        Vector2 oldPos = new Vector2(position);
        facing = direction;
        moving = true;
        switch (direction) {
            case UP -> position.y += speed * delta;
            case DOWN -> position.y -= speed * delta;
            case LEFT -> position.x -= speed * delta;
            case RIGHT -> position.x += speed * delta;
        }
        if (mapManager.isBlocked(position.x, position.y, width, height)) {
            position.set(oldPos);
        }
    }

    public void stop() {
        moving = false;
    }

    @Override
    public void update(float delta) {
        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public TextureRegion getCurrentFrame() {
        TextureRegion frame;
        switch (facing) {
            case UP -> frame = walkUp.getKeyFrame(stateTime);
            case LEFT -> frame = walkLeft.getKeyFrame(stateTime);
            case RIGHT -> frame = walkRight.getKeyFrame(stateTime);
            default -> frame = walkDown.getKeyFrame(stateTime);
        }
        return frame;
    }

    public Direction getFacing() {
        return facing;
    }
}
