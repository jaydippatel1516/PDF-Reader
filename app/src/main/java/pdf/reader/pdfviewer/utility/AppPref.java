package pdf.reader.pdfviewer.utility;

import android.content.Context;
import android.content.SharedPreferences;
import pdf.reader.pdfviewer.model.PdfFileModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppPref {
    static final String FAV_LIST = "FAV_LIST";
    static final String MyPref = "userPref";
    static final String RECENT_LIST = "RECENT_LIST";
    static final String SORT_TYPE = "SORT_TYPE";
    static final String SORT_TYPE_FAV = "SORT_TYPE_FAV";
    static final String SORT_TYPE_RECENT = "SORT_TYPE_RECENT";


    public static void saveFavArrayList(List<PdfFileModel> list, Context context) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(FAV_LIST, new Gson().toJson(list));
        edit.apply();
    }

    public static List<PdfFileModel> getFavArrayList(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(MyPref, 0);
        Gson gson = new Gson();
        String string = sharedPreferences.getString(FAV_LIST, null);
        Type type = new TypeToken<List<PdfFileModel>>() {

        }.getType();
        if (string == null) {
            return new ArrayList();
        }
        return (List) gson.fromJson(string, type);
    }

    public static void saveRecentFileList(List<PdfFileModel> list, Context context) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(RECENT_LIST, new Gson().toJson(list));
        edit.apply();
    }

    public static List<PdfFileModel> getRecentFileList(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(MyPref, 0);
        Gson gson = new Gson();
        String string = sharedPreferences.getString(RECENT_LIST, null);
        Type type = new TypeToken<List<PdfFileModel>>() {

        }.getType();
        if (string == null) {
            return new ArrayList();
        }
        return (List) gson.fromJson(string, type);
    }

    public static void setSortType(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE, str);
        edit.apply();
    }

    public static String getSortType(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE, AppConstants.DATE_DESC);
    }

    public static void setSortTypeFAV(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_FAV, str);
        edit.apply();
    }

    public static String getSortTypeFAV(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_FAV, AppConstants.DATE_DESC);
    }

    public static void setSortTypeRECENT(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_RECENT, str);
        edit.apply();
    }

    public static String getSortTypeRECENT(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_RECENT, AppConstants.DATE_DESC);
    }
}
