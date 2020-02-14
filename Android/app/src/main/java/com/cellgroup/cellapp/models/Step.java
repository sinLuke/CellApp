package com.cellgroup.cellapp.models;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step {
    public String id;

    public String ANSWER;
    public String ANSWER_EXPLANATION;

    public int TYPE;
    public String[] CHOICES;
    public WeakReference<Doc> doc;
    public String IMAGE_SUBTITLE;
    public URL IMAGE_URL;
    public int PAGE_NUMBER;
    public String TEXT;
    public Boolean hidden = false;

    public Date created;

    public AnimationBackground animationBackground;
    public Map<String, AnimationItem> animationItems;

    static int AUTO = 1;
    static int INTERACTIVE = 0;
    static int MULTICHOICE = 10;
    static int FILLBLANKS = 11;

    public Step(){
        defaultInit();
    };

    public boolean isAnimation() {
        return this.animationItems.size() > 0;
    }

    public boolean isInteractive() {
        return this.TYPE == 0;
    }

    public boolean isQuiz() {
        return this.TYPE >= 10;
    }

    public boolean isMuiltipleChoice() {
        return this.TYPE == 10;
    }

    public boolean isAnswerCorrect(String answer) {

        String text1 = answer.toUpperCase().replaceAll("\\s+", "");
        String text2 = this.ANSWER.toUpperCase().replaceAll("\\s+", "");

        return text1.equals(text2);
    }

    public Step(Map<String, Object> data, String id) throws Exception {
        defaultInit();

        this.id = id;
        if (data.get("hidden") != null) {
            this.hidden = (Boolean) data.get("hidden") == true;
        }

        Object _ANSWER = data.get("ANSWER");
        Object _ANSWER_EXPLANATION = data.get("ANSWER_EXPLANATION");
        Object _CHOICES = data.get("CHOICES");
        Object _DOCUMENT_ID = data.get("DOCUMENT_ID");
        Object _IMAGE_SUBTITLE = data.get("IMAGE_SUBTITLE");
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _PAGE_NUMBER = data.get("PAGE_NUMBER");
        Object _TEXT = data.get("TEXT");
        Object _TYPE = data.get("TYPE");
        Object _CREATED = data.get("created");

        if (_ANSWER != null) {
            this.ANSWER = (String) _ANSWER;
        }

        if (_ANSWER_EXPLANATION != null) {
            this.ANSWER_EXPLANATION = (String) _ANSWER_EXPLANATION;
        }

        if (_CHOICES != null) {
            this.CHOICES = ((String) _CHOICES).split(",");
        }

        if (_TYPE != null) {
            this.TYPE = (int) (long) _TYPE;
        }

        if (_DOCUMENT_ID != null) {
            String docID = (String) _DOCUMENT_ID;
            DataManager dataManager = DataManager.shared;
            Doc doc = dataManager.getDocumentByID(docID);
            this.doc = this.doc = new WeakReference<Doc>(doc);;
        } else {
            throw new Exception();
        }

        if (_IMAGE_SUBTITLE != null) {
            this.IMAGE_SUBTITLE = (String) _IMAGE_SUBTITLE;
        }

        if (_IMAGE_URL != null) {
            try { this.IMAGE_URL =  new URL((String) _IMAGE_URL);
            } catch (Exception e) {
                throw new Exception();
            }
        }

        if (_PAGE_NUMBER != null) {
            this.PAGE_NUMBER = (int) (long) _PAGE_NUMBER;
        }

        if (_TEXT != null) {
            this.TEXT = (String) _TEXT;
        }

        if (_CREATED != null) {
            this.created = new Date((long) _CREATED);
        }
    }

    public void addAnimationItem(AnimationItem animationItem) {
        this.animationItems.put(animationItem.id, animationItem);
    }

    public void addAnimationBackground(AnimationBackground animationBackground) {
        this.IMAGE_URL = animationBackground.IMAGE_URL;
    }

    private void defaultInit(){
        this.id = "0";
        this.TYPE = AUTO;
        this.IMAGE_SUBTITLE = "Subtitle";
        this.IMAGE_URL = null;
        this.PAGE_NUMBER = 0;
        this.TEXT = "Add text Here ...";

        this.animationBackground = null;
        this.animationItems = new HashMap();

        this.created = new Date(0);
    }
}
