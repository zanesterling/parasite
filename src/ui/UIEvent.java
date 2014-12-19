package Parasite.ui;

public class UIEvent {

	public EventCode eventCode;
	public int[]     iargs;
	public double[]  dargs;

	public UIEvent(EventCode eventCode, int[] iargs, double[] dargs) {
		this.eventCode = eventCode;
		this.iargs = iargs;
		this.dargs = dargs;
	}

	public UIEvent(EventCode eventCode, double[] dargs) {
		this.eventCode = eventCode;
		this.dargs = dargs;
	}

	public UIEvent(EventCode eventCode, int[] iargs) {
		this.eventCode = eventCode;
		this.iargs = iargs;
	}

	public UIEvent(EventCode eventCode) {
		this.eventCode = eventCode;
	}
}
