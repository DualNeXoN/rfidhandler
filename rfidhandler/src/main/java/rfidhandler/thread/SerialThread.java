package rfidhandler.thread;

public class SerialThread extends Thread {
	
	protected boolean running = true;
	
	public void kill() {
		running = false;
		interrupt();
	}
	
}
