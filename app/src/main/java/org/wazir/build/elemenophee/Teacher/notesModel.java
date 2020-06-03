package org.wazir.build.elemenophee.Teacher;

public class notesModel {
    private String fileTitle;
    private String fileUrl;

    public notesModel() {
    }

    public notesModel(String fileTitle, String fileUrl) {
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
