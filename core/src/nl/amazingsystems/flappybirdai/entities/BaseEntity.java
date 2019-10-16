package nl.amazingsystems.flappybirdai.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseEntity implements EntityInterface {
    protected Vector2 position = new Vector2();
    protected Vector2 dimensions = new Vector2(50, 50);
    protected Vector2 acceleration = new Vector2();

    protected float rotation = 0f;
    protected TextureRegion textureRegion;

    protected boolean gravityEnabled = false;
    protected float gravity = 9.81f;
    protected float mass = 1f;

    public BaseEntity(Texture texture) {
        this.textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(
                this.textureRegion,
                this.position.x,
                this.position.y,
                this.dimensions.x / 2f,
                this.dimensions.y / 2f,
                this.dimensions.x,
                this.dimensions.y,
                1,
                1,
                this.rotation
        );
    }

    @Override
    public void update(float deltaTime) {
        if (this.gravityEnabled) {
            this.acceleration.add(Vector2.Y.cpy().scl(-1f * this.gravity * this.mass * deltaTime));
        }

        this.position.add(this.acceleration.cpy().scl(deltaTime));
    }

    public Vector2 getPosition() {
        return this.position;
    }
}
