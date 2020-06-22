package org.wazir.build.elemenophee.ModelObj;

public class AnsDispObj {
    private String teachProPic, teachName, ansDate;
    private String ansBody;
    private int votes;

    public AnsDispObj() {
    }

    public AnsDispObj(String teachProPic, String teachName, String ansDate, String ansBody, int votes) {
        this.teachProPic = teachProPic;
        this.teachName = teachName;
        this.ansDate = ansDate;
        this.ansBody = ansBody;
        this.votes = votes;
    }

    public String getTeachProPic() {
        return teachProPic;
    }

    public void setTeachProPic(String teachProPic) {
        this.teachProPic = teachProPic;
    }

    public String getTeachName() {
        return teachName;
    }

    public void setTeachName(String teachName) {
        this.teachName = teachName;
    }

    public String getAnsDate() {
        return ansDate;
    }

    public void setAnsDate(String ansDate) {
        this.ansDate = ansDate;
    }

    public String getAnsBody() {
        return ansBody;
    }

    public void setAnsBody(String ansBody) {
        this.ansBody = ansBody;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
