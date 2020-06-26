package org.wazir.build.elemenophee.ModelObj;

public class QuesDispObj {
    String StuProPic, StuName, date, quesId, question;
    int upVotes, answersCount;
    boolean solved;

    public QuesDispObj() {
        this.upVotes = 0;
        this.answersCount = 0;
    }

    public QuesDispObj(String stuProPic, String stuName, String date, String quesId, String question, int upVotes, int answersCount, boolean solved) {
        StuProPic = stuProPic;
        StuName = stuName;
        this.date = date;
        this.quesId = quesId;
        this.question = question;
        this.upVotes = upVotes;
        this.answersCount = answersCount;
        this.solved = solved;
    }

    public String getStuProPic() {
        return StuProPic;
    }

    public void setStuProPic(String stuProPic) {
        StuProPic = stuProPic;
    }

    public String getStuName() {
        return StuName;
    }

    public void setStuName(String stuName) {
        StuName = stuName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(int answersCount) {
        this.answersCount = answersCount;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
