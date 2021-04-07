package ZUMA;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	public static Stage mainStage; // main stage
	public static Scene mainScene; // main scene

	public Element element = new Element();

	public static int level = 0; // level
	public static String levelContext;// level label's context
	public static Label levelShow = new Label(levelContext);// level label
	public static String scoreSave = "0"; // score
	public static Label score = new Label(scoreSave); // score label

	@Override
	public void start(Stage mainStage) throws Exception {
		// cover
		Element.cover.setLayoutX(element.sceneW/2-element.coverW/2);
		Element.cover.setPreserveRatio(true);
		Element.cover.setFitHeight(element.coverH);

		Element.start.setPreserveRatio(true);
		Element.start.setFitWidth(element.startW);
		Element.start.setVisible(false);

		Element.exit.setPreserveRatio(true);
		Element.exit.setFitHeight(element.exitH);
		Element.exit.setVisible(false);

		// start button
		Button startBtn = new Button();
		startBtn.setBackground(Background.EMPTY);
		startBtn.setFocusTraversable(false);
		startBtn.setGraphic(Element.start);
		startBtn.setMinSize(element.startBtnW, element.startBtnH);
		startBtn.setLayoutX(element.sceneW/2-element.startBtnW/2-8);
		startBtn.setLayoutY(440);

		// exit button
		Button exitBtn = new Button();
		exitBtn.setBackground(Background.EMPTY);
		exitBtn.setFocusTraversable(false);
		exitBtn.setGraphic(Element.exit);
		exitBtn.setMinSize(element.exitBtnW, element.exitBtnH);
		exitBtn.setLayoutX(element.sceneW/2-element.exitBtnW/2-9);
		exitBtn.setLayoutY(520);

		// background
		ImageView background = new ImageView(Element.background1Image);
		background.setPreserveRatio(true);
		background.setFitWidth(700);

		// main pane
		Pane mainPane = new Pane();
		mainPane.getChildren().addAll(background, Element.cover, startBtn, exitBtn);

		// main scene
		mainScene = new Scene(mainPane, element.sceneW, element.sceneH);

		// main stage
		Main.mainStage = mainStage;
		mainStage.initStyle(StageStyle.TRANSPARENT);
		mainStage.setResizable(false);
		mainStage.setAlwaysOnTop(true);
		mainStage.setScene(mainScene);
		mainStage.show();

		// set on action (start button, back button)
		Controller ctrl = new Controller();
		startBtn.setOnAction(ctrl.startGame);
		startBtn.setOnMouseEntered(ctrl.startBtnEntered);
		startBtn.setOnMouseExited(ctrl.startBtnExited);
		exitBtn.setOnAction(ctrl.exitGame);
		exitBtn.setOnMouseEntered(ctrl.exitBtnEntered);
		exitBtn.setOnMouseExited(ctrl.exitBtnExited);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
