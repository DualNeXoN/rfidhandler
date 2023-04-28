package rfidhandler.thread;

import com.fazecast.jSerialComm.SerialPort;

import javafx.application.Platform;
import rfidhandler.App;
import rfidhandler.RfidUid;
import rfidhandler.utils.PortDetector;

public class SerialReaderThread extends SerialThread {
	
    private SerialPort serialPort;

    public SerialReaderThread(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(9600);
        serialPort.openPort();
    }

    @Override
    public void run() {
        try {
            while(!isInterrupted()) {
            	while(serialPort.bytesAvailable() < 4 && !isInterrupted()) Thread.sleep(20);
            	if(isInterrupted()) break;
                byte[] buffer = new byte[4];
                serialPort.readBytes(buffer, buffer.length);
                RfidUid rfidUid = new RfidUid(buffer);
                Platform.runLater(new Runnable() {
					@Override
					public void run() {
						App.get().loadAnimal(rfidUid.getUidString());
					}
				});
            }
        } catch(InterruptedException e) {
        } finally {
            serialPort.closePort();
        }
    }
    
    public boolean isPortConnected() {
    	return PortDetector.detectArduinoPort() != null && serialPort.isOpen();
    }
    
}
