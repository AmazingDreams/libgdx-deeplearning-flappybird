package nl.amazingsystems.flappybirdai.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface EntityInterface {
    void dispose();
    void draw(Batch batch);
    void update(float deltaTime);
}
