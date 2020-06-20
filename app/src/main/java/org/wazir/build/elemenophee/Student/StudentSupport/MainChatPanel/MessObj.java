package org.wazir.build.elemenophee.Student.StudentSupport.MainChatPanel;

public class MessObj {
    private String message;
    private String senderId, receiverId;
    private String imageUrl = "";
    private String sendName;

    public MessObj() {
    }

    public MessObj(String message, String senderId, String receiverId, String sendName) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sendName = sendName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSendName() {
        return sendName;
    }
}
