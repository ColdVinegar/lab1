package com.example.ui_app;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Random;

public class HelloController {

    @FXML private GridPane env_GridPane;

    @FXML private Label plants_C_tw;
    @FXML private Label preys_C_tw;
    @FXML private Label predators_C_tw;
    @FXML private Label plants_S_tw;
    @FXML private Label plants_L_tw;
    @FXML private Label preys_S_tw;
    @FXML private Label preys_L_tw;
    @FXML private Label predators_S_tw;
    @FXML private Label predators_L_tw;
    @FXML private Label speed_tw;
    @FXML private Label steps_tw;
    @FXML private Label plants_pb_tw;
    @FXML private Label preys_pb_tw;
    @FXML private Label predators_pb_tw;

    @FXML private Spinner<Integer> height_spinner;
    @FXML private Spinner<Integer> width_spinner;

    @FXML private Slider plants_C_slider;
    @FXML private Slider preys_C_slider;
    @FXML private Slider predators_C_slider;
    @FXML private Slider plants_S_slider;
    @FXML private Slider plants_L_slider;
    @FXML private Slider preys_S_slider;
    @FXML private Slider preys_L_slider;
    @FXML private Slider predators_S_slider;
    @FXML private Slider predators_L_slider;
    @FXML private Slider speed_slider;

    @FXML private ProgressBar plants_pb;
    @FXML private ProgressBar preys_pb;
    @FXML private ProgressBar predators_pb;

    @FXML private Button apply_btn;
    @FXML private Button play_btn;
    @FXML private Button step_btn;

    private StackPane[][] cells;
    private Environment env;

    private Image imgPlant;
    private Image imgPrey;
    private Image imgPredator;

    private AnimationTimer timer;
    private boolean running = false;
    private long lastUpdate = 0;
    private static final long INTERVAL = 300_000_000L;
    private int steps = 0;

