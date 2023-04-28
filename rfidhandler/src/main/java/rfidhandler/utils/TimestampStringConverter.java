package rfidhandler.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.util.StringConverter;

public class TimestampStringConverter extends StringConverter<Timestamp> {
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public String toString(Timestamp timestamp) {
        if(timestamp == null) {
            return "";
        }
        Date date = new Date(timestamp.getTime());
        return formatter.format(date);
    }

    @Override
    public Timestamp fromString(String string) {
        throw new UnsupportedOperationException("Conversion from String to Timestamp not supported.");
    }
}
