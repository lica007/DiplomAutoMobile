package ru.iteco.fmhandroid.ui.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DataHelper {

    public static String getValidLogin() { return ("login2"); }

    public static String getValidPassword() { return ("password2"); }

    public static String getIncorrectLogin() { return ("user"); }

    public static String getIncorrectPassword() { return ("pass"); }

    public static String getCategoryAnnouncement() { return ("Объявление"); }
    public static String getCategoryHoliday() { return ("Праздник"); }

    public static String getDescription() { return ("Описание"); }

    public static String getStatusNotActive() { return ("Не активна"); }
    public static String getStatusActive() { return ("Активна"); }

    public static String getDateWithOffset(int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(new Date());
    }
}
