package anaice.com.br.bringthepopcorn.util;

public class DateUtils {

    /**
     * Get a date string and transform it to the format "dd/MM/yyyy"
     * @param defaultDateFormat date in the format "yyyy-mm-dd"
     * @return date in the format "dd/MM/yyyyy"
     */
    public static String getBrazilianDateFormat(String defaultDateFormat) {
        String year = defaultDateFormat.substring(0, 4);
        String month = defaultDateFormat.substring(5, 7);
        String day = defaultDateFormat.substring(8, 10);

        return day + "/" + month + "/" + year;
    }
}
