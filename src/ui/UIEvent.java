package Parasite.ui;

public class UIEvent {

	// eventCode codes
	public static final int MOVE_UP = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_LEFT = 2;
	public static final int MOVE_RIGHT = 3;

	public static final int STOP_UP = 4;
	public static final int STOP_DOWN = 5;
	public static final int STOP_LEFT = 6;
	public static final int STOP_RIGHT = 7;

	public int  eventCode;
	public int[]    iargs;
	public double[] dargs;

	public UIEvent(int eventCode, int[] iargs, double[] dargs) {
		this.eventCode = eventCode;
		this.iargs = iargs;
		this.dargs = dargs;
	}

	public UIEvent(int eventCode, double[] dargs) {
		this.eventCode = eventCode;
		this.dargs = dargs;
	}

	public UIEvent(int eventCode, int[] iargs) {
		this.eventCode = eventCode;
		this.iargs = iargs;
	}

	public UIEvent(int eventCode) {
		this.eventCode = eventCode;
	}
}
