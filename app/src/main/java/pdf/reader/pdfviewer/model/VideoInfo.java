package pdf.reader.pdfviewer.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class VideoInfo implements Parcelable {
    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {

        @Override 
        public VideoInfo createFromParcel(Parcel parcel) {
            return new VideoInfo(parcel);
        }

        @Override 
        public VideoInfo[] newArray(int i) {
            return new VideoInfo[i];
        }
    };
    long date;
    long duration;
    boolean isSelected = false;
    String path;
    private int selectionNumber;
    Uri uri;

    public int describeContents() {
        return 0;
    }

    public VideoInfo(Uri uri2) {
        this.uri = uri2;
    }

    public VideoInfo(Uri uri2, long j, long j2) {
        this.uri = uri2;
        this.duration = j;
        this.date = j2;
    }

    protected VideoInfo(Parcel parcel) {
        this.path = parcel.readString();
        this.duration = parcel.readLong();
        this.date = parcel.readLong();
        this.uri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeLong(this.duration);
        parcel.writeLong(this.date);
        parcel.writeParcelable(this.uri, i);
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getSelectionNumber() {
        return this.selectionNumber;
    }

    public void setSelectionNumber(int i) {
        this.selectionNumber = i;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public VideoInfo(int i) {
        this.selectionNumber = i;
    }

    public static Parcelable.Creator<VideoInfo> getCREATOR() {
        return CREATOR;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.uri.equals(((VideoInfo) obj).uri);
    }

    public int hashCode() {
        return Objects.hash(this.uri);
    }
}
