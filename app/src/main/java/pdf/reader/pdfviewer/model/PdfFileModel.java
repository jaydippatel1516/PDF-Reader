package pdf.reader.pdfviewer.model;

import android.os.Parcel;
import android.os.Parcelable;
import pdf.reader.pdfviewer.utility.PdfFileUtils;
import pdf.reader.pdfviewer.utility.Test;
import java.util.Comparator;

public class PdfFileModel implements Parcelable {
    public static final Parcelable.Creator<PdfFileModel> CREATOR = new Parcelable.Creator<PdfFileModel>() {

        @Override 
        public PdfFileModel createFromParcel(Parcel parcel) {
            return new PdfFileModel(parcel);
        }

        @Override 
        public PdfFileModel[] newArray(int i) {
            return new PdfFileModel[i];
        }
    };
    String filename;
    String filepath;
    long filesize;
    String imagepath = "";
    boolean isFav = false;
    boolean isSelected = false;
    long lastmodified;
    String newpath;

    public static class Comparators {
        public static Comparator<PdfFileModel> DATEAes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel.getLastmodified() > pdfFileModel2.getLastmodified()) {
                    return 1;
                }
                return pdfFileModel.getLastmodified() < pdfFileModel2.getLastmodified() ? -1 : 0;
            }
        };
        public static Comparator<PdfFileModel> DATEDes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel2.getLastmodified() > pdfFileModel.getLastmodified()) {
                    return 1;
                }
                return pdfFileModel2.getLastmodified() < pdfFileModel.getLastmodified() ? -1 : 0;
            }
        };
        public static Comparator<PdfFileModel> NAMEAes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                return pdfFileModel.getFilename().toLowerCase().compareTo(pdfFileModel2.getFilename().toLowerCase());
            }
        };
        public static Comparator<PdfFileModel> NAMEDes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                return pdfFileModel2.getFilename().toLowerCase().compareTo(pdfFileModel.getFilename().toLowerCase());
            }
        };
        public static Comparator<PdfFileModel> SIZEAes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel.getFilesize() > pdfFileModel2.getFilesize()) {
                    return 1;
                }
                return pdfFileModel.getFilesize() < pdfFileModel2.getFilesize() ? -1 : 0;
            }
        };
        public static Comparator<PdfFileModel> SIZEDes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel2.getFilesize() > pdfFileModel.getFilesize()) {
                    return 1;
                }
                return pdfFileModel2.getFilesize() < pdfFileModel.getFilesize() ? -1 : 0;
            }
        };
    }

    public int describeContents() {
        return 0;
    }

    public PdfFileModel() {
    }

    public PdfFileModel(String str, String str2, long j, long j2) {
        this.filepath = str;
        this.filename = str2;
        this.lastmodified = j;
        this.filesize = j2;
    }

    public PdfFileModel(PdfFileModel pdfFileModel) {
        this.filepath = pdfFileModel.getFilepath();
        this.filename = pdfFileModel.getFilename();
        this.lastmodified = pdfFileModel.getLastmodified();
        this.filesize = pdfFileModel.getFilesize();
        this.imagepath = pdfFileModel.getImagepath();
        this.isFav = pdfFileModel.isFav();
    }

    protected PdfFileModel(Parcel parcel) {
        boolean z = false;
        this.filename = parcel.readString();
        this.lastmodified = parcel.readLong();
        this.filesize = parcel.readLong();
        this.filepath = parcel.readString();
        this.newpath = parcel.readString();
        this.isSelected = parcel.readByte() != 0;
        this.isFav = parcel.readByte() != 0 ? true : z;
        this.imagepath = parcel.readString();
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public long getLastmodified() {
        return this.lastmodified;
    }

    public void setLastmodified(long j) {
        this.lastmodified = j;
    }

    public boolean isFav() {
        return this.isFav;
    }

    public void setFav(boolean z) {
        this.isFav = z;
    }

    public long getFilesize() {
        return this.filesize;
    }

    public String getFileSizeString() {
        return PdfFileUtils.getFileSize(this.filesize);
    }

    public void setFilesize(long j) {
        this.filesize = j;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String str) {
        this.filepath = str;
    }

    public String getFormateData() {
        return Test.getDate(this.lastmodified);
    }

    public String getFormateData1() {
        return Test.getDate(this.lastmodified);
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getImagepath() {
        return this.imagepath;
    }

    public void setImagepath(String str) {
        this.imagepath = str;
    }

    public String getNewpath() {
        return this.newpath;
    }

    public void setNewpath(String str) {
        this.newpath = str;
    }

    public boolean equals(Object obj) {
        if (obj == null || !PdfFileModel.class.isAssignableFrom(obj.getClass()) || !this.filepath.equals(((PdfFileModel) obj).filepath)) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.filename);
        parcel.writeLong(this.lastmodified);
        parcel.writeLong(this.filesize);
        parcel.writeString(this.filepath);
        parcel.writeString(this.newpath);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
        parcel.writeByte(this.isFav ? (byte) 1 : 0);
        parcel.writeString(this.imagepath);
    }
}
