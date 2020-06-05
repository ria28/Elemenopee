package org.wazir.build.elemenophee.Teacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class contentModel implements Parcelable {
    private String fileTitle;
    private String fileUrl;
    private Timestamp timeStamp;


    public contentModel() {
    }

    public contentModel(String fileTitle, String fileUrl, Timestamp timeStamp) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
        this.timeStamp = timeStamp;
    }

    protected contentModel(Parcel in) {
        fileTitle = in.readString();
        fileUrl = in.readString();
        timeStamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<contentModel> CREATOR = new Creator<contentModel>() {
        @Override
        public contentModel createFromParcel(Parcel in) {
            return new contentModel(in);
        }

        @Override
        public contentModel[] newArray(int size) {
            return new contentModel[size];
        }
    };

    public String getFileTitle() {
        return fileTitle;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileTitle);
        dest.writeString(fileUrl);
        dest.writeParcelable(timeStamp, flags);
    }
}
