package nl.amazingsystems.flappybirdai.entities.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import nl.amazingsystems.flappybirdai.entities.Bird;
import nl.amazingsystems.flappybirdai.entities.EntityInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Population implements EntityInterface {

    private List<Bird> birds;
    private final int size;
    private int generation = 0;
    private Texture birdTexture;

    public Population(int size) {
        this.size = size;
        this.birdTexture = new Texture("entities/bird.png");

        this.reset();
    }

    @Override
    public void dispose() {
        this.birds.forEach(EntityInterface::dispose);
    }

    @Override
    public void draw(Batch batch) {
        this.birds.forEach(e -> e.draw(batch));
    }

    @Override
    public void update(float deltaTime) {
        this.birds.forEach(e -> e.update(deltaTime));
    }

    public void evolve() {
        this.generation++;

        List<Bird> highScoreBirds = this.birds.stream()
                .sorted(Comparator.comparing(Bird::getScore).reversed())
                .limit((int) (this.size * 0.1f))
                .collect(Collectors.toList());

        int size = this.size;
        this.birds = new ArrayList<>(size);

        for (int i = 0; i < highScoreBirds.size() * 7.5f; i++, size--) {
            int randomIndex = MathUtils.random(0, highScoreBirds.size() - 1);
            Bird randomHighScoreBird = highScoreBirds.get(randomIndex);

            Bird bird = randomHighScoreBird == null
                    ? new Bird(this.birdTexture)
                    : new Bird(this.birdTexture, randomHighScoreBird.getBrain());

            this.birds.add(bird);
        }

        while (size-- > 0) {
            this.birds.add(new Bird(this.birdTexture));
        }
    }

    public void reset() {
        this.generation = 0;
        this.birds = new ArrayList<>(this.size);

        int size = this.size;
        while (size-- > 0) {
            Bird bird = new Bird(this.birdTexture);

            this.birds.add(bird);
        }
    }

    public int getSize() {
        return (int) this.birds.stream()
                .filter(b -> !b.isDead())
                .count();
    }

    public int getGeneration() {
        return this.generation;
    }

    public int getCurrentAlive() {
        return (int) this.birds.stream()
                .filter(b -> !b.isDead())
                .count();
    }

    public float getHighestScore() {
        return this.birds.stream()
                .filter(b -> !b.isDead())
                .max(Comparator.comparing(Bird::getScore))
                .map(Bird::getScore)
                .orElse(0f);
    }
}
