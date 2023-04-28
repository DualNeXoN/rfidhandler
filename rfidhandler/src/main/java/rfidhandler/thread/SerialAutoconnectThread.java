package rfidhandler.thread;

import rfidhandler.App;
import rfidhandler.ConnectionStatus;
import rfidhandler.utils.PortDetector;

public class SerialAutoconnectThread extends SerialThread {
	
    @Override
    public void run() {
    	while(running) {
    		if(!App.get().isSerialConnected()) {
    			App.get().setConnectionStatus(ConnectionStatus.DISCONNECTED);
    			tryConnect();
    		} else {
    			App.get().setConnectionStatus(ConnectionStatus.CONNECTED);
    		}
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
    	}
    }
    
    private boolean tryConnect() {
    	String portName;
    	if((portName = PortDetector.detectArduinoPort()) != null) {
			App.get().connect(portName);
			return true;
		}
    	return false;
    }
    
}
