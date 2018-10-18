package pl.sokolx.coach.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {


    public static long stringDateToLong(String data) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date d = null;
        try {
            d = f.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d.getTime();
    }

    public static String parseDateToString(Long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return calendar.get(Calendar.YEAR) +
                "-" + ((calendar.get(Calendar.MONTH) < 9) ? "0"+ (calendar.get(Calendar.MONTH)+1) : (calendar.get(Calendar.MONTH)+1)) +
                "-" + ((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0"+ calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH)) +
                " " + ((calendar.get(Calendar.HOUR_OF_DAY) < 10)? "0"+ calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + ((calendar.get(Calendar.MINUTE) < 10) ? "0"+calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
    }

    public static String parseDateToStringWithSeconds(Long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return calendar.get(Calendar.YEAR) +
                "-" + ((calendar.get(Calendar.MONTH) < 9) ? "0"+ (calendar.get(Calendar.MONTH)+1) : (calendar.get(Calendar.MONTH)+1)) +
                "-" + ((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0"+ calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH)) +
                " " + ((calendar.get(Calendar.HOUR_OF_DAY) < 10)? "0"+ calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + ((calendar.get(Calendar.MINUTE) < 10) ? "0"+ calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)) +
                ":" + ((calendar.get(Calendar.SECOND) < 10) ? "0"+ calendar.get(Calendar.SECOND) : calendar.get(Calendar.SECOND));
    }

    public static String parseMilisecondToHMS(Long miliseconds) {

        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(miliseconds),
                TimeUnit.MILLISECONDS.toMinutes(miliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(miliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(miliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliseconds)));

    }

}
