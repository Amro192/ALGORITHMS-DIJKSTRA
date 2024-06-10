package com.othman.project3.UI;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.othman.project3.DijkstraAlgorithm;
import com.othman.project3.model.Capital;
import com.othman.project3.model.Graph;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Optional;

public class MainScreenController {

    private static final Graph graph = new Graph();

    static {
        DijkstraAlgorithm.run(graph);
    }

    @FXML
    private Message note;

    @FXML
    private ComboBox<String> targetComboBox;
    @FXML
    private ComboBox<String> sourceComboBox;
    @FXML
    private Button runButton;

    @FXML
    private Pane canvasPane;

    @FXML
    private TextArea pathTextArea;
    @FXML
    private TextField distanceTextField;

    @FXML
    public void initialize() {
        note.getStyleClass().add(Styles.SUCCESS);
        runButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.SUCCESS);
        targetComboBox.getItems().addAll(graph.getCapitals().keySet());
        sourceComboBox.getItems().addAll(graph.getCapitals().keySet());


        graph.getCapitals().values().forEach(capital -> {
            Circle circle = capital.projectToImage();
            Label label = new Label(capital.name());
            label.setStyle("-fx-text-fill: black; -fx-font-size: 10; -fx-font-weight: bold");
            label.setTranslateX(capital.mapLongitudeToX() - 10);
            label.setTranslateY(capital.mapLatitudeToY() - 23);

            circle.setOnMouseClicked(e -> {
                        switch (e.getButton()) {
                            case PRIMARY -> {
                                Animations.pulse(sourceComboBox).playFromStart();
                                sourceComboBox.setValue(capital.name());
                            }
                            case SECONDARY -> {
                                Animations.pulse(targetComboBox).playFromStart();
                                targetComboBox.setValue(capital.name());
                            }
                        }
                    }
            );

            canvasPane.getChildren().addAll(label, circle);
        });
    }

    @FXML
    public void onRunClicked() {
        canvasPane.getChildren().removeIf(node -> node instanceof Line || node instanceof Polygon);

        String source = sourceComboBox.getValue();
        String target = targetComboBox.getValue();

        Optional<Capital> nullableSource = graph.get(source);
        Optional<Capital> nullableTarget = graph.get(target);

        if (nullableSource.isEmpty() || nullableTarget.isEmpty()) {
            Animations.shakeX(runButton).playFromStart();
            return;
        }


        List<String> shortestPath = graph.getShortestPath(source, target);
        StringBuilder pathToPrint = new StringBuilder();

        for (int i = 0; i < shortestPath.size(); i++) {
            if (i == shortestPath.size() - 1) break;

            String from = shortestPath.get(i);
            String to = shortestPath.get(i + 1);

            graph.get(from).ifPresent(fromCapital -> graph.get(to).ifPresent(toCapital -> drawArrow(fromCapital, toCapital)));

        }


        for (String capital : shortestPath) {
            if (capital.equals(target)) {
                pathToPrint.append(capital);
                break;
            }
            pathToPrint.append(capital).append(" => ");
        }

        double distance = Capital.calculateDistance(nullableSource.get(), nullableTarget.get());
        Animations.pulse(runButton).playFromStart();
        Animations.pulse(pathTextArea).playFromStart();
        Animations.pulse(distanceTextField).playFromStart();
        pathTextArea.setText(pathToPrint.toString());
        distanceTextField.setText(String.valueOf(distance));
    }

    private void drawArrow(Capital from, Capital to) {

        double arrowLength = 10;


        Point2D toPoint = new Point2D(to.mapLongitudeToX(), to.mapLatitudeToY());

        // Create the main line
        Line line = new Line(from.mapLongitudeToX(), from.mapLatitudeToY(), to.mapLongitudeToX(), to.mapLatitudeToY());
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1);

        // Calculate the angle of the line
        double angle = Math.atan2(to.mapLatitudeToY() - from.mapLatitudeToY(), to.mapLongitudeToX() - from.mapLongitudeToX());

        // Create the arrowhead triangle
        Point2D arrowTip1 = toPoint.add(new Point2D(-arrowLength * Math.cos(angle - Math.PI / 6), -arrowLength * Math.sin(angle - Math.PI / 6)));
        Point2D arrowTip2 = toPoint.add(new Point2D(-arrowLength * Math.cos(angle + Math.PI / 6), -arrowLength * Math.sin(angle + Math.PI / 6)));

        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(toPoint.getX(), toPoint.getY(), arrowTip1.getX(), arrowTip1.getY(), arrowTip2.getX(), arrowTip2.getY());
        arrowHead.setFill(Color.BLACK);


        canvasPane.getChildren().addAll(line, arrowHead);
    }

}
