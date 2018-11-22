package fr.irit.amak.dramas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.DrawableUI;

/**
 * This class is used to display the environment and the drones
 *
 */
public class WorldViewer extends DrawableUI<DrAmas>{

	/**
	 * The size of the areas
	 */
	public static final int AREA_SIZE = 10;

	/**
	 * Constructor
	 * 
	 * @param _drAmas
	 *            the AMAS
	 */
	public WorldViewer(DrAmas _drAmas) {
		/**
		 * Auto start the rendering thread and allow control on it
		 */
		super(Scheduling.DEFAULT, _drAmas);
	}

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Display the state of the system
	 */
	@Override
	protected void onDraw(Graphics2D arg0) {
		if (getAmas() != null) {
			// Draw areas
			arg0.setColor(Color.gray);
			for (int x = 0; x < getAmas().getEnvironment().getAreas()[0].length; x++) {
				for (int y = 0; y < getAmas().getEnvironment().getAreas().length; y++) {
					Area area = getAmas().getEnvironment().getAreas()[y][x];
					double timeSinceLastSeen = area.getTimeSinceLastSeen();
					if (timeSinceLastSeen > 1000)
						timeSinceLastSeen = 1000;
					arg0.setColor(
							new Color((float) timeSinceLastSeen / 1000f, 1 - (float) timeSinceLastSeen / 1000f, 0f));
					arg0.fillRect((int) discreteToTopContinuous(x), (int) discreteToTopContinuous(y), AREA_SIZE,
							AREA_SIZE);
				}
			}

			// Draw agents
			ArrayList<Agent<?, World>> agents = new ArrayList<>(getAmas().getAgents());
			for (Agent<?, World> agent : agents) {
				Drone drone = (Drone) agent;
				arg0.setColor(Color.white);
				arg0.fillOval((int) discreteToTopContinuous(drone.getX()), (int) discreteToTopContinuous(drone.getY()),
						AREA_SIZE, AREA_SIZE);
				arg0.setColor(Color.black);
				arg0.drawOval((int) discreteToTopContinuous(drone.getX()), (int) discreteToTopContinuous(drone.getY()),
						AREA_SIZE, AREA_SIZE);
			}
		}
	}

	/**
	 * Helper function aiming at converting a discrete value to a screen value
	 * 
	 * @param dx
	 * @return
	 */
	public static double discreteToTopContinuous(int dx) {
		return (dx) * AREA_SIZE;
	}

	/**
	 * When dragging the mouse, add critical areas
	 */
	@Override
	protected void onMouseDragged(int x, int y) {
		for (int rx = -2; rx <= 2; rx++)
			for (int ry = -2; ry <= 2; ry++)
				getAmas().getEnvironment().getAreaByPosition(x / AREA_SIZE + rx, y / AREA_SIZE + ry).setCritical();
	}

}
