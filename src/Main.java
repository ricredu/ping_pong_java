

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class Main extends Application
{
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 15;
    private static final int PLAYER_WIDTH = 100;
    private static final double BALL_R = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerYPos = height - PLAYER_HEIGHT;
    private double playerXPos = width / 2 - PLAYER_WIDTH / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int score = 0;
    private boolean gameStarted;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean gameEnded = false;
    private Color ballColor = Color.WHITE;
    public GraphicsContext gc;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("P O N G");
        stage.setResizable(false);
        stage.show();
        gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        canvas.setOnMouseClicked(e -> {
            if (!gameStarted) {
                resetGame();
            }
        });

        // Event handlers for left and right arrow keys
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                moveLeft = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                moveRight = true;
            } else if (event.getCode() == KeyCode.Y && gameEnded) {
                resetGame();
            } else if (event.getCode() == KeyCode.N && gameEnded) {
                finishGame();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                moveLeft = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                moveRight = false;
            }
        });

        tl.play();
    }

    private void run(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));



        if (gameStarted)
        {
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            // Draw paddle
            gc.setFill(Color.WHITE);
            gc.fillRect(playerXPos, playerYPos, PLAYER_WIDTH, PLAYER_HEIGHT);

            // Draw ball
            gc.setFill(ballColor);
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

            if (gameEnded)
            {
                gc.setStroke(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText("Game Over\nPress 'Y' to Restart\nPress 'N' to Finish", width / 2, height / 2);
                return;
            }

          //   ball collision detection with paddle
            if (ballYPos + BALL_R >= playerYPos && ballYPos - BALL_R <= playerYPos + PLAYER_HEIGHT&&
                    ballXPos + BALL_R >= playerXPos && ballXPos - BALL_R <= playerXPos + PLAYER_WIDTH  )
            {
                ballYSpeed *= -1; // Reverse ball direction
                score++; // Increase score
            }


            // Move paddle
            if (moveLeft && playerXPos > 0)
            {
                playerXPos -= 3; // Adjust speed as needed
            }
            if (moveRight && playerXPos + PLAYER_WIDTH < width)
            {
                playerXPos += 3; // Adjust speed as needed
            }

            // Check ball collision with walls
            if (ballXPos + BALL_R >= width || ballXPos <= 0)
            {
                ballXSpeed *= -1;
            }

            // Check ball collision with top wall
            if (ballYPos <= 0)
            {
                ballYSpeed *= -1;
            }

            // Check for game over
            if (ballYPos >= height || (ballXPos + BALL_R < playerXPos && ballXPos > playerXPos + PLAYER_WIDTH))
            {
                gameEnded = true;
            }
        }




        else
        {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click to Start", width / 2, height / 2);
        }

        // Display score
        gc.fillText(String.valueOf(score), width / 2, 100);
    }

    private void resetGame()
    {
        gameStarted = true;
        gameEnded = false;
        score = 0;
        ballXPos = width / 2;
        ballYPos = height / 2;
        Random random = new Random();
        int randomX = random.nextInt(2); // Generates either 0 or 1
        if (randomX == 0)
        {
            ballXSpeed = 1;
        }
        else
        {
            ballXSpeed = -1;
        }

        int randomY = random.nextInt(2); // Generates either 0 or 1
        if (randomY == 0)
        {
            ballYSpeed = 1;
        }
        else
        {
            ballYSpeed = -1;
        }
//        ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
//        ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
    }

    private void finishGame()
    {
        gameStarted = false;
        gameEnded = true;
        Platform.exit();
    }
}
