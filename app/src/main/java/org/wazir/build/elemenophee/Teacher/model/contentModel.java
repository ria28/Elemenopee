package org.wazir.build.elemenophee.Teacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class contentModel implements Parcelable {
    private String fileTitle;
    private String fileUrl;
    private Timestamp timeStamp;
    private String privacy;
    private String teacherID;
    private String mime;
    private String clas;
    private String subject;
    private String chapter;

    public contentModel() {
    }

    public contentModel(String fileTitle, String fileUrl, Timestamp timeStamp, String privacy, String teacherID, String mime, String clas, String subject, String chapter) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
        this.timeStamp = timeStamp;
        this.privacy = privacy;
        this.teacherID = teacherID;
        this.mime = mime;
        this.clas = clas;
        this.subject = subject;
        this.chapter = chapter;
    }

    protected contentModel(Parcel in) {
        fileTitle = in.readString();
        fileUrl = in.readString();
        timeStamp = in.readParcelable(Timestamp.class.getClassLoader());
        privacy = in.readString();
        teacherID = in.readString();
        mime = in.readString();
        clas = in.readString();
        subject = in.readString();
        chapter = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileTitle);
        dest.writeString(fileUrl);
        dest.writeParcelable(timeStamp, flags);
        dest.writeString(privacy);
        dest.writeString(teacherID);
        dest.writeString(mime);
        dest.writeString(clas);
        dest.writeString(subject);
        dest.writeString(chapter);

    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }
    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
