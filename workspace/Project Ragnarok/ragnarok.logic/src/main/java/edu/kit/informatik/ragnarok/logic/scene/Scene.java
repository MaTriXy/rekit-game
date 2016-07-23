package edu.kit.informatik.ragnarok.logic.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.PriorityQueueIterator;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gui.GuiElement;

/**
 * Based on the concept of scenes in Unity. </br> "Scenes contain the objects of
 * your game. They can be used to create a main menu, individual levels, and
 * anything else. Think of each unique Scene file as a unique level. In each
 * Scene, you will place your environments, obstacles, and decorations,
 * essentially designing and building your game in pieces." <a href="
 * https://docs.unity3d.com/Manual/CreatingScenes.html">Unity Manual</a> </p> A
 * new Scene needs an entry in {@link Scenes} and a method with the Signature:
 * {@code public static Scene create(GameModel, String[])}, for the GameModel to
 * be able to start that Scene.</br> For Scene switching take a look at
 * {@link GameModel#switchScene(Scenes, String[])}
 *
 *
 * @author Matthias Schmitt
 *
 * @version 1.0
 */
public abstract class Scene implements CameraTarget {

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	private final Object sync = new Object();

	protected GameModel model;

	private PriorityQueue<GuiElement> guiElements;

	private PriorityQueue<GameElement> gameElements;

	private ArrayList<GameElement> gameElementAddQueue;

	private ArrayList<GameElement> gameElementRemoveQueue;

	private Map<Class<?>, Long> gameElementDurations = new HashMap<>();

	public Scene(GameModel model) {
		this.model = model;
	}

	/**
	 * Initialize the scene. e.g. build Level/GUI so Scene is ready to be drawn
	 * Must be called on restart.
	 */
	public void init() {
		this.guiElements = new PriorityQueue<>();
		this.gameElements = new PriorityQueue<>();
		this.gameElementAddQueue = new ArrayList<>();
		this.gameElementRemoveQueue = new ArrayList<>();
	}

	/**
	 * Start the scene. Begin drawing and Player/Enemies will begin to move.
	 */
	public void start() {
	}

	public void end(boolean won) {
		this.model.switchScene(Scenes.MENU);
	}

	public void stop() {
	}

	public void restart() {
		this.init();
		this.start();
	}

	public void logicLoop(float timeDelta) {

		this.logicLoopPre(timeDelta);

		// add GameElements that have been added
		this.addGameElements();

		// iterate all GameElements to invoke logicLoop
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.getGameElementIterator();

			while (it.hasNext()) {
				this.logicLoopGameElement(timeDelta, it);
			}
		}

		// remove GameElements that must be removed
		this.removeGameElements();

		this.logicLoopAfter();

		// after all game related logic update GuiElements
		synchronized (this.synchronize()) {
			Iterator<GuiElement> it = this.getGuiElementIterator();
			while (it.hasNext()) {
				GuiElement e = it.next();
				e.logicLoop(timeDelta);
			}
		}

	}

	protected void logicLoopAfter() {
	}

	protected void logicLoopPre(float timeDelta) {
	}

	protected void logicLoopGameElement(float timeDelta, Iterator<GameElement> it) {
		GameElement e = it.next();

		// if this GameElement is marked for destruction
		if (e.getDeleteMe()) {
			it.remove();
		}

		// Debug: Save time before logicLoop
		long timeBefore;
		if (GameConf.DEBUG) {
			timeBefore = System.currentTimeMillis();

		}

		e.logicLoop(timeDelta);

		// Debug: Compare and save logicLoop Duration
		if (GameConf.DEBUG) {
			long timeAfter = System.currentTimeMillis();
			Class<?> clazz = e.getClass();
			long dur = (timeAfter - timeBefore);
			if (this.gameElementDurations.containsKey(clazz)) {
				long newTime = this.gameElementDurations.get(clazz) + dur;
				this.gameElementDurations.put(clazz, newTime);
			} else {
				this.gameElementDurations.put(clazz, dur);
			}
		}
	}

	/**
	 * Adds a GameElement to the Model. The elements will not directly be added
	 * to the internal data structure to prevent concurrency errors. Instead
	 * there is an internal list to hold all waiting GameElements that will be
	 * added in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to add
	 */
	public void addGameElement(GameElement element) {
		// Put GameElement in waiting list
		synchronized (this.synchronize()) {
			this.gameElementAddQueue.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementAddQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				this.gameElements.add(element);
				element.setScene(this);

				it.remove();
			}
		}
	}

	/**
	 * Removes a GameElement from the Model The elements will not directly be
	 * removed from the internal data structure to prevent concurrency errors.
	 * Instead there is an internal list to hold all waiting GameElements that
	 * will be removed in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to remove
	 */
	public void removeGameElement(GameElement element) {
		synchronized (this.synchronize()) {
			this.gameElementRemoveQueue.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementRemoveQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				it.remove();
				this.gameElements.remove(element);
			}
		}
	}

	public Iterator<GameElement> getOrderedGameElementIterator() {
		return new PriorityQueueIterator<>(this.gameElements);
	}

	public Iterator<GameElement> getGameElementIterator() {
		return this.gameElements.iterator();
	}

	/**
	 * Adds a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	public void addGuiElement(GuiElement e) {
		this.guiElements.add(e);
	}

	public void removeGuiElement(GuiElement e) {
		this.guiElements.remove(e);
	}

	public Iterator<GuiElement> getGuiElementIterator() {
		return this.guiElements.iterator();
	}

	@Override
	public float getCameraOffset() {
		return 0;
	}

	public void setCameraTarget(CameraTarget cameraTarget) {

	}

	public int getGameElementCount() {
		return this.gameElements.size();
	}

	public Object synchronize() {
		return this.sync;
	}

	public Player getPlayer() {
		return null;
	}

	public long getTime() {
		return 0;
	}

	public Map<Class<?>, Long> getGameElementDurations() {
		// Reset debug info
		Map<Class<?>, Long> ret = this.gameElementDurations;
		gameElementDurations = new HashMap<>();
		return ret;
	}

}
