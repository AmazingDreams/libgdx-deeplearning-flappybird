package nl.amazingsystems.flappybirdai.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.amazingsystems.flappybirdai.FlappyBirdAI;
import nl.amazingsystems.flappybirdai.entities.controllers.BirdController;

public class Bird extends BaseEntity {

    private float jumpStrength = 500f;
    private boolean isDead = false;
    private BirdController brain;
    private float score = 0f;

    public Bird(Texture texture) {
        super(texture);

        this.brain = new BirdController(this);

        this.init();
    }

    public Bird(Texture texture, BirdController oldBrain) {
        this(texture);

        this.brain = new BirdController(this, oldBrain);

        this.init();
    }

    private void init() {
        this.gravityEnabled = true;
        this.mass = 100f;

        this.position.x = Gdx.graphics.getWidth() * MathUtils.random(0.1f, 0.3f);
        this.position.y = Gdx.graphics.getHeight() * MathUtils.random(0.2f, 0.8f);
    }

    public boolean isDead() {
        return this.isDead;
    }

    @Override
    public void draw(Batch batch) {
        if (this.isDead) {
            return;
        }

        super.draw(batch);
    }

    public BirdController getBrain() {
        return this.brain;
    }

    @Override
    public void update(float deltaTime) {
        if (this.isDead) {
            return;
        }

        // Score is basically time alive = good
        this.score += deltaTime;

        super.update(deltaTime);

        this.brain.update(deltaTime);

        this.checkIsDead();
    }

    public void jump() {
        // Disable influence of gravity
        this.acceleration.set(0, 0);
        this.acceleration.add(Vector2.Y.cpy().scl(this.jumpStrength));
    }

    private void checkIsDead() {
        if (this.position.y >= Gdx.graphics.getHeight() || this.position.y + this.dimensions.y / 2 <= 0) {
            this.isDead = true;
        }

        Rectangle myRect = new Rectangle();
        myRect.x = this.position.x;
        myRect.y = this.position.y;
        myRect.width = this.dimensions.x;
        myRect.height = this.dimensions.y;

        for (Obstacle o : FlappyBirdAI.obstacleManager.getObstacles()) {
            for (Rectangle rect : o.getRectangles()) {
                if (rect.overlaps(myRect)) {
                    this.isDead = true;
                    return;
                }
            }
        }
    }

    public float getScore() {
        return score;
    }
}
