package com.madhu.smartmobilefeatures;

public class SharedPrefVariables {

    private static final String prefName="Feature Settings";
    private static final String newPref="newPref";


    /////Features Preferences////
    private static final String auto_call="auto-call";


    public static String getPrefName() {
        return prefName;
    }

    public static String getNewPref() {
        return newPref;
    }

    public static String getAuto_call() {
        return auto_call;
    }
}
