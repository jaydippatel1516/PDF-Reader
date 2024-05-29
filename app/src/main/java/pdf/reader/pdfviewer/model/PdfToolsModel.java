package pdf.reader.pdfviewer.model;

import android.graphics.drawable.Drawable;
import androidx.databinding.BaseObservable;

public class PdfToolsModel extends BaseObservable {
    int color;
    Drawable imagename;
    String title;

    public PdfToolsModel(String str, Drawable drawable, int i) {
        this.imagename = drawable;
        this.title = str;
        this.color = i;
    }

    public Drawable getImagename() {
        return this.imagename;
    }

    public void setImagename(Drawable drawable) {
        this.imagename = drawable;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }
}
