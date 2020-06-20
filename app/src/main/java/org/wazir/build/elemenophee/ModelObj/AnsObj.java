package org.wazir.build.elemenophee.ModelObj;

public class AnsObj {
    String teaId, Answer, time;
    boolean satisfied;

    public AnsObj(String teaId, String answer, String time, boolean satisfied) {
        this.teaId = teaId;
        Answer = answer;
        this.time = time;
        this.satisfied = satisfied;
    }

    public AnsObj() {
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
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
