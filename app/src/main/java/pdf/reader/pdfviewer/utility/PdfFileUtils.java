package pdf.reader.pdfviewer.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import pdf.reader.pdfviewer.R;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.cli.HelpFormatter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.tom_roush.pdfbox.pdmodel.common.PDPageLabelRange;
import com.tom_roush.pdfbox.pdmodel.documentinterchange.taggedpdf.PDLayoutAttributeObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class PdfFileUtils {
    public static String getFileSize(long j) {
        if (j <= 0) {
            return PDLayoutAttributeObject.GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES;
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, (double) log10)) + HelpFormatter.DEFAULT_LONG_OPT_SEPARATOR + new String[]{"B", "kB", "MB", "GB", "TB"}[log10];
    }

    public static String getExternalStoragePath() {
        String str = "/";
        String[] split = Environment.getExternalStorageDirectory().getAbsolutePath().split(str);
        int length = split.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str2 = split[i];
            if (str2.trim().length() > 0) {
                str = str.concat(str2);
                break;
            }
            i++;
        }
        new File(str);
        return null;
    }

    public static String getDirecotyofThumnail(Context context) {
        File file = new File(context.getCacheDir(), context.getResources().getString(R.string.app_name_sub));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String thumbOfFirstPage(String str, Context context) {
        ParcelFileDescriptor parcelFileDescriptor;
        try {
            PdfiumCore pdfiumCore = new PdfiumCore(context);
            String str2 = System.currentTimeMillis() + ".jpg";
            File file = new File(getDirecotyofThumnail(context), str2);
            PdfDocument pdfDocument = null;
            try {
                if (Build.VERSION.SDK_INT > 29) {
                    parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(str), PDPageLabelRange.STYLE_ROMAN_LOWER);
                } else {
                    parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.fromFile(new File(str)), PDPageLabelRange.STYLE_ROMAN_LOWER);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                parcelFileDescriptor = null;
            }
            try {
                pdfDocument = pdfiumCore.newDocument(parcelFileDescriptor);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            pdfiumCore.openPage(pdfDocument, 0);
            int pageWidthPoint = pdfiumCore.getPageWidthPoint(pdfDocument, 0) / 2;
            int pageHeightPoint = pdfiumCore.getPageHeightPoint(pdfDocument, 0) / 2;
            Bitmap createBitmap = Bitmap.createBitmap(pageWidthPoint, pageHeightPoint, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, createBitmap, 0, 0, 0, pageWidthPoint, pageHeightPoint);
            try {
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
            parcelFileDescriptor.close();
            return str2;
        } catch (Exception e4) {
            e4.printStackTrace();
            return "";
        }
    }

    public static void deleteAllThumbanail(Context context) {
        File file = new File(getDirecotyofThumnail(context));
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
    }
}
