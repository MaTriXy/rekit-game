package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate.IdleState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate.State;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

@LoadMe
@VisitInfo(res = "conf/canon", visit = true)
public class Canon extends Enemy implements Visitable, StateMachine {

	private static Vec SIZE;
	public static float STATE_IDLE_DURATION;
	public static float STATE_AIMING_DURATION;
	public static float STATE_SHOOTING_DURATION;
	
	public static float ANGLE_SPEED;
	
	public static RGBColor COLOR_BASE;
	public static RGBColor COLOR_CANON;
	
	@NoVisit
	private State currentState;
	
	@NoVisit
	private float currentAngle;
	
	@NoVisit
	private Polygon polygon;
	
	/**
	 * Prototype constructor
	 */
	public Canon() {
	}
	
	public Canon(Vec pos) {
		super(pos.addY(-0.5f + SIZE.getY() / 2f), new Vec(), SIZE);
		
		this.currentState = new IdleState();
		this.currentState.enter(this);
		this.currentAngle = currentState.getTargetAngle();
		
		float x = 0.3f;
		float y = 0.9f;
		this.polygon = new Polygon(this.getPos(), new Vec[]{
			new Vec(x/2, 0),
			new Vec(x/2, y),
			new Vec(-x/2, y),
			new Vec(-x/2, 0),
			new Vec(0, 0)
		});
	}
	
	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new Canon(startPos);
	}
	
	@Override
	public void internalRender(Field f) {

		f.drawCircle(this.getPos(), this.getSize(), COLOR_BASE);
		f.drawPolygon(this.polygon.rotate(-this.currentAngle, this.getPos()), COLOR_CANON, true);
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.currentState.logicLoop(deltaTime);
		
		// move angle in right direction
		this.currentAngle += Math.signum(this.currentState.getTargetAngle() - this.currentAngle) * deltaTime * ANGLE_SPEED;
		
		if (Math.abs(this.currentState.getTargetAngle() - this.currentAngle) < ANGLE_SPEED / 10) {
			this.currentAngle = this.currentState.getTargetAngle();
		}

	}

	@Override
	public void setNextState(State next) {
		currentState.leave();
		next.enter(this);
		currentState = next;
	}
	
}
