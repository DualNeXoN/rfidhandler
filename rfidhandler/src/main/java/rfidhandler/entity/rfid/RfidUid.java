package rfidhandler.entity.rfid;

import rfidhandler.utils.Decoder;

public class RfidUid {
	
	private byte[] uid;
	
	public RfidUid(byte[] uid) {
		this.uid = uid;
	}
	
	public RfidUid(String uid) {
		String temp = "";
		for(String s : uid.split("-")) temp += s;
		this.uid = Decoder.decodeHexString(temp);
	}
	
	public String getUidString() {
		String uidString = "";
		for(int index = 0; index < uid.length; index++) {
			String hexValue = Integer.toHexString(uid[index] & 0xFF).toUpperCase();
			if(hexValue.length() == 1) hexValue = "0" + hexValue;
			uidString += hexValue + (index < 3 ? "-" : "");
		}
		return uidString;
	}
	
	public byte[] getUid() {
		return uid;
	}
	
}
