package sim.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import sim.participants.Car;
import sim.participants.DataFactory;
import sim.participants.Intersection;
import sim.participants.Road;
import sim.participants.SimulationProperties;



/** 
 * A class for building Animators.
 */
public class SwingAnimatorBuilder implements AnimatorBuilder {
	MyPainter _painter;
	public SwingAnimatorBuilder() {
		_painter = new MyPainter();
	}
	public Animator getAnimator() {
		if (_painter == null) { throw new IllegalStateException(); }
		Animator returnValue = new SwingAnimator(_painter, "Traffic Simulator",
				VP.displayWidth, VP.displayHeight, VP.displayDelay);
		_painter = null;
		return returnValue;
	}

	private static double skipInit = VP.gap;
	private static double skipRoad = VP.gap + SimulationProperties.constantGUIroadLength;
	private static double skipCar = VP.gap + VP.elementWidth;
	private static double skipRoadCar = skipRoad + skipCar;

	public void addLight(Intersection d, int i, int j) {
		double x = skipInit + skipRoad + j*skipRoadCar;
		double y = skipInit + skipRoad + i*skipRoadCar;
		Translator t = new TranslatorWE(x, y, SimulationProperties.constantGUIcarLength, VP.elementWidth, VP.scaleFactor);
		_painter.addLight(d,t);
	}
	public void addHorizontalRoad(Road l, int i, int j, boolean eastToWest) {
		double x = skipInit + j*skipRoadCar;
		double y = skipInit + skipRoad + i*skipRoadCar;
		Translator t = eastToWest ? new TranslatorEW(x, y, SimulationProperties.constantGUIroadLength, VP.elementWidth, VP.scaleFactor)
				: new TranslatorWE(x, y, SimulationProperties.constantGUIroadLength, VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l,t);
	}
	public void addVerticalRoad(Road l, int i, int j, boolean southToNorth) {
		double x = skipInit + skipRoad + j*skipRoadCar;
		double y = skipInit + i*skipRoadCar;
		Translator t = southToNorth ? new TranslatorSN(x, y, SimulationProperties.constantGUIroadLength, VP.elementWidth, VP.scaleFactor)
				: new TranslatorNS(x, y, SimulationProperties.constantGUIroadLength, VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l,t);
	}


	/** Class for drawing the Model. */
	private static class MyPainter implements SwingAnimatorPainter {

		/** Pair of a model element <code>x</code> and a translator <code>t</code>. */
		private static class Element<T> {
			T x;
			Translator t;
			Element(T x, Translator t) {
				this.x = x;
				this.t = t;
			}
		}

		private List<Element<Road>> _roadElements;
		private List<Element<Intersection>> _lightElements;
		MyPainter() {
			_roadElements = new ArrayList<Element<Road>>();
			_lightElements = new ArrayList<Element<Intersection>>();
		}    
		void addLight(Intersection x, Translator t) {
			_lightElements.add(new Element<Intersection>(x,t));
		}
		void addRoad(Road x, Translator t) {
			_roadElements.add(new Element<Road>(x,t));
		}

		public void paint(Graphics g) {
			// This method is called by the swing thread, so may be called
			// at any time during execution...

			// First draw the background elements
			for (Element<Intersection> e : _lightElements) {
				if (e.x.getLight().getLightstate() == DataFactory.LIGHT_STATE.EW_GREEN)
					g.setColor(Color.GREEN);
				else if (e.x.getLight().getLightstate() == DataFactory.LIGHT_STATE.EW_YELLOW)
					g.setColor(Color.YELLOW);
				else if (e.x.getLight().getLightstate() == DataFactory.LIGHT_STATE.NS_YELLOW)
					g.setColor(Color.ORANGE);
				else if (e.x.getLight().getLightstate() == DataFactory.LIGHT_STATE.NS_GREEN)
					g.setColor(Color.MAGENTA);
				else
					g.setColor(Color.RED);
				XGraphics.fillOval(g, e.t, 0, 0, SimulationProperties.constantGUIcarLength, VP.elementWidth);
			}
			g.setColor(Color.BLACK);
			for (Element<Road> e : _roadElements) {
				XGraphics.fillRect(g, e.t, 0, 0, SimulationProperties.constantGUIroadLength, VP.elementWidth);
			}

			// Then draw the foreground elements
			for (Element<Road> e : _roadElements) {
				// iterate through a copy because e.x.getCars() may change during iteration...
				try {
					for (Car d : ((Road) e.x).getCars().toArray(new Car[0])) {
						
						g.setColor(d.getColor());
						double tempEp = d.getCurrentRoad().getEndPosition();
						double tempRl = SimulationProperties.constantGUIroadLength;
						double tempCl = d.getLengthOfCar();
						double tempBp = d.getbackPosition();
						XGraphics.fillOval(g, e.t, (tempBp * tempRl / tempEp),0,(tempCl*tempRl / tempEp),VP.elementWidth);
						//XGraphics.fillOval(g, e.t, d.getfrontPosition(), 0, SimulationProperties.GUIroadLength, VP.elementWidth);
					}
				} catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//System.out.println("e  "+ ((Road) e.x));
				}
			}
		}
	}
}

