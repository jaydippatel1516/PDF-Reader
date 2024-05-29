package pdf.reader.pdfviewer.model;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Objects;

public class VideoDir {
    ArrayList<VideoInfo> arrayList;
    String bucketId;
    String bucketPath;
    long count;
    String dirName;
    String dirPath;
    Uri uri;

    public long getCount() {
        return this.count;
    }

    public void setCount() {
        this.count++;
    }

    public String getCountS() {
        return this.count + "";
    }

    public VideoDir(String str, String str2) {
        this.arrayList = new ArrayList<>();
        this.count = 1;
        this.dirName = str;
        this.bucketId = str2;
    }

    public VideoDir(String str, int i) {
        this.arrayList = new ArrayList<>();
        this.count = 1;
        this.dirName = str;
    }

    public void addInfoVideo(VideoInfo videoInfo) {
        this.arrayList.add(videoInfo);
    }

    public VideoDir(ArrayList<VideoInfo> arrayList2, String str, String str2) {
        new ArrayList();
        this.count = 1;
        this.arrayList = arrayList2;
        this.dirPath = str;
        this.dirName = str2;
    }

    public String getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(String str) {
        this.bucketId = str;
    }

    public String getBucketPath() {
        return this.bucketPath;
    }

    public void setBucketPath(String str) {
        this.bucketPath = str;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public void setCount(long j) {
        this.count = j;
    }

    public ArrayList<VideoInfo> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<VideoInfo> arrayList2) {
        this.arrayList = arrayList2;
    }

    public String getDirPath() {
        return this.dirPath;
    }

    public void setDirPath(String str) {
        this.dirPath = str;
    }

    public String getDirName() {
        return this.dirName;
    }

    public void setDirName(String str) {
        this.dirName = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.bucketId, ((VideoDir) obj).bucketId);
    }

    public int hashCode() {
        return Objects.hash(this.bucketId);
    }
}
