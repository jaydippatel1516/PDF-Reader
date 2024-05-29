package pdf.reader.pdfviewer.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DecimalFormat;
import java.util.Comparator;

public class AllDirc implements Parcelable {
    public static final Parcelable.Creator<AllDirc> CREATOR = new Parcelable.Creator<AllDirc>() {

        @Override
        public AllDirc createFromParcel(Parcel parcel) {
            return new AllDirc(parcel);
        }

        @Override 
        public AllDirc[] newArray(int i) {
            return new AllDirc[i];
        }
    };
    private String absolutePath;
    private long datetime;
    private String folder_date;
    private String folder_item;
    private String folder_name;
    private String folder_path;
    private String folder_size;
    private long sizeoffile;

    public static class Comparators {
        public static Comparator<AllDirc> DATEAes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                if (allDirc.getDatetime() > allDirc2.getDatetime()) {
                    return 1;
                }
                return allDirc.getDatetime() < allDirc2.getDatetime() ? -1 : 0;
            }
        };
        public static Comparator<AllDirc> DATEDes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                if (allDirc2.getDatetime() > allDirc.getDatetime()) {
                    return 1;
                }
                return allDirc2.getDatetime() < allDirc.getDatetime() ? -1 : 0;
            }
        };
        public static Comparator<PdfFileModel> DATE_MODIFIED_Aes = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel.getLastmodified() > pdfFileModel2.getLastmodified()) {
                    return 1;
                }
                return pdfFileModel.getLastmodified() < pdfFileModel2.getLastmodified() ? -1 : 0;
            }
        };
        public static Comparator<PdfFileModel> DATE_MODIFIED_Des = new Comparator<PdfFileModel>() {

            public int compare(PdfFileModel pdfFileModel, PdfFileModel pdfFileModel2) {
                if (pdfFileModel.getLastmodified() > pdfFileModel2.getLastmodified()) {
                    return 1;
                }
                return pdfFileModel.getLastmodified() < pdfFileModel2.getLastmodified() ? -1 : 0;
            }
        };
        public static Comparator<AllDirc> NAMEAes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                return allDirc.getFolder_name().toLowerCase().compareTo(allDirc2.getFolder_name().toLowerCase());
            }
        };
        public static Comparator<AllDirc> NAMEDes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                return allDirc2.getFolder_name().toLowerCase().compareTo(allDirc.getFolder_name().toLowerCase());
            }
        };
        public static Comparator<AllDirc> SIZEAes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                if (allDirc.getSizeoffile() > allDirc2.getSizeoffile()) {
                    return 1;
                }
                return allDirc.getSizeoffile() < allDirc2.getSizeoffile() ? -1 : 0;
            }
        };
        public static Comparator<AllDirc> SIZEDes = new Comparator<AllDirc>() {

            public int compare(AllDirc allDirc, AllDirc allDirc2) {
                if (allDirc2.getSizeoffile() > allDirc.getSizeoffile()) {
                    return 1;
                }
                return allDirc2.getSizeoffile() < allDirc.getSizeoffile() ? -1 : 0;
            }
        };
    }

    public int describeContents() {
        return 0;
    }

    protected AllDirc(Parcel parcel) {
        this.folder_name = parcel.readString();
        this.folder_item = parcel.readString();
        this.folder_date = parcel.readString();
        this.datetime = parcel.readLong();
        this.folder_size = parcel.readString();
        this.folder_path = parcel.readString();
        this.absolutePath = parcel.readString();
        this.sizeoffile = parcel.readLong();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.folder_name);
        parcel.writeString(this.folder_item);
        parcel.writeString(this.folder_date);
        parcel.writeLong(this.datetime);
        parcel.writeString(this.folder_size);
        parcel.writeString(this.folder_path);
        parcel.writeString(this.absolutePath);
        parcel.writeLong(this.sizeoffile);
    }

    public AllDirc(String str) {
        this.folder_path = str;
    }

    public AllDirc() {
    }

    public String getFolder_name() {
        return this.folder_name;
    }

    public String getFolder_item() {
        return this.folder_item;
    }

    public void setFolder_item(String str) {
        this.folder_item = str;
    }

    public void setFolder_name(String str) {
        this.folder_name = str;
    }

    public void setFolder_path(String str) {
        this.folder_path = str;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public String getFolder_path() {
        return this.folder_path;
    }

    public String getFolder_date() {
        return this.folder_date;
    }

    public void setFolder_date(String str) {
        this.folder_date = str;
    }

    public String getFolder_size() {
        return this.folder_size;
    }

    public void setFolder_size(String str) {
        this.folder_size = str;
    }

    public String Foldersize() {
        String str = this.folder_size;
        if (str == null) {
            return "";
        }
        double longValue = ((double) Long.valueOf(str).longValue()) / 1024.0d;
        double d = longValue / 1024.0d;
        if (d > 1.0d) {
            return new DecimalFormat("0.00").format(d).concat(" MB");
        }
        return String.format("%.2f", Double.valueOf(longValue)).concat(" KB");
    }

    public void setAbsolutePath(String str) {
        this.absolutePath = str;
    }

    public long getSizeoffile() {
        return this.sizeoffile;
    }

    public void setSizeoffile(long j) {
        this.sizeoffile = j;
    }

    public long getDatetime() {
        return this.datetime;
    }

    public void setDatetime(long j) {
        this.datetime = j;
    }

    public AllDirc(String str, String str2, String str3, long j, String str4, String str5, String str6, long j2) {
        this.folder_name = str;
        this.folder_item = str2;
        this.folder_date = str3;
        this.datetime = j;
        this.folder_size = str4;
        this.folder_path = str5;
        this.absolutePath = str6;
        this.sizeoffile = j2;
    }

    public boolean equals(Object obj) {
        if (obj != null && AllDirc.class.isAssignableFrom(obj.getClass())) {
            String str = this.folder_path;
            if (str != null && str.equals(((AllDirc) obj).folder_path)) {
                return true;
            }
            String str2 = this.folder_name;
            if (str2 == null || !str2.equals(((AllDirc) obj).folder_name)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
