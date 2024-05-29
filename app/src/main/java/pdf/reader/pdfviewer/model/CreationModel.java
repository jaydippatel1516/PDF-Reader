package pdf.reader.pdfviewer.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.databinding.BaseObservable;

public class CreationModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<CreationModel> CREATOR = new Parcelable.Creator<CreationModel>() {

        @Override 
        public CreationModel createFromParcel(Parcel parcel) {
            return new CreationModel(parcel);
        }

        @Override 
        public CreationModel[] newArray(int i) {
            return new CreationModel[i];
        }
    };
    int length;
    String path;
    String tiltle;

    public int describeContents() {
        return 0;
    }

    public CreationModel() {
    }

    public CreationModel(String str) {
        this.path = str;
    }

    protected CreationModel(Parcel parcel) {
        this.tiltle = parcel.readString();
        this.path = parcel.readString();
        this.length = parcel.readInt();
    }

    public String getTiltle() {
        return this.tiltle;
    }

    public void setTiltle(String str) {
        this.tiltle = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int i) {
        this.length = i;
    }

    public String getTotalFile() {
        return String.valueOf(this.length);
    }

    public boolean equals(Object obj) {
        if (obj == null || !CreationModel.class.isAssignableFrom(obj.getClass()) || !this.path.equals(((CreationModel) obj).path)) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tiltle);
        parcel.writeString(this.path);
        parcel.writeInt(this.length);
    }
}
