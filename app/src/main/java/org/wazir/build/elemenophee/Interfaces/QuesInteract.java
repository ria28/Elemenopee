package org.wazir.build.elemenophee.Interfaces;

public interface QuesInteract {
    void voteQuestion(String quesId, int pos);

    void giveSolution(String quesId, int pos);

    void viewQuestion(String quesId, int pos);
}
