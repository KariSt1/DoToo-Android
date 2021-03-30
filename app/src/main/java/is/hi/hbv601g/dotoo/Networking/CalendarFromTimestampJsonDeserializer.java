package is.hi.hbv601g.dotoo.Networking;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The Class CalendarFromTimestampJsonDeserializer.
 */
public class CalendarFromTimestampJsonDeserializer implements JsonDeserializer<Calendar> {

    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    public Calendar deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        String timestamp = json.getAsString();
        Calendar calendar = Calendar.getInstance();
        timestamp = timestamp.replace(timestamp.charAt(10), ' '); //Get rid of the T from date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            calendar.setTime(sdf.parse(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}
