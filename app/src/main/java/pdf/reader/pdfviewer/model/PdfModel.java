package pdf.reader.pdfviewer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PdfModel implements Parcelable {
    public static final Parcelable.Creator<PdfModel> CREATOR = new Parcelable.Creator<PdfModel>() {

        @Override 
        public PdfModel createFromParcel(Parcel parcel) {
            return new PdfModel(parcel);
        }

        @Override 
        public PdfModel[] newArray(int i) {
            return new PdfModel[i];
        }
    };
    private long FileDateTime;
    private long FileLength;
    private String Filename;
    private String Filepath;
    private boolean isChecked;

    public int describeContents() {
        return 0;
    }

    public PdfModel() {
        this.Filename = "";
        this.FileDateTime = 0;
        this.FileLength = 0;
        this.Filepath = "";
        this.isChecked = false;
    }

    public PdfModel(String str, long j, long j2, String str2) {
        this.isChecked = false;
        this.Filename = str;
        this.FileDateTime = j;
        this.FileLength = j2;
        this.Filepath = str2;
    }

    protected PdfModel(Parcel parcel) {
        this.Filename = "";
        this.FileDateTime = 0;
        this.FileLength = 0;
        this.Filepath = "";
        boolean z = false;
        this.isChecked = false;
        this.Filename = parcel.readString();
        this.FileDateTime = parcel.readLong();
        this.FileLength = parcel.readLong();
        this.Filepath = parcel.readString();
        this.isChecked = parcel.readByte() != 0 ? true : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.Filename);
        parcel.writeLong(this.FileDateTime);
        parcel.writeLong(this.FileLength);
        parcel.writeString(this.Filepath);
        parcel.writeByte(this.isChecked ? (byte) 1 : 0);
    }

    public String getFilename() {
        return this.Filename;
    }

    public void setFilename(String str) {
        this.Filename = str;
    }

    public long getFileDateTime() {
        return this.FileDateTime;
    }

    public void setFileDateTime(long j) {
        this.FileDateTime = j;
    }

    public long getFileLength() {
        return this.FileLength;
    }

    public void setFileLength(long j) {
        this.FileLength = j;
    }

    public String getFilepath() {
        return this.Filepath;
    }

    public void setFilepath(String str) {
        this.Filepath = str;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }
}
