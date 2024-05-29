package pdf.reader.pdfviewer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PdftoImageModel implements Parcelable {
    public static final Parcelable.Creator<PdftoImageModel> CREATOR = new Parcelable.Creator<PdftoImageModel>() {

        @Override 
        public PdftoImageModel createFromParcel(Parcel parcel) {
            return new PdftoImageModel(parcel);
        }

        @Override 
        public PdftoImageModel[] newArray(int i) {
            return new PdftoImageModel[i];
        }
    };
    String filename;
    String inputPath;
    boolean isjpg;
    String outputPath;

    public int describeContents() {
        return 0;
    }

    public PdftoImageModel(String str, String str2, boolean z, String str3) {
        this.inputPath = str;
        this.outputPath = str2;
        this.isjpg = z;
        this.filename = str3;
    }

    protected PdftoImageModel(Parcel parcel) {
        this.inputPath = parcel.readString();
        this.outputPath = parcel.readString();
        this.filename = parcel.readString();
        this.isjpg = parcel.readByte() != 0;
    }

    public String getInputPath() {
        return this.inputPath;
    }

    public void setInputPath(String str) {
        this.inputPath = str;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public void setOutputPath(String str) {
        this.outputPath = str;
    }

    public boolean isIsjpg() {
        return this.isjpg;
    }

    public void setIsjpg(boolean z) {
        this.isjpg = z;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.inputPath);
        parcel.writeString(this.outputPath);
        parcel.writeString(this.filename);
        parcel.writeByte(this.isjpg ? (byte) 1 : 0);
    }
}
