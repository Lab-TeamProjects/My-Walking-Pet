package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lab_team_projects.my_walking_pet.db.AppDatabase;

public class WalkViewModel extends AndroidViewModel {
    private final LiveData<Walk> walkLiveData;
    private final AppDatabase db;

    public WalkViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        walkLiveData = db.walkDao().getWalkLast();
    }

    public LiveData<Walk> getWalkLiveData() {
        return walkLiveData;
    }

    public void updateWalk(Walk walk) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getInstance(getApplication()).walkDao().update(walk);
        });
    }

}
