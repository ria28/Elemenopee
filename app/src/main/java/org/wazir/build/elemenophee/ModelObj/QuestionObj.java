package org.wazir.build.elemenophee.ModelObj;

public class QuestionObj {
    private String question, subject, subClass, time, ques_id;
    private String StuName, stuProfile;
    int likes, ansCount;

    public QuestionObj() {
    }

    public QuestionObj(String question, String subject, String subClass, String time, boolean satisfied) {

        this.question = question;
        this.subject = subject;
        this.subClass = subClass;
        this.time = time;
        likes = 0;
        ansCount = 0;
    }

    public String getStuName() {
        return StuName;
    }

    public void setStuName(String stuName) {
        StuName = stuName;
    }

    public String getStuProfile() {
        return stuProfile;
    }

    public void setStuProfile(String stuProfile) {
        this.stuProfile = stuProfile;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getAnsCount() {
        return ansCount;
    }

    public void setAnsCount(int ansCount) {
        this.ansCount = ansCount;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
