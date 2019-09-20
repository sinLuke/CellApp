package com.cellgroup.cellapp.database;

public class CellAppDbSchema {

    public static final class AnimationBackgroundTable {
        public static final String NAME = "ANIMATION_BACKGROUND";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String IMAGE_URL = "IMAGE_URL";
            public static final String STEP_ID = "STEP_ID";
        }
    }

    public static final class AnimationItemTable {
        public static final String NAME = "ANIMATION_ITEM";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String END_POSITION_X = "END_POSITION_X";
            public static final String END_POSITION_Y = "END_POSITION_Y";
            public static final String IMAGE_URL = "IMAGE_URL";
            public static final String START_POSITION_X = "START_POSITION_X";
            public static final String START_POSITION_Y = "START_POSITION_Y";
            public static final String STEP_ID = "STEP_ID";
        }
    }

    public static final class DataVersionTable {
        public static final String NAME = "DATA_VERSION";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String VERSION = "VERSION";
        }
    }

    public static final class DocumentTable {
        public static final String NAME = "DOCUMENT";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String DIFFICULTY = "DIFFICULTY";
            public static final String IMAGE_URL = "IMAGE_URL";
            public static final String INTRODUCTION = "INTRODUCTION";
            public static final String TOPIC_ID = "TOPIC_ID";
        }
    }

    public static final class StepTable {
        public static final String NAME = "STEP";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String AUTO_ANIMATION = "AUTO_ANIMATION";
            public static final String DOCUMENT_ID = "DOCUMENT_ID";
            public static final String IMAGE_URL = "IMAGE_URL";
            public static final String PAGE_NUMBER = "PAGE_NUMBER";
            public static final String TEXT = "TEXT";
        }
    }

    public static final class TopicTable {
        public static final String NAME = "TOPIC";

        public static final class Cols {
            public static final String ID = "ID";
            public static final String IMAGE_URL = "IMAGE_URL";
            public static final String TOPIC_NAME = "TOPIC_NAME";
        }
    }

}
