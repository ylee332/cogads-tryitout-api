package com.ibm.cogads.utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Constants {

    public static final String FAQ = "FAQ";
    public static final String FREEFORM = "FREEFORM";
    public static final int FREEFORM_TOKEN_LIMIT = 5;

    public static final List<String> QUESTION_START_WORDS = Collections.unmodifiableList(Arrays.asList("q", "q.", "q:"));
    public static final List<String> QUESTION_END_WORDS = Collections.unmodifiableList(Arrays.asList("?"));

    public static final String WATSON_ASSISTANT_CREDENTIALS = "watson-assistant-credentials";
    public static final String KNOWLEDGE_KIT_IDS = "KNOWLEDGE_KIT_IDS";
    public static final String CORPUS_DOC = "gemini-corpus";
    public static final String WORKSPACE_DOC = "workspace_json";
    public static final String SCRAPED_DOC = "scraped-corpus";
    public static final String VARIABLES_DOC = "variables";

    public static final String KNOWLEDGE_KIT_DB = "reusable-faq";

    public static final String WIZARD_CORPUS = "Wizard-Corpus";
    public static final String CLOUDANT_IDS = "_id";
    public static final String WIZARD = "wizard";

    //ENV Variables
    public static final String ENV_NAME_WATSON_ASSISTANT_URL = "watsonAssistantUrl";
    public static final String ENV_NAME_WATSON_ASSISTANT_VERSION = "watsonAssistantVersion";
}
