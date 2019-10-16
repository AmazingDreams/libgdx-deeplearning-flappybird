package nl.amazingsystems.flappybirdai.entities.controllers;

import com.badlogic.gdx.Gdx;
import nl.amazingsystems.flappybirdai.FlappyBirdAI;
import nl.amazingsystems.flappybirdai.entities.Bird;
import nl.amazingsystems.flappybirdai.entities.Obstacle;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.weightnoise.WeightNoise;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;

import java.util.List;

public class BirdController {

    public static MultiLayerNetwork createDefaultNetwork() {
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.05f))
                .weightInit(WeightInit.RELU)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(4)
                        .nOut(3)
                        .activation(Activation.SIGMOID)
                        .build()
                )
                .layer(1, new DenseLayer.Builder()
                        .nIn(3)
                        .nOut(1)
                        .activation(Activation.SIGMOID)
                        .build())
                .build();

        return new MultiLayerNetwork(configuration);
    }

    private final Bird bird;
    private final MultiLayerNetwork network;

    public BirdController(Bird bird) {
        this.bird = bird;
        this.network = createDefaultNetwork();
    }

    public BirdController(Bird bird, BirdController oldBrain) {
        this(bird, oldBrain.network);
    }

    public BirdController(Bird bird, MultiLayerNetwork originalNetwork) {
        this.bird = bird;
        this.network = new TransferLearning.Builder(originalNetwork)
                .fineTuneConfiguration(new FineTuneConfiguration.Builder()
                        .weightNoise(new WeightNoise(new NormalDistribution(0.01d, 0.005d), true))
                        .build()
                )
                .build();
    }

    private void think() {
        Obstacle next = FlappyBirdAI.obstacleManager.getNextObstacle(this.bird.getPosition().x);

        float horizontalDistanceToNext = next.getBottomRectangle().x;
        float verticalDistanceToBottom = this.bird.getPosition().y - next.getBottomRectangle().height;
        float verticalDistanceToTop = this.bird.getPosition().y - next.getTopRectangle().y;

        float[][] input = new float[][] {
                {
                        this.normalize(this.bird.getPosition().y, -Gdx.graphics.getHeight(), Gdx.graphics.getHeight()),
                        this.normalize(horizontalDistanceToNext, -Gdx.graphics.getWidth(), Gdx.graphics.getWidth()),
                        this.normalize(verticalDistanceToBottom, -Gdx.graphics.getHeight(), Gdx.graphics.getHeight()),
                        this.normalize(verticalDistanceToTop, -Gdx.graphics.getHeight(), Gdx.graphics.getHeight()),
                }
        };

        INDArray data = Nd4j.create(input);

        List<INDArray> result = this.network.feedForward(data);

        double rawOutput = result.get(1).getDouble(0);
        boolean shouldJump = rawOutput >= 0.5f;

        if (shouldJump) {
            this.bird.jump();
        }
    }

    public void update(float deltaTime) {
        this.think();
    }

    private float normalize(float input, float min, float max) {
        return (input - ((max + min) / 2)) / max;
    }
}
