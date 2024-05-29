package pdf.reader.pdfviewer.utility;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.print.PrintManager;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.adapter.PrintAdapter;

import com.shockwave.pdfium.PdfPasswordException;
import com.shockwave.pdfium.PdfiumCore;
import com.tom_roush.fontbox.ttf.OpenTypeScript;
import com.tom_roush.pdfbox.pdmodel.common.PDPageLabelRange;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConstants {
    public static int COMPRESSION_HIGH = 4;
    public static int COMPRESS_LOW = 2;
    public static int COMPRESS_MEDIUM = 3;
    public static int COMPRESS_NO_COMPRESSION = 1;
    public static final String DATE_ASC = "DATE_ASC";
    public static final String DATE_DESC = "DATE_DESC";
    public static final String NAME_ASC = "NAME_ASC";
    public static final String NAME_DESC = "NAME_DESC";
    public static final String PDF_FILE_MODEL = "PdfFileModel";
    public static int PDF_VIEWER = 1;
    public static final String PRIVACY_POLICY_URL = "https://sites.google.com/view/essentialapps-privacy";
    public static final String SIZE_ASC = "SIZE_ASC";
    public static final String SIZE_DESC = "SIZE_DESC";


    public static int getDPToPixel(Context context, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    public static void refreshFiles(Context context, File file) {
        if (file != null) {
            try {
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file));
                context.sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String formatMetadataDate(String str) {
        try {
            return new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(str.split("\\+")[0].split(":")[1]));
        } catch (Exception e) {
            e.printStackTrace();
            return OpenTypeScript.UNKNOWN;
        }
    }


    public static void printPriview(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                new PdfiumCore(context).newDocument(context.getContentResolver().openFileDescriptor(uri, PDPageLabelRange.STYLE_ROMAN_LOWER));
                if (PrintHelper.systemSupportsPrint()) {
                    PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                    String str = context.getString(R.string.app_name) + " Document";
                    if (printManager != null) {
                        printManager.print(str, new PrintAdapter(context, uri), null);
                        return;
                    }
                    return;
                }
                Toast.makeText(context, "Device not support printing", 0).show();
            } catch (PdfPasswordException e) {
                Toast.makeText(context, "PDF Can't print, its password protected", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e2) {
                Toast.makeText(context, "Error in pdf preview", 0).show();
                e2.printStackTrace();
            } catch (Exception e3) {
                Toast.makeText(context, "Error in pdf preview", 0).show();
                e3.printStackTrace();
            }
        }
    }

    public static boolean checkStoragePermissionApi30(Context context) {
        int unsafeCheckOpNoThrow = ((AppOpsManager) context.getSystemService("appops")).unsafeCheckOpNoThrow(AppOpsManager.permissionToOp("android.permission.MANAGE_EXTERNAL_STORAGE"), context.getApplicationInfo().uid, context.getApplicationInfo().packageName);
        if (unsafeCheckOpNoThrow == 3) {
            if (context.checkCallingOrSelfPermission("android.permission.MANAGE_EXTERNAL_STORAGE") == 0) {
                return true;
            }
        } else if (unsafeCheckOpNoThrow == 0) {
            return true;
        }
        return false;
    }

    public static boolean checkStoragePermissionApi19(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0;
    }


    public static void setStatusBarWhite(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.setStatusBarColor(-1);
        activity.getWindow().getDecorView().setSystemUiVisibility(8192);
    }

    public static void setStatusBarBlack(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.dark_toolbar));
    }

    public static void setStatusBarDefault(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
    }
}
