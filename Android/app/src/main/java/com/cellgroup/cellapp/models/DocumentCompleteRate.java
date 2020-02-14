package com.cellgroup.cellapp.models;

public class DocumentCompleteRate {
    private int stepCount;
    private int completionCount;
    public float rate;

    public DocumentCompleteRate(int stepCount, int completionCount) {
        this.stepCount = stepCount;
        this.completionCount = completionCount;

        if (stepCount == 0 || stepCount < completionCount) {
            this.rate = 0;
        } else {
            this.rate = ((float) completionCount) / ((float) stepCount);
        }
    }

    public boolean isFinished(){
        return (stepCount == completionCount) && (stepCount != 0);
    }
}
