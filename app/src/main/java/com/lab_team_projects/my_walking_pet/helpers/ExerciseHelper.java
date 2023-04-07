package com.lab_team_projects.my_walking_pet.helpers;

public class ExerciseHelper {
    private static final ExerciseHelper instance = new ExerciseHelper();

    private OnExerciseListener listener;

    public interface OnExerciseListener {
        void onFinish(boolean flag);
    }

    public void setListener(OnExerciseListener listener) {
        this.listener = listener;
    }

    public OnExerciseListener getListener() {
        return listener;
    }

    public static ExerciseHelper getInstance() {
        return instance;
    }

    private ExerciseHelper() {}
}
