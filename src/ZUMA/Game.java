package ZUMA;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Game {

	public static Scene gameScene; // game scene
	public BorderPane gamePane = new BorderPane(); // 700 640
	public Pane topPane = new Pane(); // 700 40
	public Pane playPane = new Pane(); // 700 600
	public Button backBtn = new Button("BACK"); // back button
	public Button pauseBtn = new Button("PAUSE"); // pause button
	public static Rectangle scoreBarOUT = new Rectangle(339, 6.5, 102, 27); // scoreBar
	public static Rectangle scoreBarIN = new Rectangle(340.5, 7.75, 0, 24.5); // scoreBar

	private Element element = new Element();
	private Ground ground;

	public static int startBeanAmount1; // start bean's amount
	public static int startBeanAmount2; // start bean's amount
	public static int startBeanAmount3; // start bean's amount
	public static int passScore; // the score that should get to pass

	public Game() {

		startBeanAmount1 = 25;
		startBeanAmount2 = 20;
		startBeanAmount3 = 10;
		passScore = 1000;

		// background
		ImageView background = new ImageView(Element.background2Image);
		background.setPreserveRatio(true);
		background.setFitWidth(700);

		// back button
		backBtn.setLayoutX(5);
		backBtn.setLayoutY(5);
		backBtn.setMinSize(element.backW, element.backH);
		backBtn.setFocusTraversable(false);
		backBtn.setBackground(Background.EMPTY);
		backBtn.setFont(Font.font("Timer New Roman", FontWeight.BOLD, 15));

		// pause button
		pauseBtn.setLayoutX(70);
		pauseBtn.setLayoutY(5);
		pauseBtn.setMinSize(element.backW, element.backH);
		pauseBtn.setFocusTraversable(false);
		pauseBtn.setBackground(Background.EMPTY);
		pauseBtn.setFont(Font.font("Timer New Roman", FontWeight.BOLD, 15));

		// scoreBar
		scoreBarOUT.setArcHeight(10);
		scoreBarOUT.setArcWidth(10);
		scoreBarOUT.setFill(Color.BLACK);
		scoreBarIN.setArcHeight(7);
		scoreBarIN.setArcWidth(7);
		scoreBarIN.setWidth(0);
		scoreBarIN.setFill(Color.DARKRED);

		// score
		Main.score.setLayoutX(580);
		Main.score.setLayoutY(5);
		Main.score.setMinSize(element.scoreW, element.scoreH);
		Main.score.setFocusTraversable(false);
		Main.score.setFont(Font.font("Timer New Roman", FontWeight.NORMAL, 25));

		// scoreName
		Label scoreName = new Label("SCORE");
		scoreName.setLayoutX(480);
		scoreName.setLayoutY(5);
		scoreName.setMinSize(element.scoreW, element.scoreH);
		scoreName.setFocusTraversable(false);
		scoreName.setFont(Font.font("Timer New Roman", FontWeight.BOLD, 25));

		// level
		Main.levelContext = "LEVEL "+String.valueOf(Main.level/3+1)+"-"+String.valueOf(Main.level%3+1);
		Main.levelShow.setText(Main.levelContext);
		Main.levelShow.setLayoutX(180);
		Main.levelShow.setLayoutY(5);
		Main.levelShow.setFont(Font.font("Timer New Roman", FontWeight.BOLD, 25));

		// top pane
		topPane.setBackground(new Background(new BackgroundFill(Color.KHAKI, null, null)));
		topPane.setMinSize(element.topPaneW, element.topPaneH);
		topPane.getChildren().addAll(backBtn, pauseBtn, Main.levelShow, scoreBarOUT, scoreBarIN, Main.score, scoreName);
		for(int i=2;i<8;i+=2)
		{
			Rectangle rec = new Rectangle(340+i*10,7,20,25.5);
			rec.setFill(null);
			rec.setStroke(Color.WHITE);
			topPane.getChildren().add(rec);
		}
		Rectangle rec2 = new Rectangle(339, 6.5, 102, 27);
		rec2.setStrokeWidth(3);
		rec2.setArcHeight(10);
		rec2.setArcWidth(10);
		rec2.setStroke(Color.GRAY);
		rec2.setFill(null);
		topPane.getChildren().add(rec2);
		
		// playPane
		playPane.getChildren().add(background);

		// game pane
		gamePane.setTop(topPane);
		gamePane.setCenter(playPane);

		// game scene
		gameScene = new Scene(gamePane, element.sceneW, element.sceneH);

		// set element
		ground = new Ground(this);
		ground.setGround();
		setFirstBean();

		// start game
		startGame();

		// set on action (back button)
		Controller ctrl = new Controller(this);
		backBtn.setOnAction(ctrl.backToMenu);
		backBtn.setOnMouseEntered(ctrl.backBtnEntered);
		backBtn.setOnMouseExited(ctrl.backBtnExited);
		pauseBtn.setOnAction(ctrl.pauseGame);
		pauseBtn.setOnMouseEntered(ctrl.pauseBtnEntered);
		pauseBtn.setOnMouseExited(ctrl.pauseBtnExited);

		// Mouse control
		playPane.setOnMousePressed(ctrl.shotBean);
		playPane.setOnMouseMoved(ctrl.rollToad);
		playPane.setOnMouseDragged(ctrl.rollToad);
		Element.pause.setOnMousePressed(ctrl.goGame);

		// Key control
		gameScene.setOnKeyPressed(ctrl.switchShotBean);
	}

	private List<ImageView> beanChain = new LinkedList<ImageView>();
	private List<ImageView> beanChain2 = new LinkedList<ImageView>();
	private List<ImageView> shotBean = new LinkedList<ImageView>();
	private List<ImageView> gem = new LinkedList<ImageView>();

	private List<int[]> beanChainData = new LinkedList<int[]>(); // [color, place, ID]
	private List<int[]> beanChainData2 = new LinkedList<int[]>(); // [color, place, ID]
	private List<double[]> shotBeanData = new LinkedList<double[]>(); // [color, moveX, moveY]

	private static int ID = 0; // beanChain's ID
	private int beanChainRound = 0; // not make bean overlap
	private boolean checkForWin = false; // check for win
	private boolean checkForLose = false; // check for lose
	private int skullRound = 0; // repeat skull's move
	private int plusScore = 10; // plus score

	/***********
	 * winStar *
	 ***********/
	private List<Circle> winChain = new LinkedList<Circle>();
	private List<Circle> winChain2 = new LinkedList<Circle>();
	private List<Integer> winChainData = new LinkedList<Integer>(); // place
	private List<Integer> winChainData2 = new LinkedList<Integer>(); // place

	private int winPlace = 0; // win place
	private int winPlace2 = 0; // win place
	private int winStarCount = 0; // winStar's amount

	// time-line for winStar's move
	public Timeline winTL = new Timeline(new KeyFrame(Duration.millis(30), (e) -> {
		// add winStar
		if(winStarCount<5)
		{
			addWinChain(winStarCount, winChain, winChainData, winPlace);
			winStarCount++;
		}
		// move winStar
		moveWinChain(winChain, winChainData, ground.place);

		// for orbit3
		if(Main.level%3+1==3)
		{
			// add winStar
			if(winStarCount<5)
			{
				addWinChain(winStarCount, winChain2, winChainData2, winPlace2);
				winStarCount++;
			}
			// move winStar
			moveWinChain(winChain2, winChainData2, ground.place2);
		}
	}));

	private void win() {
		// start to run winStar
		if(winPlace<=winPlace2||Main.level%3+1!=3)
			winTL.setCycleCount((ground.place.size()-winPlace)/(Element.beanR*2)+5+1);
		else
			winTL.setCycleCount((ground.place2.size()-winPlace2)/(Element.beanR*2)+5+1);
		winTL.play();
	}

	private void addWinChain(int size, List<Circle> winChain, List<Integer> winChainData, int winPlace) {
		// winStar
		Circle win = new Circle(10+2*(5-size));
		win.setFill(Color.WHITE);
		winChain.add(win);
		winChainData.add(winPlace);
		// add to playPane
		playPane.getChildren().add(winChain.get(winChain.size()-1));
	}

	private void moveWinChain(List<Circle> winChain, List<Integer> winChainData, List<double[]> place) {
		for(int i = 0 ; i<winChain.size() ; i++)
		{
			// add score
			if(i==0)
				Main.score.setText(String.valueOf(Integer.valueOf(Main.score.getText())+Element.beanR));
			// move the winStar
			if(winChainData.get(i)<place.size()-1&&winChainData.get(i)>place.size()-1-Element.beanR*2)
			{
				int tem2 = place.size()-1;
				winChainData.add(i, tem2);
				winChainData.remove(i+1);
				winChain.get(i).setCenterX(place.get(winChainData.get(i))[0]+Element.beanR);
				winChain.get(i).setCenterY(place.get(winChainData.get(i))[1]+Element.beanR);
			}
			else
			{
				int tem = winChainData.get(i)+Element.beanR*2;
				winChainData.add(i, tem);
				winChainData.remove(i+1);
				winChain.get(i).setCenterX(place.get(winChainData.get(i))[0]+Element.beanR);
				winChain.get(i).setCenterY(place.get(winChainData.get(i))[1]+Element.beanR);
			}
			// remove the winStar that out of range
			if(winChainData.get(i)==place.size()-1)
			{
				playPane.getChildren().remove(winChain.get(i));
				winChainData.remove(i);
				winChain.remove(i);
			}
		}
	}

	/********
	 * skull *
	 ********/
	// time-line for skull's move
	public Timeline skullTL = new Timeline(new KeyFrame(Duration.millis(5), (e) -> {
		// open the skull
		if(skullRound%40<20)
		{
			Element.skullUp.setLayoutY(Element.skullUp.getLayoutY()-1);
			Element.skullDown.setLayoutY(Element.skullDown.getLayoutY()+1);
			if(Main.level%3+1==3)
			{
				Element.skullUp2.setLayoutY(Element.skullUp2.getLayoutY()-1);
				Element.skullDown2.setLayoutY(Element.skullDown2.getLayoutY()+1);
			}
			skullRound++;
		}
		// close the skull
		else if(skullRound%40<40)
		{
			Element.skullUp.setLayoutY(Element.skullUp.getLayoutY()+1);
			Element.skullDown.setLayoutY(Element.skullDown.getLayoutY()-1);
			if(Main.level%3+1==3)
			{
				Element.skullUp2.setLayoutY(Element.skullUp2.getLayoutY()+1);
				Element.skullDown2.setLayoutY(Element.skullDown2.getLayoutY()-1);
			}
			skullRound++;
		}
	}));

	/*********
	 * color *
	 *********/
	private boolean[] colorController = {true, true, true, true, true, true}; // [red, yellow, green, blue, purple, gray]

	// time-line for control the color of shotBean
	public Timeline colorControllerTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
		for(int i = 0 ; i<6 ; i++)
		{
			// no more this color
			boolean noMore = true;
			// for orbit3
			if(Main.level%3+1==3)
			{
				noMore = false;
				if(beanChain2.size()==0)
					noMore = true;
				for(int j = 0 ; j<beanChainData2.size() ; j++)
				{
					// there is a same color
					if(beanChainData2.get(j)[0]==i)
					{
						colorController[i] = true;
						break;
					}
					// no more
					if(j==beanChainData2.size()-1)
					{
						colorController[i] = false;
						noMore = true;
					}
				}
			}
			for(int j = 0 ; j<beanChainData.size() ; j++)
			{
				// there is a same color
				if(beanChainData.get(j)[0]==i)
				{
					colorController[i] = true;
					break;
				}
				// no more
				if(j==beanChainData.size()-1&&noMore==true)
					colorController[i] = false;
			}
		}
		if(shotBeanData.size()>1&&(!(skullTL.getStatus()==Timeline.Status.RUNNING)))
		{
			// change the shotBean on the toad's tongue
			if(colorController[(int)shotBeanData.get(shotBeanData.size()-2)[0]]==false)
			{
				// save shotBean's position
				double x = shotBean.get(shotBeanData.size()-2).getLayoutX();
				double y = shotBean.get(shotBeanData.size()-2).getLayoutY();
				// color
				Random random = new Random();
				int ran = 0;
				while(true)
				{
					ran = random.nextInt(6);
					if(colorController[ran]==true)
						break;
				}
				double[] tem = {ran, 0, 0};
				// change shotBean's color
				playPane.getChildren().remove(shotBean.get(shotBean.size()-2));
				shotBeanData.add(shotBeanData.size()-2, tem);
				shotBeanData.remove(shotBeanData.size()-2);
				addBeanColor(shotBean, ran, shotBean.size()-2);
				shotBean.remove(shotBean.size()-2);
				shotBean.get(shotBean.size()-2).setLayoutX(x);
				shotBean.get(shotBean.size()-2).setLayoutY(y);
				playPane.getChildren().add(shotBean.get(shotBean.size()-2));
			}
			// change the nextShotBean and next shotBean
			if(colorController[(int)shotBeanData.get(shotBeanData.size()-1)[0]]==false)
			{
				// save nextShotBean's position
				double x = gem.get(0).getLayoutX();
				double y = gem.get(0).getLayoutY();
				// color to change
				Random random = new Random();
				int ran = 0;
				while(true)
				{
					ran = random.nextInt(6);
					if(colorController[ran]==true)
						break;
				}
				double[] tem = {ran, 0, 0};
				// change gem's color
				playPane.getChildren().remove(gem.get(0));
				gem.clear();
				addBeanColor(gem, ran, 0);
				gem.get(0).setLayoutX(x);
				gem.get(0).setLayoutY(y);
				gem.get(0).setFitWidth(Element.gemR*2);
				gem.get(0).setFitHeight(Element.gemR*2);
				playPane.getChildren().add(gem.get(0));
				// change next shotBean's color
				playPane.getChildren().remove(shotBean.get(shotBean.size()-1));
				shotBeanData.add(shotBeanData.size()-1, tem);
				shotBeanData.remove(shotBeanData.size()-1);
				addBeanColor(shotBean, ran, shotBean.size()-1);
				shotBean.remove(shotBean.size()-1);
			}
		}
	}));

	/*************
	 * beanChain *
	 *************/
	// time-line for run beanChain
	public Timeline beanChainRunTL = new Timeline(new KeyFrame(Duration.millis(35), (e) -> {
		// add bean
		if(passScore>0&&beanChainRound%(Element.beanR*2)==0)
			addBeanChain();
		// shotBean's color control
		if(passScore<=0)
		{
			colorControllerTL.setCycleCount(Timeline.INDEFINITE);
			colorControllerTL.play();
			// win
			if(beanChain.size()==0&&beanChain2.size()==0&&checkForWin==false&&checkForLose==false)
			{
				checkForWin = true;
				win();
				Timeline forWinTL = new Timeline(new KeyFrame(Duration.millis(30), (k) -> {
					if(!(winTL.getStatus()==Timeline.Status.RUNNING))
						overGame();
				}));
				if(winPlace<=winPlace2||Main.level%3+1!=3)
					forWinTL.setCycleCount((ground.place.size()-winPlace)/(Element.beanR*2)+5+1);
				else
					forWinTL.setCycleCount((ground.place2.size()-winPlace2)/(Element.beanR*2)+5+1);
				forWinTL.play();
			}
		}
		// move bean
		if(beanChainRound%(Element.beanR*2)<Element.beanR*2)
		{
			moveBeanChain(beanChain, beanChainData, ground.place);
			if(Main.level%3+1==3)
				moveBeanChain(beanChain2, beanChainData2, ground.place2);
			if(beanChain.size()>0)
				winPlace = beanChainData.get(0)[1];
			if(Main.level%3+1==3&&beanChain2.size()>0)
				winPlace2 = beanChainData2.get(0)[1];
			beanChainRound++;
		}
	}));

	private void addBeanChain() {
		Random random = new Random();
		// add the bean's data
		int ran = 0;
		while(true)
		{
			ran = random.nextInt(6);
			if(colorController[ran]==true)
				break;
		}
		int[] tem = {ran, 0, ID++};
		beanChainData.add(tem);
		// add the bean to beanChain
		addBeanColor(beanChain, tem[0], beanChain.size());
		// add to playPane
		playPane.getChildren().add(beanChain.get(beanChain.size()-1));
		// for orbit3
		if(Main.level%3+1==3)
		{
			// add the bean's data
			int ran2 = 0;
			while(true)
			{
				ran2 = random.nextInt(6);
				if(colorController[ran2]==true)
					break;
			}
			int[] tem2 = {ran2, 0, ID++};
			beanChainData2.add(tem2);
			// add the bean to beanChain
			addBeanColor(beanChain2, tem2[0], beanChain2.size());
			// add to playPane
			playPane.getChildren().add(beanChain2.get(beanChain2.size()-1));
		}
	}

	private void moveBeanChain(List<ImageView> beanChain, List<int[]> beanChainData, List<double[]> place) {
		for(int i = 0 ; i<beanChain.size() ; i++)
		{
			// move the bean
			beanChainData.get(i)[1] = beanChainData.get(i)[1]+1;
			// lose
			if(beanChainData.get(i)[1]>place.size()-2)
			{
				// lose
				if(checkForLose==false)
				{
					checkForLose = true;
					beanChainRunTL.setRate(40);
					passScore = 0;
					scoreBarIN.setWidth(Math.min((1000-passScore)/10, 100));
					scoreBarIN.setFill(Color.LIGHTGREEN);
					changeSpeed.stop();
					moveBackTL.stop();
					skullTL.setCycleCount(Timeline.INDEFINITE);
					skullTL.play();
				}
				// remove
				playPane.getChildren().remove(beanChain.get(i));
				beanChainData.remove(i);
				beanChain.remove(i);
				overGame();
			}
			// move the bean
			else
			{
				beanChain.get(i).setLayoutX(place.get(beanChainData.get(i)[1])[0]);
				beanChain.get(i).setLayoutY(place.get(beanChainData.get(i)[1])[1]);
			}
		}
	}

	// time-line for change of the beanChain
	public Timeline changeSpeed = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
		// start
		if(beanChain.size()==0&&beanChain2.size()==0)
			beanChainRunTL.setRate(30);
		// normal
		if(Main.level%3+1==1)
			if(beanChain.size()==startBeanAmount1)
				beanChainRunTL.setRate(1);
		if(Main.level%3+1==2)
			if(beanChain.size()==startBeanAmount2)
				beanChainRunTL.setRate(1);
		if(Main.level%3+1==3)
			if(beanChain.size()==startBeanAmount3)
				beanChainRunTL.setRate(1);
	}));

	/************
	 * shotBean *
	 ************/
	// next shotBean's position
	private double saveX = Element.beanX;
	private double saveY = Element.beanY;

	// next gem's position
	private double saveGX = Element.gemX;
	private double saveGY = Element.gemY;

	// time-line for shoot shotBean
	public Timeline shotBeanTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
		for(int i = 0 ; i<shotBean.size()-1 ; i++)
		{
			// move the shotBean
			shotBean.get(i).setLayoutX(shotBean.get(i).getLayoutX()+shotBeanData.get(i)[1]);
			shotBean.get(i).setLayoutY(shotBean.get(i).getLayoutY()+shotBeanData.get(i)[2]);
			// remove the shotBean
			double LX = shotBean.get(i).getLayoutX();
			double LY = shotBean.get(i).getLayoutY();
			if(LX>element.sceneW||LX<0||LY>element.sceneH-element.topPaneH||LY<0)
			{
				playPane.getChildren().remove(shotBean.get(i));
				shotBeanData.remove(i);
				shotBean.remove(i);
			}
		}
	}));

	public void shotBean(double mouseX, double mouseY) {
		// next shotBean's position
		saveX = shotBean.get(shotBean.size()-2).getLayoutX();
		saveY = shotBean.get(shotBean.size()-2).getLayoutY();
		// next gem's position
		saveGX = gem.get(0).getLayoutX();
		saveGY = gem.get(0).getLayoutY();
		// the distance from toad's center
		double distanceX = mouseX-(saveX+Element.beanR);
		double distanceY = mouseY-(saveY+Element.beanR);
		if((mouseX-saveX-Element.beanR)*(mouseX-Element.toadCX)<=0)
		{
			distanceX = -distanceX;
			distanceY = -distanceY;
		}
		double distance = Math.sqrt(distanceX*distanceX+distanceY*distanceY);
		// the unit for distance
		double unitX = distanceX/distance;
		double unitY = distanceY/distance;
		// record to shotBeanData
		shotBeanData.get(shotBean.size()-2)[1] = unitX;
		shotBeanData.get(shotBean.size()-2)[2] = unitY;
	}

	public void addShotBean() {
		// next shotBean's position
		shotBean.get(shotBean.size()-1).setLayoutX(saveX);
		shotBean.get(shotBean.size()-1).setLayoutY(saveY);
		// add to playPane
		playPane.getChildren().add(shotBean.get(shotBean.size()-1));
	}

	public void gem() {
		// add the next shotBean's data
		Random random = new Random();
		int ran = 0;
		while(true)
		{
			ran = random.nextInt(6);
			if(colorController[ran]==true)
				break;
		}
		double[] tem = {ran, 0, 0};
		shotBeanData.add(tem);
		// add the next shotBean
		addBeanColor(shotBean, ran, shotBean.size());
		// add the gem
		playPane.getChildren().remove(gem.get(0));
		gem.clear();
		addBeanColor(gem, ran, gem.size());
		gem.get(0).setFitWidth(Element.gemR*2);
		gem.get(0).setFitHeight(Element.gemR*2);
		gem.get(0).setLayoutX(saveGX);
		gem.get(0).setLayoutY(saveGY);
		// add to the playPane
		playPane.getChildren().add(gem.get(0));
	}

	private void setFirstBean() {
		Random random = new Random();
		if(Main.level/3+1==1)
		{
			colorController[4] = false;
			colorController[5] = false;
		}
		if(Main.level/3+1==2)
		{
			colorController[5] = false;
		}
		// add the first shotBean's data
		int ran1 = 0;
		while(true)
		{
			ran1 = random.nextInt(6);
			if(colorController[ran1]==true)
				break;
		}
		double[] tem1 = {ran1, 0, 0};
		shotBeanData.add(tem1);
		// add the first shotBean
		addBeanColor(shotBean, ran1, shotBean.size());
		shotBean.get(0).setLayoutX(Element.beanX);
		shotBean.get(0).setLayoutY(Element.beanY);
		// add to playPane
		playPane.getChildren().add(shotBean.get(0));

		// add the next shotBean's data
		int ran2 = 0;
		while(true)
		{
			ran2 = random.nextInt(6);
			if(colorController[ran2]==true)
				break;
		}
		double[] tem2 = {ran2, 0, 0};
		shotBeanData.add(tem2);
		// add the next shotBean
		addBeanColor(shotBean, ran2, shotBean.size());
		// add the gem
		addBeanColor(gem, ran2, gem.size());
		gem.get(0).setFitWidth(Element.gemR*2);
		gem.get(0).setFitHeight(Element.gemR*2);
		gem.get(0).setLayoutX(Element.gemX);
		gem.get(0).setLayoutY(Element.gemY);
		// add to the playPane
		playPane.getChildren().add(gem.get(0));
	}

	public void roll(MouseEvent e) {
		if(e.getY()-Element.toadCY!=0&&e.getX()-Element.toadCX!=0)
		{
			// the degree between mouse and toad's center
			double degree = Math.toDegrees(Math.atan2(e.getY()-Element.toadCY, e.getX()-Element.toadCX));
			// toad's rotate
			Element.toad.setRotate(degree);
			// the distance between mouse and toad's center
			double X = e.getX()-Element.toadCX;
			double Y = e.getY()-Element.toadCY;
			double D = Math.sqrt(X*X+Y*Y);
			// the distance for shotBean
			double shotBeanX = Element.toadCX+(X/D)*element.beanD;
			double shotBeanY = Element.toadCY+(Y/D)*element.beanD;
			// the distance for gem
			double gemX = Element.toadCX-(X/D)*element.nextBeanD;
			double gemY = Element.toadCY-(Y/D)*element.nextBeanD;
			// shotBean's position
			shotBean.get(shotBean.size()-2).setLayoutX(shotBeanX-Element.beanR);
			shotBean.get(shotBean.size()-2).setLayoutY(shotBeanY-Element.beanR);
			// gem's position
			gem.get(0).setLayoutX(gemX-Element.gemR);
			gem.get(0).setLayoutY(gemY-Element.gemR);
		}
	}

	public void switchShotBean() {
		// shotBean's position
		double shotBeanX = shotBean.get(shotBean.size()-2).getLayoutX();
		double shotBeanY = shotBean.get(shotBean.size()-2).getLayoutY();
		// gem's position
		double gemX = gem.get(0).getLayoutX();
		double gemY = gem.get(0).getLayoutY();
		// change to gem
		double[] tem = shotBeanData.get(shotBean.size()-2);
		playPane.getChildren().remove(shotBean.get(shotBean.size()-2));
		shotBean.remove(shotBean.size()-2);
		shotBeanData.remove(shotBeanData.size()-2);
		shotBeanData.add(tem);
		addBeanColor(shotBean, (int)tem[0], shotBean.size());
		// change from gem
		shotBean.get(shotBean.size()-2).setLayoutX(shotBeanX);
		shotBean.get(shotBean.size()-2).setLayoutY(shotBeanY);
		playPane.getChildren().add(shotBean.get(shotBean.size()-2));
		// change gem
		playPane.getChildren().remove(gem.get(0));
		gem.clear();
		addBeanColor(gem, (int)shotBeanData.get(shotBeanData.size()-1)[0], gem.size());
		gem.get(0).setLayoutX(gemX);
		gem.get(0).setLayoutY(gemY);
		gem.get(0).setFitWidth(Element.gemR*2);
		gem.get(0).setFitHeight(Element.gemR*2);
		playPane.getChildren().add(gem.get(0));
	}

	/**********
	 * insert *
	 **********/
	// time-line for insert shotBean
	public Timeline insertBeanTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
		if(shotBean.size()>=3)
		{
			for(int i = 0 ; i<shotBean.size()-2 ; i++)
			{
				insertBean(i, beanChain, beanChainData, ground.place);
				// for orbit3
				if(Main.level%3+1==3&&shotBean.size()>=3)
					insertBean(i, beanChain2, beanChainData2, ground.place2);
			}
		}
	}));

	private void insertBean(int index, List<ImageView> beanChain, List<int[]> beanChainData, List<double[]> place) {
		for(int i = 0 ; i<beanChain.size() ; i++)
		{
			// the distance between shotBean and beanChain's bean
			double x = shotBean.get(shotBean.size()-3-index).getLayoutX()-beanChain.get(i).getLayoutX();
			double y = shotBean.get(shotBean.size()-3-index).getLayoutY()-beanChain.get(i).getLayoutY();
			double d = Math.sqrt(x*x+y*y);
			double upD = 100, downD = 100;
			// at a beanChain's bean's nearby
			if(d<=Element.beanR*2)
			{
				// the distance between shotBean and beanChain's bean's previous bean
				if(i!=0)
				{
					double upX = shotBean.get(shotBean.size()-3-index).getLayoutX()-beanChain.get(i-1).getLayoutX();
					double upY = shotBean.get(shotBean.size()-3-index).getLayoutY()-beanChain.get(i-1).getLayoutY();
					upD = Math.sqrt(upX*upX+upY*upY);
				}
				// the distance between shotBean and beanChain's bean's next bean
				if(i!=beanChain.size()-1)
				{
					double downX = shotBean.get(shotBean.size()-3-index).getLayoutX()-beanChain.get(i+1).getLayoutX();
					double downY = shotBean.get(shotBean.size()-3-index).getLayoutY()-beanChain.get(i+1).getLayoutY();
					downD = Math.sqrt(downX*downX+downY*downY);
				}
				// insert the bean
				if(i==0)
				{
					if(downD>=Element.beanR*2)
						insert(i, beanChain, beanChainData, place);
					else
						insert(i+1, beanChain, beanChainData, place);
				}
				else if(i==beanChain.size()-1)
				{
					if(upD<Element.beanR*2)
						insert(i, beanChain, beanChainData, place);
					else
						insert(i+1, beanChain, beanChainData, place);
				}
				else
				{
					if(upD<=downD)
						insert(i, beanChain, beanChainData, place);
					else
						insert(i+1, beanChain, beanChainData, place);
				}
				break;
			}
		}
	}

	private void insert(int index, List<ImageView> beanChain, List<int[]> beanChainData, List<double[]> place) {
		// add to beanChain
		beanChain.add(index, shotBean.get(shotBean.size()-3));
		// add to beanChainData
		int[] tem = {0, 0, 0};
		if(index<beanChainData.size())
		{
			tem[0] = (int)shotBeanData.get(shotBeanData.size()-3)[0];
			tem[1] = beanChainData.get(index)[1]+Element.beanR*2;
			tem[2] = ID++;
		}
		else
		{
			tem[0] = (int)shotBeanData.get(shotBeanData.size()-3)[0];
			tem[1] = beanChainData.get(index-1)[1]-Element.beanR*2;
			tem[2] = ID++;
		}
		beanChainData.add(index, tem);
		// remove shotBean from playPane
		playPane.getChildren().remove(shotBean.get(shotBean.size()-3));
		// add to playPane
		playPane.getChildren().add(beanChain.get(index));
		// remove shotBean
		shotBean.remove(shotBean.size()-3);
		shotBeanData.remove(shotBeanData.size()-3);
		// remove the bean that over 3
		removeBean(beanChain, beanChainData, place, index, plusScore, false, 1);
	}

	// time-line for move the bean before the inserted one
	public Timeline insertTL = new Timeline(new KeyFrame(Duration.millis(2), (e) -> {
		for(int i = beanChain.size()-1 ; i>0 ; i--)
		{
			// lose
			if(beanChainData.get(0)[1]+1>ground.place.size()-1)
			{
				beanChainRunTL.setRate(40);
				// remove
				playPane.getChildren().remove(beanChain.get(0));
				beanChainData.remove(0);
				beanChain.remove(0);
			}
			// there is a bean inserted
			if(i<beanChain.size())
			{
				if(beanChainData.get(i-1)[1]-beanChainData.get(i)[1]<Element.beanR*2)
				{
					beanChainData.get(i-1)[1] = beanChainData.get(i-1)[1]+1;
					beanChain.get(i-1).setLayoutX(ground.place.get(beanChainData.get(i-1)[1])[0]);
					beanChain.get(i-1).setLayoutY(ground.place.get(beanChainData.get(i-1)[1])[1]);
				}
			}
		}
		// for orbit3
		if(Main.level%3+1==3)
		{
			for(int i = beanChain2.size()-1 ; i>0 ; i--)
			{
				// lose
				if(beanChainData2.get(0)[1]+1>ground.place2.size()-1)
				{
					beanChainRunTL.setRate(40);
					// remove
					playPane.getChildren().remove(beanChain2.get(0));
					beanChainData2.remove(0);
					beanChain2.remove(0);
				}
				// there is a bean inserted
				if(i<beanChain2.size())
				{
					if(beanChainData2.get(i-1)[1]-beanChainData2.get(i)[1]<Element.beanR*2)
					{
						beanChainData2.get(i-1)[1] = beanChainData2.get(i-1)[1]+1;
						beanChain2.get(i-1).setLayoutX(ground.place2.get(beanChainData2.get(i-1)[1])[0]);
						beanChain2.get(i-1).setLayoutY(ground.place2.get(beanChainData2.get(i-1)[1])[1]);
					}
				}
			}
		}
	}));

	/**********
	 * remove *
	 **********/
	private void removeBean(List<ImageView> beanChain, List<int[]> beanChainData, List<double[]> place, int index, int plusScore, boolean check, int blank) {
		if(index>-1&&index<beanChain.size())
		{
			int count = 1; // count for remove
			int color = beanChainData.get(index)[0]; // the color to remove
			// count
			int up = index-1, down = index+1;
			boolean downCheck = false;
			for(; up>=0 ; up--)
			{
				if(beanChainData.get(up)[0]==color)
					count++;
				else
					break;
			}
			for(; down<beanChain.size() ; down++)
			{
				if(beanChainData.get(down)[0]==color)
				{
					downCheck = true;
					count++;
				}
				else
					break;
			}
			// remove
			if(count>=3&&((check==true&&downCheck==true)||check==false))
			{
				// the ID of bean
				int saveID = beanChainData.get(up+1)[2];
				// the ID of previous bean
				int nextSaveID = -1;
				if(up>-1)
					nextSaveID = beanChainData.get(up)[2];
				// mark the bean that should be remove
				for(int i = up+2 ; i<down ; i++)
					beanChainData.get(i)[2] = beanChainData.get(up+1)[2];
				// time-line for removing the bean
				Timeline removeTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
					// find the range of bean's index that is going to remove
					int start = -1, end = -1;
					for(int i = 0 ; i<beanChainData.size() ; i++)
					{
						if(beanChainData.get(i)[2]==saveID)
						{
							if(start==-1)
								start = i;
							else
								end = i;
						}
					}
					if(start!=-1)
					{
						boolean ready = true, reset = false;
						for(int i = end ; i>start ; i--)
						{
							// there is a bean that insert between them
							if(beanChainData.get(i-1)[2]!=beanChainData.get(i)[2])
							{
								reset = true;
								break;
							}
							// not come in contact
							else if(beanChainData.get(i-1)[1]-beanChainData.get(i)[1]!=Element.beanR*2)
							{
								ready = false;
								break;
							}
						}
						// reset the ID
						if(reset==true)
						{
							for(int i = end ; i>=start ; i--)
							{
								ready = false;
								beanChainData.get(i)[2] = ID;
								ID++;
							}
						}
						// remove the bean
						if(ready==true)
						{
							for(int i = 0 ; i<=end-start ; i++)
							{
								playPane.getChildren().remove(beanChain.get(start));
								beanChain.remove(start);
								beanChainData.remove(start);
								// plus score and minus passScore
								Main.score.setText(String.valueOf(Integer.valueOf(Main.score.getText())+plusScore));
								passScore -= plusScore;
								scoreBarIN.setWidth(Math.min((1000-passScore)/10, 100));
								if((1000-passScore)/10>=70)
									scoreBarIN.setFill(Color.LIGHTGREEN);
								else if((1000-passScore)/10>=30)
									scoreBarIN.setFill(Color.ORANGE);
							}
						}

					}
				}));
				removeTL.setCycleCount(Element.beanR*4*blank);
				removeTL.play();

				if(nextSaveID>-1)
				{
					int nextID = nextSaveID;
					int nextBlank = count*2;
					// remove the next bean that move back
					Timeline checkRemoveTL = new Timeline(new KeyFrame(Duration.millis(1), (e) -> {
						if(!(removeTL.getStatus()==Timeline.Status.RUNNING))
						{
							// find the index
							int nextIndex = 0;
							for(int i = 0 ; i<beanChain.size() ; i++)
							{
								if(nextID==beanChainData.get(i)[2])
									nextIndex = i;
							}
							removeBean(beanChain, beanChainData, place, nextIndex, plusScore*2, true, nextBlank);
						}
					}));
					checkRemoveTL.setCycleCount(Element.beanR*4*blank);
					checkRemoveTL.play();
				}
			}
		}
	}

	// time-line for move back the beanChain
	public Timeline moveBackTL = new Timeline(new KeyFrame(Duration.millis(4), (e) -> {
		if(passScore>0)
		{
			if(beanChainData.get(beanChainData.size()-1)[1]>40)
			{
				beanChainData.get(beanChainData.size()-1)[1] = beanChainData.get(beanChainData.size()-1)[1]-1;
				beanChain.get(beanChain.size()-1).setLayoutX(ground.place.get(beanChainData.get(beanChain.size()-1)[1])[0]);
				beanChain.get(beanChain.size()-1).setLayoutY(ground.place.get(beanChainData.get(beanChain.size()-1)[1])[1]);
			}
		}
		for(int i = beanChain.size()-1 ; i>0 ; i--)
		{
			// there is a bean that should move back
			if(beanChainData.get(i-1)[1]-beanChainData.get(i)[1]>Element.beanR*2)
			{
				beanChainData.get(i-1)[1] = beanChainData.get(i-1)[1]-1;
				beanChain.get(i-1).setLayoutX(ground.place.get(beanChainData.get(i-1)[1])[0]);
				beanChain.get(i-1).setLayoutY(ground.place.get(beanChainData.get(i-1)[1])[1]);
			}
		}
		// for orbit3
		if(Main.level%3+1==3)
		{
			if(passScore>0)
			{
				if(beanChainData2.get(beanChainData2.size()-1)[1]>40)
				{
					beanChainData2.get(beanChainData2.size()-1)[1] = beanChainData2.get(beanChainData2.size()-1)[1]-1;
					beanChain2.get(beanChain2.size()-1).setLayoutX(ground.place2.get(beanChainData2.get(beanChain2.size()-1)[1])[0]);
					beanChain2.get(beanChain2.size()-1).setLayoutY(ground.place2.get(beanChainData2.get(beanChain2.size()-1)[1])[1]);
				}
			}
			for(int i = beanChain2.size()-1 ; i>0 ; i--)
			{
				// there is a bean that should move back
				if(beanChainData2.get(i-1)[1]-beanChainData2.get(i)[1]>Element.beanR*2)
				{
					beanChainData2.get(i-1)[1] = beanChainData2.get(i-1)[1]-1;
					beanChain2.get(i-1).setLayoutX(ground.place2.get(beanChainData2.get(i-1)[1])[0]);
					beanChain2.get(i-1).setLayoutY(ground.place2.get(beanChainData2.get(i-1)[1])[1]);
				}
			}
		}
	}));

	/*********
	 * other *
	 *********/
	private void startGame() {
		// the run speed
		changeSpeed.setCycleCount(Timeline.INDEFINITE);
		changeSpeed.play();

		// run the beanChain
		beanChainRunTL.setCycleCount(Timeline.INDEFINITE);
		beanChainRunTL.play();

		// shoot the shotBean
		shotBeanTL.setCycleCount(Timeline.INDEFINITE);
		shotBeanTL.play();

		// insert bean
		insertBeanTL.setCycleCount(Timeline.INDEFINITE);
		insertBeanTL.play();

		// insert
		insertTL.setCycleCount(Timeline.INDEFINITE);
		insertTL.play();

		// move back
		moveBackTL.setCycleCount(Timeline.INDEFINITE);
		moveBackTL.play();
	}

	private void overGame() {
		if(beanChain.size()==0&&beanChain2.size()==0)
		{
			// ImageView
			Element.continue1.setFitHeight(element.continueH);
			Element.continue1.setFitWidth(element.continueW);
			Element.continue1.setVisible(false);

			Image continueImage2 = new Image("ZUMA/photo/continue1.png", false);
			ImageView continue2 = new ImageView(continueImage2);
			continue2.setFitHeight(element.continueH);
			continue2.setFitWidth(element.continueW);

			Element.menu.setFitHeight(element.menuH);
			Element.menu.setFitWidth(element.menuW);
			Element.menu.setVisible(false);

			Image menuImage1 = new Image("ZUMA/photo/menu1.png", false);
			ImageView menu1 = new ImageView(menuImage1);
			menu1.setFitHeight(element.menuH);
			menu1.setFitWidth(element.menuW);

			Element.again.setFitHeight(element.againH);
			Element.again.setFitWidth(element.againW);
			Element.again.setVisible(false);

			Image againImage1 = new Image("ZUMA/photo/again1.png", false);
			ImageView again1 = new ImageView(againImage1);
			again1.setFitHeight(element.againH);
			again1.setFitWidth(element.againW);

			Image winImage = new Image("ZUMA/photo/win1.png", false);
			ImageView win = new ImageView(winImage);
			win.setFitHeight(element.winH);
			win.setFitWidth(element.winW);
			
			Image winAllImage = new Image("ZUMA/photo/winAllLevel.png", false);
			ImageView winAll = new ImageView(winAllImage);
			winAll.setFitHeight(element.winAllH);
			winAll.setFitWidth(element.winAllW);

			Image gameOverImage = new Image("ZUMA/photo/gameOver.png", false);
			ImageView gameOver = new ImageView(gameOverImage);
			gameOver.setFitHeight(element.gameOverH);
			gameOver.setFitWidth(element.gameOverW);

			Image frameImage = new Image("ZUMA/photo/frame.png", false);
			ImageView frame = new ImageView(frameImage);
			frame.setFitWidth(element.frameW);
			frame.setFitHeight(element.frameH);

			// continue button
			Button continueBtn = new Button();
			continueBtn.setGraphic(Element.continue1);
			continueBtn.setBackground(Background.EMPTY);
			continueBtn.setLayoutX(element.frameW/2-element.continueW/2-10);
			continueBtn.setLayoutY(270);
			continue2.setLayoutX(element.frameW/2-element.continueW/2);
			continue2.setLayoutY(275);

			// menu button
			Button menuBtn = new Button();
			menuBtn.setGraphic(Element.menu);
			menuBtn.setBackground(Background.EMPTY);
			menuBtn.setLayoutX(element.frameW/2-element.menuW/2-10);
			menuBtn.setLayoutY(340);
			menu1.setLayoutX(element.frameW/2-element.menuW/2);
			menu1.setLayoutY(345);

			// again button
			Button againBtn = new Button();
			againBtn.setGraphic(Element.again);
			againBtn.setBackground(Background.EMPTY);
			againBtn.setLayoutX(element.frameW/2-element.againW/2-10);
			againBtn.setLayoutY(270);
			again1.setLayoutX(element.frameW/2-element.againW/2);
			again1.setLayoutY(275);

			// win or lose
			Button outcome = new Button();
			outcome.setBackground(Background.EMPTY);
			outcome.setLayoutY(200);

			// outcome pane
			Pane outcomePane = new Pane();
			outcomePane.setBackground(Background.EMPTY);
			outcomePane.setLayoutX(element.sceneW/2-element.frameW/2);
			outcomePane.setLayoutY(100);
			outcomePane.getChildren().addAll(frame);

			// win
			if(checkForWin==true)
			{
				again1.setVisible(false);
				againBtn.setVisible(false);
				outcome.setGraphic(win);
				outcome.setLayoutX(element.frameW/2-element.winW/2);
				if(Main.level==14)
				{
					again1.setVisible(true);
					againBtn.setVisible(true);
					continue2.setVisible(false);
					continueBtn.setVisible(false);
					outcome.setGraphic(winAll);
					outcome.setLayoutX(element.frameW/2-element.winAllW/2);
				}
			}
			// lose
			if(checkForLose==true)
			{
				continue2.setVisible(false);
				continueBtn.setVisible(false);
				outcome.setGraphic(gameOver);
				outcome.setLayoutX(element.frameW/2-element.gameOverW/2);
				
			}

			// add to pane
			outcomePane.getChildren().addAll(menu1, continue2, again1);
			outcomePane.getChildren().addAll(menuBtn, continueBtn, againBtn, outcome);
			gamePane.getChildren().add(outcomePane);

			stopTimeline();

			// set on action and mouse control
			Controller ctrl = new Controller();
			menuBtn.setOnAction(ctrl.toMenu);
			menuBtn.setOnMouseEntered(ctrl.menuBtnEntered);
			menuBtn.setOnMouseExited(ctrl.menuBtnExited);
			continueBtn.setOnAction(ctrl.continueGame);
			continueBtn.setOnMouseEntered(ctrl.continueBtnEntered);
			continueBtn.setOnMouseExited(ctrl.continueBtnExited);
			againBtn.setOnAction(ctrl.againGame);
			againBtn.setOnMouseEntered(ctrl.againBtnEntered);
			againBtn.setOnMouseExited(ctrl.againBtnExited);
		}
	}

	private void addBeanColor(List<ImageView> i, int num, int index) {
		// add to the LinkedList
		if(num==0)
			i.add(index, new ImageView(element.redImage));
		if(num==1)
			i.add(index, new ImageView(element.yellowImage));
		if(num==2)
			i.add(index, new ImageView(element.greenImage));
		if(num==3)
			i.add(index, new ImageView(element.blueImage));
		if(num==4)
			i.add(index, new ImageView(element.purpleImage));
		if(num==5)
			i.add(index, new ImageView(element.grayImage));

		// set the size
		i.get(index).setFitWidth(Element.beanR*2);
		i.get(index).setFitHeight(Element.beanR*2);
		i.get(index).setPreserveRatio(true);
		i.get(index).setSmooth(true);
		i.get(index).setCache(true);
		i.get(index).setRotate(0);
	}

	public void stopTimeline() {
		winTL.stop();
		skullTL.stop();
		colorControllerTL.stop();
		beanChainRunTL.stop();
		changeSpeed.stop();
		shotBeanTL.stop();
		insertBeanTL.stop();
		insertTL.stop();
		moveBackTL.stop();
	}
}
