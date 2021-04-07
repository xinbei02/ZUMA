package ZUMA;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Element {

	public Element() {
	}

	// scene, button, label, pane's size
	public static Image background1Image = new Image("zuma/photo/background1.png", false);
	public static Image background2Image = new Image("zuma/photo/background2.png", false);
	public static Image pauseImage = new Image("zuma/photo/pause.png", false);
	public static ImageView pause = new ImageView(pauseImage);
	public int sceneW = 700;
	public int sceneH = 640;
	public int topPaneW = sceneW;
	public int topPaneH = 40;
	public int backW = 50;
	public int backH = 30;
	public int scoreW = 100;
	public int scoreH = 30;

	//// cover
	public static Image coverImage = new Image("zuma/photo/cover.png", false);
	public static ImageView cover = new ImageView(coverImage);
	public static Image startImage = new Image("zuma/photo/start.png", false);
	public static ImageView start = new ImageView(startImage);
	public static Image exitImage = new Image("zuma/photo/exit.png", false);
	public static ImageView exit = new ImageView(exitImage);
	// data
	public double coverW = 485.8;
	public double coverH = 640;
	public double startW = 242.1;
	public double startH = 0;
	public double exitW = 0;
	public double exitH = 59.9;
	public double startBtnW = 243.1;
	public double startBtnH = 59.9;
	public double exitBtnW = 150.08;
	public double exitBtnH = 59.9;

	// outcome
	public static Image continueImage1 = new Image("zuma/photo/continue.png", false);
	public static ImageView continue1 = new ImageView(continueImage1);
	public static Image menuImage = new Image("zuma/photo/menu.png", false);
	public static ImageView menu = new ImageView(menuImage);
	public static Image againImage = new Image("zuma/photo/again.png", false);
	public static ImageView again = new ImageView(againImage);
	public int frameW = 615;
	public int frameH = 450;
	public double continueW = 315.5;
	public double continueH = 50;
	public double menuW = 197.7;
	public double menuH = 50;
	public double againW = 377.4;
	public double againH = 50;
	public double gameOverW = 410.1;
	public double gameOverH = 55;
	public double winW = 114.9;
	public double winH = 55;
	public double winAllW = 327.1;
	public double winAllH = 55;

	//// toad
	public static Image toadImage = new Image("zuma/photo/toad.png", false);
	public static ImageView toad = new ImageView(toadImage);
	// data
	public static int toadR = 60; // size
	public static int toadCX, toadCY, toadX, toadY; // center, position
	// level 1 ..... 330,320
	// level 2 ..... 370,330
	// level 3 ..... 300,280

	//// bean
	// red
	public Image redImage = new Image("zuma/photo/red.png", false);
	public ImageView red = new ImageView(redImage);
	// yellow
	public Image yellowImage = new Image("zuma/photo/yellow.png", false);
	public ImageView yellow = new ImageView(yellowImage);
	// green
	public Image greenImage = new Image("zuma/photo/green.png", false);
	public ImageView green = new ImageView(greenImage);
	// blue
	public Image blueImage = new Image("zuma/photo/blue.png", false);
	public ImageView blue = new ImageView(blueImage);
	// purple
	public Image purpleImage = new Image("zuma/photo/purple.png", false);
	public ImageView purple = new ImageView(purpleImage);
	// gray
	public Image grayImage = new Image("zuma/photo/gray.png", false);
	public ImageView gray = new ImageView(grayImage);
	// data
	public static int beanR = 20; // size
	public static int beanCX, beanCY, beanX, beanY; // center, position
	public static int gemR = 8; // size
	public static double nextBeanCX, nextBeanCY, gemX, gemY; // center, position
	public int beanD = 40, nextBeanD = 30;

	//// skull
	// out
	public static Image skullOutImage = new Image("zuma/photo/skullOut.png", false);
	public static ImageView skullOut = new ImageView(skullOutImage);
	public static ImageView skullOut2 = new ImageView(skullOutImage);
	// up
	public static Image skullUpImage = new Image("zuma/photo/skullUp.png", false);
	public static ImageView skullUp = new ImageView(skullUpImage);
	public static ImageView skullUp2 = new ImageView(skullUpImage);
	// down
	public static Image skullDownImage = new Image("zuma/photo/skullDown.png", false);
	public static ImageView skullDown = new ImageView(skullDownImage);
	public static ImageView skullDown2 = new ImageView(skullDownImage);
	// hole
	public Circle hole = new Circle(25);
	public Circle hole2 = new Circle(25);
	// data
	public static int skullR = 50; // size
	public static double skullCX, skullCY, skullX, skullY; // center, position
	public static double skull2CX = 0, skull2CY = 0, skull2X, skull2Y; // center, position
	// level 1 .... 150.6+beanR, 321.3+beanR
	// level 2 .... 429.65+beanR, 478.7+beanR
	// level 3 .... 479.6+beanR, 180.0+beanR; / 180.4+beanR, 380.0+beanR
}
