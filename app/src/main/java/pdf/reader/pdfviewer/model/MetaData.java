package pdf.reader.pdfviewer.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import pdf.reader.pdfviewer.utility.AppConstants;

public class MetaData extends BaseObservable {
    String authot;
    String created;
    String creator;
    String keyword;
    String modifed;
    String producer;
    String subject;
    String title;

    @Bindable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
        notifyChange();
    }

    @Bindable
    public String getAuthor() {
        return this.authot;
    }

    public void setAuthor(String str) {
        this.authot = str;
        notifyChange();
    }

    @Bindable
    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String str) {
        this.creator = str;
        notifyChange();
    }

    @Bindable
    public String getProducer() {
        return this.producer;
    }

    public void setProducer(String str) {
        this.producer = str;
        notifyChange();
    }

    @Bindable
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String str) {
        this.subject = str;
        notifyChange();
    }

    @Bindable
    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String str) {
        this.keyword = str;
        notifyChange();
    }

    @Bindable
    public String getCreated() {
        return this.created;
    }

    public void setCreated(String str) {
        this.created = str;
        notifyChange();
    }

    @Bindable
    public String getModifed() {
        return this.modifed;
    }

    public void setModifed(String str) {
        this.modifed = str;
        notifyChange();
    }

    @Bindable
    public String getFormateDateCreated() {
        return AppConstants.formatMetadataDate(this.created);
    }

    @Bindable
    public String getFormateDateModified() {
        return AppConstants.formatMetadataDate(this.modifed);
    }
}
