package org.wazir.build.elemenophee.ModelObj;

import java.util.ArrayList;

public class QuestionObj {
    private String stuId, question, subject, subClass, time;
    private boolean satisfied;
    private ArrayList<AnsObj> ansObjs;

    public QuestionObj(String stuId, String question, String subject, String subClass, String time, boolean satisfied) {
        this.stuId = stuId;
        this.question = question;
        this.subject = subject;
        this.subClass = subClass;
        this.time = time;
        this.satisfied = satisfied;
        ansObjs = new ArrayList<>();
    }

    public QuestionObj() {
        ansObjs = new ArrayList<>();
    }

    public ArrayList<AnsObj> getAnsObjs() {
        return ansObjs;
    }

    public void setAnsObjs(ArrayList<AnsObj> ansObjs) {
        this.ansObjs = ansObjs;
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
