package rfidhandler.utils;

import com.fazecast.jSerialComm.SerialPort;

public abstract class PortDetector {
	
	public static final String DEVICE_NAME_WIN = "USB-SERIAL CH340";
	public static final String DEVICE_NAME_LINUX = "USB-to-Serial Port (ch341-uart)";
	
	public static final String detectArduinoPort() {
		SerialPort[] ports = SerialPort.getCommPorts();
        for(SerialPort port : ports) {
            if(port.getDescriptivePortName().startsWith(DEVICE_NAME_WIN) || port.getDescriptivePortName().startsWith(DEVICE_NAME_LINUX)) {
            	return port.getSystemPortName();
            }
        }
        return null;
	}
	
}
