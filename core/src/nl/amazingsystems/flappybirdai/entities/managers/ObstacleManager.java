package nl.amazingsystems.flappybirdai.entities.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import nl.amazingsystems.flappybirdai.entities.EntityInterface;
import nl.amazingsystems.flappybirdai.entities.Obstacle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ObstacleManager implements EntityInterface {

    private List<Obstacle> obstacles = new ArrayList<>();
    private int targetObstacles = 3;

    private float minTimeBetweenObstacles = 2f;
    private float maxTimeBetweenObstacles = 3f;
    private float minNextSpawnTime = 0f;

    private float totalElapsedTime = 0f;

    public ObstacleManager() {
        this.trySpawnObstacle();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void draw(Batch batch) {
        this.obstacles.forEach(o -> o.draw(batch));
    }

    @Override
    public void update(float deltaTime) {
        this.totalElapsedTime += deltaTime;

        this.obstacles.forEach(o -> o.update(deltaTime));

        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            if (this.obstacles.get(i).isDead()) {
                this.obstacles.remove(i);
            }
        }

        if (this.obstacles.size() < this.targetObstacles) {
            this.trySpawnObstacle();
        }
    }

    public void reset() {
        this.obstacles.clear();
        this.minNextSpawnTime = 0f;
        this.trySpawnObstacle();
    }

    private void trySpawnObstacle() {
        if (this.totalElapsedTime < minNextSpawnTime) {
            return;
        }

        this.obstacles.add(new Obstacle());
        this.minNextSpawnTime = this.totalElapsedTime + MathUtils.random(this.minTimeBetweenObstacles, this.maxTimeBetweenObstacles);
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public Obstacle getNextObstacle(float minX) throws NoSuchElementException {
        return this.obstacles.stream()
                .filter(o -> o.getBottomRectangle().x + o.getBottomRectangle().width > minX)
                .min(Comparator.comparing(o -> o.getBottomRectangle().x))
                .orElseThrow(NoSuchElementException::new);
    }
}
