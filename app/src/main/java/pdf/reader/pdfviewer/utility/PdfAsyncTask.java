package pdf.reader.pdfviewer.utility;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import pdf.reader.pdfviewer.listener.PdfDataGetListener;
import pdf.reader.pdfviewer.model.PdfFileModel;
import com.itextpdf.text.xml.xmp.PdfSchema;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfAsyncTask {
    Context context;
    CompositeDisposable disposable = new CompositeDisposable();
    List<PdfFileModel> favList;
    String foldername;
    boolean isfolderwise;
    PdfDataGetListener pdfDataGetListener;

    public PdfAsyncTask(Context context2, PdfDataGetListener pdfDataGetListener2, List<PdfFileModel> list) {
        this.context = context2;
        this.pdfDataGetListener = pdfDataGetListener2;
        this.isfolderwise = false;
        this.favList = list;
        setObservable();
    }

    public PdfAsyncTask(Context context2, PdfDataGetListener pdfDataGetListener2, String str, List<PdfFileModel> list) {
        this.context = context2;
        this.pdfDataGetListener = pdfDataGetListener2;
        this.isfolderwise = true;
        this.foldername = str;
        this.favList = list;
        setObservable();
    }

    public void setObservable() {
        this.disposable.add(Observable.fromCallable(new PdfAsyncTask$$ExternalSyntheticLambda0(this, new ArrayList())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new PdfAsyncTask$$ExternalSyntheticLambda1(this)));
    }

    
    public  List m232x4520a241(List list) throws Exception {
        String[] strArr;
        String str;
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(PdfSchema.DEFAULT_XPATH_ID);
        Uri contentUri = MediaStore.Files.getContentUri("external");
        String[] strArr2 = {"_data", "_size"};
        if (this.isfolderwise) {
            str = "mime_type=?" + " and _data like?";
            strArr = new String[]{mimeTypeFromExtension, "%Plite/" + this.foldername + "%"};
        } else {
            strArr = new String[]{mimeTypeFromExtension};
            str = "mime_type=?";
        }
        Cursor query = this.context.getContentResolver().query(contentUri, strArr2, str, strArr, "date_modified DESC");
        if (query == null || !query.moveToFirst()) {
            return list;
        }
        do {
            File file = new File(query.getString(0));
            if (file.exists() && query.getLong(1) > 0) {
                PdfFileModel pdfFileModel = new PdfFileModel(query.getString(0), file.getName(), file.lastModified(), query.getLong(1));
                int indexOf = this.favList.indexOf(pdfFileModel);
                if (indexOf != -1) {
                    list.add(this.favList.get(indexOf));
                } else {
                    list.add(pdfFileModel);
                }
            }
        } while (query.moveToNext());
        return list;
    }

    
    public  void m233x55d66f02(List list) throws Exception {
        this.pdfDataGetListener.onDatagetListener(list);
    }
}
