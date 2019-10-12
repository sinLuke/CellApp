package com.cellgroup.cellapp;

import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.models.Topic;

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

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
        setCurrentDoc(currentStep.doc.get());
    }

    private Topic currentTopic;
    private Doc currentDoc;
    private Step currentStep;


}
