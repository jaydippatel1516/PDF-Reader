package pdf.reader.pdfviewer.utility;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

public class FolderCreation {
    public static String FOLDER_ADD_WATERMARK = "Add Watermark";
    public static String FOLDER_DELETE_PAGE = "Delete Page";
    public static String FOLDER_EXTRACT_PAGE = "Extract Page";
    public static String FOLDER_IMAGE_TO_PDF = "Image to Pdf";
    public static String FOLDER_LOCK_PAGE = "Lock Pdf";
    public static String FOLDER_MERGE = "Merge Pdf";
    public static final String FOLDER_NAME = "Plite";
    public static String FOLDER_PDF_TO_IMAGE = "Pdf to Image";
    public static String FOLDER_ROTATE_PAGE = "Rotate Page";
    public static String FOLDER_SPLIT = "Split Pdf";
    public static String FOLDER_UNLOCK_PAGE = "Unlock Pdf";
    public static File makeFile;

    public static String PATH_IMAGE_TO_PDF() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_IMAGE_TO_PDF;
    }

    public static String PATH_PDF_TO_IMAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_PDF_TO_IMAGE;
    }

    public static String PATH_MERGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_MERGE;
    }

    public static String PATH_SPLIT() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_SPLIT;
    }

    public static String PATH_DELETE_PAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_DELETE_PAGE;
    }

    public static String PATH_EXTRACT_PAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_EXTRACT_PAGE;
    }

    public static String PATH_LOCK_PAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_LOCK_PAGE;
    }

    public static String PATH_UNLOCK_PAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_UNLOCK_PAGE;
    }

    public static String PATH_ROTATE_PAGE() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_ROTATE_PAGE;
    }

    public static String PATH_ADD_WATERMARK() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME + "/" + FOLDER_ADD_WATERMARK;
    }


    public static void CreateDirecory() {
        if (Build.VERSION.SDK_INT > 29) {
            makeFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME);
        } else {
            makeFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME);
        }
        if (!makeFile.exists()) {
            makeFile.mkdir();
        }
        File file = new File(PATH_IMAGE_TO_PDF());
        makeFile = file;
        if (!file.exists()) {
            makeFile.mkdir();
        }
        File file2 = new File(PATH_PDF_TO_IMAGE());
        makeFile = file2;
        if (!file2.exists()) {
            makeFile.mkdir();
        }
        File file3 = new File(PATH_MERGE());
        makeFile = file3;
        if (!file3.exists()) {
            makeFile.mkdir();
        }
        File file4 = new File(PATH_SPLIT());
        makeFile = file4;
        if (!file4.exists()) {
            makeFile.mkdir();
        }
        File file5 = new File(PATH_DELETE_PAGE());
        makeFile = file5;
        if (!file5.exists()) {
            makeFile.mkdir();
        }
        File file6 = new File(PATH_EXTRACT_PAGE());
        makeFile = file6;
        if (!file6.exists()) {
            makeFile.mkdir();
        }
        File file7 = new File(PATH_LOCK_PAGE());
        makeFile = file7;
        if (!file7.exists()) {
            makeFile.mkdir();
        }
        File file8 = new File(PATH_UNLOCK_PAGE());
        makeFile = file8;
        if (!file8.exists()) {
            makeFile.mkdir();
        }
        File file9 = new File(PATH_ROTATE_PAGE());
        makeFile = file9;
        if (!file9.exists()) {
            makeFile.mkdir();
        }
        File file10 = new File(PATH_ADD_WATERMARK());
        makeFile = file10;
        if (!file10.exists()) {
            makeFile.mkdir();
        }
    }


    public static boolean isFolderExists(Context context, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        sb.append(File.separator);
        sb.append(FOLDER_NAME);
        sb.append(File.separator);
        sb.append(str);
        sb.append(File.separator);
        Cursor query = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_display_name", "bucket_display_name"}, null, null, "datetaken DESC");
        if (query == null || !query.moveToFirst()) {
            return false;
        }
        do {
            if (!TextUtils.equals(str, FOLDER_PDF_TO_IMAGE)) {
                String string = query.getString(query.getColumnIndexOrThrow("_display_name"));
                if (TextUtils.equals(string, str2 + ".pdf")) {
                    return true;
                }
            } else if (TextUtils.equals(query.getString(query.getColumnIndex("bucket_display_name")), str2)) {
                return true;
            }
        } while (query.moveToNext());
        return false;
    }
}
