package org.wazir.build.elemenophee.ModelObj;

public class QuestionObj {
    private String stuId, question, subject, subClass, time, ques_id;
    private boolean satisfied;

    public QuestionObj() {
    }

    public QuestionObj(String stuId, String question, String subject, String subClass, String time, boolean satisfied) {
        this.stuId = stuId;
        this.question = question;
        this.subject = subject;
        this.subClass = subClass;
        this.time = time;
        this.satisfied = satisfied;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
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

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }
}
