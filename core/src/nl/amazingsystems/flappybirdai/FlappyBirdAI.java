package nl.amazingsystems.flappybirdai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import nl.amazingsystems.flappybirdai.entities.managers.ObstacleManager;
import nl.amazingsystems.flappybirdai.entities.managers.Population;

public class FlappyBirdAI extends ApplicationAdapter {
	public static Population population;
	public static ObstacleManager obstacleManager;

	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		population = new Population(200);
		obstacleManager = new ObstacleManager();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.55f, 0.75f, 0.99f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float deltaTime = Gdx.graphics.getDeltaTime();

		population.update(deltaTime);
		obstacleManager.update(deltaTime);

		batch.begin();
		population.draw(batch);
		obstacleManager.draw(batch);

		BitmapFont font = new BitmapFont();
		font.draw(batch, "Generation: " + population.getGeneration(), 20f, Gdx.graphics.getHeight() - 30f);
		font.draw(batch, "Alive: " + population.getCurrentAlive(), 20f, Gdx.graphics.getHeight() - 60f);
		font.draw(batch, "Score: " + population.getHighestScore(), 20f, Gdx.graphics.getHeight() - 90f);

		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			population.evolve();
			obstacleManager.reset();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
		    population.reset();
		    obstacleManager.reset();
		}

		if (population.getCurrentAlive() == 0) {
			population.evolve();
			obstacleManager.reset();
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
