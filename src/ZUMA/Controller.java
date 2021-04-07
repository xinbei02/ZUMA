package ZUMA;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller {

	public Game game;
	Pane pausePane = new Pane();
	boolean pauseStart = true;
	static int time = 0;
	private Timeline pressedTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
		time++;
	}));

	public Controller() {
	}

	public Controller(Game b) {
		this.game = b;
	}

	// startBtn - start the game (Main.java)
	public EventHandler<ActionEvent> startGame = (e) -> {
		new Game();
		Main.mainStage.setScene(Game.gameScene);
	};
	public EventHandler<MouseEvent> startBtnEntered = (e) -> {
		Element.start.setVisible(true);
	};
	public EventHandler<MouseEvent> startBtnExited = (e) -> {
		Element.start.setVisible(false);
	};

	// exitBtn - close the stage (Main.java)
	public EventHandler<ActionEvent> exitGame = (e) -> {
		Main.mainStage.close();
	};
	public EventHandler<MouseEvent> exitBtnEntered = (e) -> {
		Element.exit.setVisible(true);
	};
	public EventHandler<MouseEvent> exitBtnExited = (e) -> {
		Element.exit.setVisible(false);
	};

	// backBtn - back to menu (Game.java)
	public EventHandler<ActionEvent> backToMenu = (e) -> {
		stopTimeline();
		initialize();
		Main.mainStage.setScene(Main.mainScene);
	};
	public EventHandler<MouseEvent> backBtnEntered = (e) -> {
		game.backBtn.setTextFill(Color.BROWN);
	};
	public EventHandler<MouseEvent> backBtnExited = (e) -> {
		game.backBtn.setTextFill(Color.BLACK);
	};

	// pauseBtn - pause the game (Game.java)
	public EventHandler<ActionEvent> pauseGame = (e) -> {
		pauseStart = false;
		pauseTimeline();
		pausePane.setMinSize(700, 600);
		pausePane.setBackground(new Background(new BackgroundFill(new Color(1.0f, 1.0f, 1.0f, 0.15f), null, null)));
		Element.pause.setLayoutX(300);
		Element.pause.setLayoutY(250);
		Element.pause.setFitWidth(100);
		Element.pause.setPreserveRatio(true);
		pausePane.getChildren().add(Element.pause);
		game.playPane.getChildren().add(pausePane);
	};
	public EventHandler<MouseEvent> pauseBtnEntered = (e) -> {
		game.pauseBtn.setTextFill(Color.BROWN);
	};
	public EventHandler<MouseEvent> pauseBtnExited = (e) -> {
		game.pauseBtn.setTextFill(Color.BLACK);
	};
	public EventHandler<MouseEvent> goGame = (e) -> {
		pausePane.getChildren().remove(Element.pause);
		game.playPane.getChildren().remove(pausePane);
		playTimeline();
		Timeline start = new Timeline(new KeyFrame(Duration.millis(1), (k) -> {
			pauseStart = true;
		}));
		start.setCycleCount(1);
		start.play();
	};

	// roll the toad, shotBean and nextShotBean (Game.java)
	public EventHandler<MouseEvent> rollToad = (e) -> {
		if(!(game.skullTL.getStatus()==Timeline.Status.RUNNING))
			if(!(game.winTL.getStatus()==Timeline.Status.RUNNING))
				if(game.beanChainRunTL.getStatus()==Timeline.Status.RUNNING&&pauseStart==true)
					game.roll(e);
	};

	// shoot the bean (Game.java)
	public EventHandler<MouseEvent> shotBean = (e) -> {
		if(!(game.skullTL.getStatus()==Timeline.Status.RUNNING))
			if(!(game.winTL.getStatus()==Timeline.Status.RUNNING))
				if(game.beanChainRunTL.getStatus()==Timeline.Status.RUNNING)
					if(!(pressedTL.getStatus()==Timeline.Status.RUNNING)&&pauseStart==true)
					{
						game.shotBean(e.getX(), e.getY());
						game.addShotBean();
						game.gem();
						pressedTL.setCycleCount(200);
						pressedTL.play();
					}
	};

	// continueBtn - next level (Game.java)
	public EventHandler<ActionEvent> continueGame = (e) -> {
		Main.level += 1;
		new Game();
		Main.mainStage.setScene(Game.gameScene);
	};
	public EventHandler<MouseEvent> continueBtnEntered = (e) -> {
		Element.continue1.setVisible(true);
	};
	public EventHandler<MouseEvent> continueBtnExited = (e) -> {
		Element.continue1.setVisible(false);
	};

	// againBtn - play again (Game.java)
	public EventHandler<ActionEvent> againGame = (e) -> {
		initialize();
		new Game();
		Main.mainStage.setScene(Game.gameScene);
	};
	public EventHandler<MouseEvent> againBtnEntered = (e) -> {
		Element.again.setVisible(true);
	};
	public EventHandler<MouseEvent> againBtnExited = (e) -> {
		Element.again.setVisible(false);
	};

	// menuBtn - back to menu (Game.java)
	public EventHandler<ActionEvent> toMenu = (e) -> {
		initialize();
		Main.mainStage.setScene(Main.mainScene);
	};
	public EventHandler<MouseEvent> menuBtnEntered = (e) -> {
		Element.menu.setVisible(true);
	};
	public EventHandler<MouseEvent> menuBtnExited = (e) -> {
		Element.menu.setVisible(false);
	};

	// switch the shotBean (Game.java)
	public EventHandler<KeyEvent> switchShotBean = (e) -> {
		if(e.getCode()==KeyCode.SPACE)
			game.switchShotBean();
	};

	private void pauseTimeline() {
		game.colorControllerTL.pause();
		game.beanChainRunTL.pause();
		game.changeSpeed.pause();
		game.shotBeanTL.pause();
		game.insertBeanTL.pause();
		game.insertTL.pause();
		game.moveBackTL.pause();
	}

	private void playTimeline() {
		game.beanChainRunTL.play();
		game.changeSpeed.play();
		game.shotBeanTL.play();
		game.insertBeanTL.play();
		game.insertTL.play();
		game.moveBackTL.play();
	}

	private void stopTimeline() {
		game.winTL.stop();
		game.skullTL.stop();
		game.colorControllerTL.stop();
		game.beanChainRunTL.stop();
		game.changeSpeed.stop();
		game.shotBeanTL.stop();
		game.insertBeanTL.stop();
		game.insertTL.stop();
		game.moveBackTL.stop();
	}

	private void initialize() {
		Main.level = 0;
		Main.scoreSave = "0";
		Main.score.setText(Main.scoreSave);
	}
}
