package rfidhandler.utils;

import com.fazecast.jSerialComm.SerialPort;

public abstract class PortDetector {
	
	public static final String DEVICE_NAME = "USB-SERIAL CH340";
	
	public static final String detectArduinoPort() {
		SerialPort[] ports = SerialPort.getCommPorts();
        for(SerialPort port : ports) {
            if(port.getDescriptivePortName().startsWith(DEVICE_NAME)) {
            	return port.getSystemPortName();
            }
        }
        return null;
	}
	
}
