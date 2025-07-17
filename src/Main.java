import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Main extends Application {

    private int questionIndex = 0;
    private int score = 0;
    private int timeLeft = 15;
    private Label questionLabel;
    private RadioButton[] options = new RadioButton[4];
    private ToggleGroup toggleGroup = new ToggleGroup();
    private Label timerLabel = new Label("Time: 15");
    private Label feedbackLabel = new Label();
    private Timeline timeline;

    private String[][] questions = {
        {"What is the capital of France?", "Berlin", "Paris", "Rome", "London", "2"},
        {"Which language runs in a web browser?", "Java", "C", "Python", "JavaScript", "4"},
        {"What is 10 + 20?", "10", "20", "30", "40", "3"}
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");
        
        questionLabel = new Label();
        for (int i = 0; i < 4; i++) {
            options[i] = new RadioButton();
            options[i].setToggleGroup(toggleGroup);
        }

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> checkAnswer());

        root.getChildren().addAll(timerLabel, questionLabel);
        root.getChildren().addAll(options);
        root.getChildren().addAll(submitButton, feedbackLabel);

        loadQuestion();

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("JavaFX Timed Quiz Game");
        stage.show();
    }

    private void loadQuestion() {
        if (questionIndex >= questions.length) {
            showResult();
            return;
        }

        timeLeft = 15;
        timerLabel.setText("Time: " + timeLeft);
        if (timeline != null) {
            timeline.stop();
        }

        questionLabel.setText("Q" + (questionIndex + 1) + ": " + questions[questionIndex][0]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[questionIndex][i + 1]);
            options[i].setSelected(false);
        }

        feedbackLabel.setText("");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                feedbackLabel.setText("Time's up!");
                questionIndex++;
                loadQuestion();
            }
        }));
        timeline.setCycleCount(15);
        timeline.play();
    }

    private void checkAnswer() {
        if (timeline != null) timeline.stop();

        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i + 1;
                break;
            }
        }

        if (selected == -1) {
            feedbackLabel.setText("Please select an answer!");
            timeline.play();
            return;
        }

        int correct = Integer.parseInt(questions[questionIndex][5]);

        if (selected == correct) {
            score++;
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Wrong! Correct answer: " + questions[questionIndex][correct]);
        }

        questionIndex++;
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> loadQuestion());
        pause.play();
    }

    private void showResult() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Completed");
        alert.setHeaderText("Final Score: " + score + " / " + questions.length);
        alert.setContentText("Thank you for playing!");
        alert.showAndWait();
        System.exit(0);
    }
}
