package com.cellgroup.cellapp;

import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.models.UserHistory;
import com.cellgroup.cellapp.network.CreateUserHistoryCallBackDelegate;
import com.cellgroup.cellapp.network.UserManager;

public class AppState {
    static public AppState shared = new AppState();
    private AppState() {return;}

    public Topic getCurrentTopic() {
        return currentTopic;
    }

    public Doc getCurrentDoc() {
        return currentDoc;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public void setCurrentTopic(Topic currentTopic) {
        this.currentTopic = currentTopic;
    }

    public void setCurrentDoc(Doc currentDoc) {
        this.currentDoc = currentDoc;
        setCurrentTopic(currentDoc.topic.get());
    }

    public void setCurrentStep(Step step) {

        this.currentStep = step;

        setCurrentDoc(this.currentStep.doc.get());
    }

    public void setStepFinished(final Step step) {
        if (!UserManager.shared.isStepCompleted(step)){
            UserHistory.createUserHistory(step, new CreateUserHistoryCallBackDelegate() {
                @Override
                public void handler(UserHistory userHistory) {
                    UserManager.shared.addUserHistory(step, userHistory);
                }
            });
        }
    }

    public void setDocFinished(final Doc doc) {

    }

    private Topic currentTopic;
    private Doc currentDoc;
    private Step currentStep;


}
