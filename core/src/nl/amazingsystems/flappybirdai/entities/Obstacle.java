package nl.amazingsystems.flappybirdai.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.List;

public class Obstacle implements EntityInterface {
    private float speed = 120f;
    private float gapSize = 200f;

    private Rectangle bottomRectangle = new Rectangle();
    private Rectangle topRectangle = new Rectangle();

    public Obstacle() {
        float width = 40f;
        float height = MathUtils.random(Gdx.graphics.getHeight() * 0.1f, Gdx.graphics.getHeight() * 0.5f);

        this.bottomRectangle.x = Gdx.graphics.getWidth();
        this.bottomRectangle.y = 0;
        this.bottomRectangle.width = width;
        this.bottomRectangle.height = height;

        this.topRectangle.x = this.bottomRectangle.x;
        this.topRectangle.y = this.bottomRectangle.y + this.bottomRectangle.height + this.gapSize;
        this.topRectangle.width = width;
        this.topRectangle.height = Gdx.graphics.getHeight() - this.topRectangle.y;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void draw(Batch batch) {
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(
                this.bottomRectangle.x,
                this.bottomRectangle.y,
                this.bottomRectangle.width,
                this.bottomRectangle.height
        );
        renderer.rect(
                this.topRectangle.x,
                this.topRectangle.y,
                this.topRectangle.width,
                this.topRectangle.height
        );
        renderer.end();

        renderer.dispose();

        batch.begin();
    }

    @Override
    public void update(float deltaTime) {
        this.bottomRectangle.x -= this.speed * deltaTime;
        this.topRectangle.x = this.bottomRectangle.x;
    }

    public boolean isDead() {
        return this.bottomRectangle.x + this.bottomRectangle.width < 0f;
    }

    public List<Rectangle> getRectangles() {
        return Arrays.asList(this.topRectangle, this.bottomRectangle);
    }

    public Rectangle getTopRectangle() {
        return this.topRectangle;
    }

    public Rectangle getBottomRectangle() {
        return this.bottomRectangle;
    }
}