    @FXML
    public void initialize(){
        imgPlant = loadImage("/com/example/ui_app/plant.png");
        imgPrey = loadImage("/com/example/ui_app/prey.png");
        imgPredator = loadImage("/com/example/ui_app/predator.png");

        this.apply_btn.requestFocus();

        resetSpinners();

        height_spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            resetSliders(true);
            switchControls(false);
        });
        width_spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            resetSliders(true);
            switchControls(false);
        });
        plants_C_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.plants_C_tw.setText(String.format("Plants: %d", newVal.intValue()));
            updateSliders();
            switchControls(false);
        });
        preys_C_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.preys_C_tw.setText(String.format("Preys: %d", newVal.intValue()));
            updateSliders();
            switchControls(false);
        });
        predators_C_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.predators_C_tw.setText(String.format("Predators: %d", newVal.intValue()));
            updateSliders();
            switchControls(false);
        });
        plants_S_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.plants_S_tw.setText(String.format("Plants start health: %d", newVal.intValue()));
            switchControls(false);
        });
        plants_L_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.plants_L_tw.setText(String.format("Plants div limit: %d", newVal.intValue()));
            switchControls(false);
        });
        preys_S_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.preys_S_tw.setText(String.format("Preys start health: %d", newVal.intValue()));
            switchControls(false);
        });
        preys_L_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.preys_L_tw.setText(String.format("Preys div limit: %d", newVal.intValue()));
            switchControls(false);
        });
        predators_S_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.predators_S_tw.setText(String.format("Predators start health: %d", newVal.intValue()));
            switchControls(false);
        });
        predators_L_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.predators_L_tw.setText(String.format("Predators div limit: %d", newVal.intValue()));
            switchControls(false);
        });
        speed_slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.speed_tw.setText(String.format("Speed: %.2fX", newVal.doubleValue()/100));
        });

        resetSliders(false);
        setupEnv();
        buildGrid(env.height, env.length);
        env.clear();
        render();
    }

    private void switchControls(boolean key){
        play_btn.setDisable(!key);
        step_btn.setDisable(!key);
    }

    private Image loadImage(String resourcePath) {
        var stream = getClass().getResourceAsStream(resourcePath);
        if (stream == null) {
            System.err.println("Image not found: " + resourcePath);
            return null;
        }
        return new Image(stream);
    }

    @FXML
    protected void updateSliders(){
        int capacity = this.height_spinner.getValue()*this.width_spinner.getValue();
        int plants = (int)this.plants_C_slider.getValue();
        int preys = (int)this.preys_C_slider.getValue();
        int predators = (int)this.predators_C_slider.getValue();

        this.plants_C_slider.setMax(capacity-preys-predators);
        this.plants_C_slider.setMajorTickUnit(Math.max((int)this.plants_C_slider.getMax()/4, 1));
        this.preys_C_slider.setMax(capacity-plants-predators);
        this.preys_C_slider.setMajorTickUnit(Math.max((int)this.preys_C_slider.getMax()/4, 1));
        this.predators_C_slider.setMax(capacity-plants-preys);
        this.predators_C_slider.setMajorTickUnit(Math.max((int)this.predators_C_slider.getMax()/4, 1));

        this.plants_C_slider.setDisable(this.plants_C_slider.getMax() == 1);
        this.preys_C_slider.setDisable(this.preys_C_slider.getMax() == 1);
        this.predators_C_slider.setDisable(this.predators_C_slider.getMax() == 1);
    }

    @FXML
    protected void resetSliders(boolean onlyCounts){
        plants_C_slider.setValue(100);
        preys_C_slider.setValue(200);
        predators_C_slider.setValue(200);
        if(!onlyCounts){
            plants_S_slider.setValue(1);
            plants_L_slider.setValue(10);
            preys_S_slider.setValue(15);
            preys_L_slider.setValue(20);
            predators_S_slider.setValue(15);
            predators_L_slider.setValue(20);
            speed_slider.setValue(100);
        }
        updateSliders();
    }

    @FXML
    protected void resetSpinners(){
        height_spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 200, 50)
        );
        width_spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 200, 50)
        );
    }

    @FXML
    protected void onCancelClicked(){
        resetSpinners();
        resetSliders(false);
        env.clear();
        steps = 0;
        buildGrid(env.height, env.length);
        render();
        switchControls(false);
    }

    @FXML
    protected void onApplyClicked(){
        env.clear();
        steps = 0;
        setupEnv();
        buildGrid(env.height, env.length);
        render();
        switchControls(true);
    }

    @FXML
    protected void onPlayClicked(){
        if (running) pauseSimulation();
        else startSimulation();
    }

    @FXML
    protected void onStepClicked(){
        env.proceed();
        steps++;
        updateStatus();
        render();
    }

    private void startSimulation() {
        this.step_btn.setDisable(true);
        if (timer == null) createTimer();
        lastUpdate = 0;
        timer.start();
        running = true;
        play_btn.setText("⏸");
    }

    private void pauseSimulation() {
        this.step_btn.setDisable(false);
        if (timer != null) timer.stop();
        running = false;
        play_btn.setText("▶");
    }

    private void stopSimulation() {
        if (timer != null) timer.stop();
        running = false;
        play_btn.setText("▶");
    }

    private void createTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate < INTERVAL/(speed_slider.getValue()/100)) return;
                lastUpdate = now;

                if (env.plants > 0 && env.preys > 0 && env.predators > 0) {
                    env.proceed();
                    steps++;
                    updateStatus();
                    render();
                } else {
                    stopSimulation();
                }
            }
        };
    }

    private void updateStatus(){
        int population = env.plants+env.preys+env.predators;

        steps_tw.setText(String.format("Steps: %d, Agents: %d", steps, population));
        plants_pb_tw.setText(String.format("Plants: %d", env.plants));
        plants_pb.setProgress((double) env.plants /population);
        preys_pb_tw.setText(String.format("Preys: %d", env.preys));
        preys_pb.setProgress((double) env.preys /population);
        predators_pb_tw.setText(String.format("Predators: %d", env.predators));
        predators_pb.setProgress((double) env.predators /population);
    }

    private void setupEnv(){
        env = new Environment(this.height_spinner.getValue(), this.width_spinner.getValue());
        spawn(agentType.PLANT, (int)this.plants_C_slider.getValue(), env);
        spawn(agentType.PREY, (int)this.preys_C_slider.getValue(), env);
        spawn(agentType.PREDATOR, (int)this.predators_C_slider.getValue(), env);
    }

    private void spawn(agentType type, int count, Environment env){
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(env.length);
                y = random.nextInt(env.height);
            }while (env.world[y][x] != null);
            Agent agnt = null;
            switch (type){
                case PLANT -> agnt = new Plant(type, x, y, (int)this.plants_S_slider.getValue(), (int)this.plants_L_slider.getValue());
                case PREY -> agnt = new Prey(type, x, y, (int)this.preys_S_slider.getValue(), (int)this.preys_L_slider.getValue());
                case PREDATOR -> agnt = new Predator(type, x, y, (int)this.predators_S_slider.getValue(), (int)this.predators_L_slider.getValue());
            }
            env.add(agnt);
        }
    }

    private void buildGrid(int rows, int cols) {
        env_GridPane.getChildren().clear();
        env_GridPane.getColumnConstraints().clear();
        env_GridPane.getRowConstraints().clear();
        env_GridPane.setGridLinesVisible(false);


        for (int c = 0; c < cols; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / cols);
            cc.setHgrow(Priority.ALWAYS);
            env_GridPane.getColumnConstraints().add(cc);
        }
        for (int r = 0; r < rows; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / rows);
            rc.setVgrow(Priority.ALWAYS);
            env_GridPane.getRowConstraints().add(rc);
        }

        cells = new StackPane[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().addAll("env-cell", "cell-empty");

                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                iv.setSmooth(true);
                iv.setFitWidth(11);
                iv.setFitHeight(11);

                cell.getChildren().add(iv);
                cells[r][c] = cell;
                env_GridPane.add(cell, c, r);
            }
        }
    }

    public void render() {
        for (int r = 0; r < env.height; r++) {
            for (int c = 0; c < env.length; c++) {
                Agent a = env.world[r][c];
                StackPane cell = cells[r][c];
                ImageView iv = (ImageView) cell.getChildren().get(0);

                cells[r][c].setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #DBDBDB;" +
                                "-fx-border-width: 0.4;" +
                                "-fx-alignment: center;"
                );

                if (a == null) {
                    iv.setImage(null);
                    cell.getStyleClass().setAll("env-cell", "cell-empty");
                    continue;
                }

                iv.setImage(switch (a.type) {
                    case PLANT    -> imgPlant;
                    case PREY     -> imgPrey;
                    case PREDATOR -> imgPredator;
                });
                cell.getStyleClass().setAll("env-cell", switch (a.type) {
                    case PLANT    -> "cell-plant";
                    case PREY     -> "cell-prey";
                    case PREDATOR -> "cell-predator";
                });
            }
        }
    }
}
