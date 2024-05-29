package pdf.reader.pdfviewer.utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import com.itextpdf.text.xml.xmp.PdfSchema;

public class FileResolver {
    public static void insert(String str, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", str);
        contentValues.put("mime_type", MimeTypeMap.getSingleton().getMimeTypeFromExtension(PdfSchema.DEFAULT_XPATH_ID));
        context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
    }



    public static void delete(String str, Context context) {
        Uri contentUri = MediaStore.Files.getContentUri("external");
        context.getContentResolver().delete(contentUri, "_data=?", new String[]{str});
    }
}
