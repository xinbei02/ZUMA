package ZUMA;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ground {

	public Element element = new Element();
	public Game game;

	public Ground(Game g) {
		game = g;
		level();
	}

	private void level() {
		// level 1
		if(Main.level%3+1==1)
		{
			Game.startBeanAmount1 += 6*(Main.level/3+1);
			Element.toadCX = 360;
			Element.toadCY = 320;
			Element.skullCX = 150.6+Element.beanR;
			Element.skullCY = 321.3+Element.beanR;
			orbit1();
		}
		// level 2
		if(Main.level%3+1==2)
		{
			Game.startBeanAmount2 += 4*(Main.level/3+1);
			Element.toadCX = 370;
			Element.toadCY = 330;
			Element.skullCX = 429.65+Element.beanR;
			Element.skullCY = 478.7+Element.beanR;
			orbit2();
		}
		// level 3
		if(Main.level%3+1==3)
		{
			Game.startBeanAmount3 += 3*(Main.level/3+1);
			Element.toadCX = 350;
			Element.toadCY = 300;
			Element.skullCX = 479.6+Element.beanR;
			Element.skullCY = 180.0+Element.beanR;
			Element.skull2CX = 180.4+Element.beanR;
			Element.skull2CY = 380.0+Element.beanR;
			orbit3();
		}
		Element.toadX = Element.toadCX-Element.toadR;
		Element.toadY = Element.toadCY-Element.toadR;
		Element.skullX = Element.skullCX-Element.skullR;
		Element.skullY = Element.skullCY-Element.skullR;
		Element.skull2X = Element.skull2CX-Element.skullR;
		Element.skull2Y = Element.skull2CY-Element.skullR;
		Element.beanCX = Element.toadCX+element.beanD;
		Element.beanCY = Element.toadCY;
		Element.beanX = Element.beanCX-Element.beanR;
		Element.beanY = Element.beanCY-Element.beanR;
		Element.nextBeanCX = Element.toadCX-element.nextBeanD;
		Element.nextBeanCY = Element.toadCY;
		Element.gemX = Element.nextBeanCX-Element.gemR;
		Element.gemY = Element.nextBeanCY-Element.gemR;
	}

	public List<double[]> place = new LinkedList<double[]>(); // [x, y]
	public List<double[]> place2 = new LinkedList<double[]>(); // [x, y]

	public void orbit1() {
		for(double t = 16.6 ; t>5.8 ; t -= 0.00005)
		{
			double x = 295-25*t*Math.cos(1.1*t);
			double y = 310+19*t*Math.sin(1.1*t);
			if(t==16.6)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
			else
			{
				double dX = Math.pow(x-(place.get(place.size()-1)[0]), 2);
				double dY = Math.pow(y-(place.get(place.size()-1)[1]), 2);
				double d = Math.sqrt(dX+dY);
				if(d>0.95&&d<=1)
				{
					double[] tem = {x, y};
					place.add(tem);
				}
			}
		}
	}

	private void orbit2() {
		for(double x = 25 ; x<620 ; x += 0.01)
		{
			double y = (x-40)*(x-660)/180+550;
			if(x==25)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
			else
			{
				double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
				double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
				double d = dX+dY;
				if(d>0.9&&d<=1)
				{
					double[] tem = {x, y};
					place.add(tem);
				}
			}
		}
		for(double y = 420.81116666540754 ; y<430 ; y += 0.01)
		{
			double x = -(y-420.81116666540754)*(y-440.81116666540754)/400+619.8999999995801;
			if(y==420.81116666540754)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
			else
			{
				double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
				double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
				double d = dX+dY;
				if(d>0.9&&d<=1)
				{
					double[] tem = {x, y};
					place.add(tem);
				}
			}
		}
		for(double y = 429.36116666539976 ; y<450 ; y += 0.01)
		{
			double x = -(y-429.36116666539976)*(y-429.36116666539976)/100+620.1447437495801;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double y = 449.7911666653812 ; y<460 ; y += 0.01)
		{
			double x = -(y-449.7911666653812)*(y-430.7911666653812)/50+615.9708947495877;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 610.2642067495947 ; x>600 ; x -= 0.01)
		{
			double y = -(x-610.2642067495947)*(x-450)/180+459.6711666653722;

			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 600.004206749604 ; x>570 ; x -= 0.01)
		{
			double y = -(x-600.004206749604)*(x-580)/180+468.2214064500919;

			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 570.6442067496307 ; x>550 ; x -= 0.01)
		{
			double y = -(x-570.6442067496307)*(x-630)/100+466.6953726176997;

			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 550.524206749649 ; x>500 ; x -= 0.01)
		{
			double y = -(x-550.524206749649)*(x-700)/100+450.7048430157436;

			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 500.3142067496947 ; x>160 ; x -= 0.01)
		{
			double y = (x-500.3142067496947)*(x-200)/100+350.4426062248565;

			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double y = 486.47623307514357 ; y<500 ; y += 0.01)
		{
			double x = (y-486.47623307514357)*(y-486.47623307514357)/500+160.02420675000417;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double y = 499.7762330751315 ; y<510 ; y += 0.01)
		{
			double x = (y-499.7762330751315)*(y-450)/200+160.37798675000352;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double y = 509.81623307512234 ; y<530 ; y += 0.01)
		{
			double x = (y-509.81623307512234)*(y-450)/100+163.38076165037194;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double y = 529.9162330751041 ; y<540 ; y += 0.01)
		{
			double x = (y-529.9162330751041)*(y-400)/100+179.44392449845324;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
		for(double x = 193.465535052565 ; x<430 ; x += 0.01)
		{
			double y = -(x-193.465535052565)*(x-300)/500+539.936233075095;
			double dX = Math.pow(x-place.get(place.size()-1)[0], 2);
			double dY = Math.pow(y-place.get(place.size()-1)[1], 2);
			double d = dX+dY;
			if(d>0.9&&d<=1)
			{
				double[] tem = {x, y};
				place.add(tem);
			}
		}
	}

	private void orbit3() {
		for(double x = -40 ; x<620. ; x += 0.95)
		{
			double y = 40;
			double[] tem = {x, y};
			place.add(tem);
		}
		for(double y = 41 ; y<450 ; y += 0.95)
		{
			double x = 620;
			double[] tem = {x, y};
			place.add(tem);
		}
		for(double x = 619 ; x>110. ; x -= 0.95)
		{
			double y = 450;
			double[] tem = {x, y};
			place.add(tem);
		}
		for(double y = 449 ; y>180 ; y -= 0.95)
		{
			double x = 110;
			double[] tem = {x, y};
			place.add(tem);
		}
		for(double x = 111 ; x<480. ; x += 0.95)
		{
			double y = 180;
			double[] tem = {x, y};
			place.add(tem);
		}

		for(double x = 700 ; x>40. ; x -= 0.95)
		{
			double y = 520;
			double[] tem = {x, y};
			place2.add(tem);
		}
		for(double y = 519 ; y>110 ; y -= 0.95)
		{
			double x = 40;
			double[] tem = {x, y};
			place2.add(tem);
		}
		for(double x = 41 ; x<550. ; x += 0.95)
		{
			double y = 109;
			double[] tem = {x, y};
			place2.add(tem);
		}
		for(double y = 110 ; y<380 ; y += 0.95)
		{
			double x = 550;
			double[] tem = {x, y};
			place2.add(tem);
		}
		for(double x = 549 ; x>180 ; x -= 0.95)
		{
			double y = 380;
			double[] tem = {x, y};
			place2.add(tem);
		}
	}

	private void setToad() {
		Element.toad.setFitWidth(Element.toadR*2);
		Element.toad.setFitHeight(Element.toadR*2);
		Element.toad.setLayoutX(Element.toadX);
		Element.toad.setLayoutY(Element.toadY);
		Element.toad.setPreserveRatio(true);
		Element.toad.setSmooth(true);
		Element.toad.setCache(true);
		Element.toad.setRotate(0);
		game.playPane.getChildren().add(Element.toad);
	}

	private void setDead() {
		element.hole.setLayoutX(Element.skullCX);
		element.hole.setLayoutY(Element.skullCY);
		game.playPane.getChildren().add(element.hole);
		skullImage(Element.skullUp, Element.skullR, Element.skullX, Element.skullY);
		skullImage(Element.skullDown, Element.skullR, Element.skullX, Element.skullY);
		skullImage(Element.skullOut, Element.skullR, Element.skullX, Element.skullY);
		if(Main.level%3+1==3)
		{
			element.hole2.setLayoutX(Element.skull2CX);
			element.hole2.setLayoutY(Element.skull2CY);
			game.playPane.getChildren().add(element.hole2);
			skullImage(Element.skullUp2, Element.skullR, Element.skull2X, Element.skull2Y);
			skullImage(Element.skullDown2, Element.skullR, Element.skull2X, Element.skull2Y);
			skullImage(Element.skullOut2, Element.skullR, Element.skull2X, Element.skull2Y);
		}
	}

	private void skullImage(ImageView i, int R, double X, double Y) {
		i.setFitWidth(R*2);
		i.setFitHeight(R*2);
		i.setLayoutX(X);
		i.setLayoutY(Y);
		i.setPreserveRatio(true);
		i.setSmooth(true);
		i.setCache(true);
		i.setRotate(0);
		game.playPane.getChildren().add(i);
	}

	private void background() {
		if(Main.level/3+1<5)
		{
			for(int i = place.size()-1 ; i>=0 ; i--)
			{
				Circle road = new Circle(15);
				if(i%10==0)
					road.setFill(Color.DARKGREY);
				else
					road.setFill(Color.GRAY);
				road.setCenterX(place.get(i)[0]+20);
				road.setCenterY(place.get(i)[1]+20);
				game.playPane.getChildren().add(road);
			}
			if(Main.level%3+1==3)
			{
				for(int i = 0 ; i<place2.size() ; i++)
				{
					Circle road = new Circle(15);
					if(i%10==0)
						road.setFill(Color.DARKGREY);
					else
						road.setFill(Color.GRAY);
					road.setCenterX(place2.get(i)[0]+20);
					road.setCenterY(place2.get(i)[1]+20);
					game.playPane.getChildren().add(road);
				}
			}
		}
	}

	public void setGround() {
		background();
		setToad();
		setDead();
	}
}
