package pdf.reader.pdfviewer.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {
    public static String dateFormat = "d MMM yyyy";

    public static void main(String[] strArr) {
    }

    public static String getDate(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }
}
