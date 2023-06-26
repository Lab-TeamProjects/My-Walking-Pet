package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lab_team_projects.my_walking_pet.db.AppDatabase;

/**
 * 걸음 정보를 실시간으로 반환하는 뷰모델 클래스입니다.
 * 일일 걸음 통계 탭에서 실시간으로 걸음 수가 변경되는 것을 시각적으로 볼 수 있습니다.
 */
public class WalkViewModel extends AndroidViewModel {
    private final LiveData<Walk> walkLiveData;
    private final AppDatabase db;

    /**
     * Instantiates a new Walk view model.
     *
     * @param application the application
     */
    public WalkViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        walkLiveData = db.walkDao().getWalkLast();
    }

    /**
     * 실시간으로 변수에 접근하여 값을 반환한는 클래스
     *
     * @return walk live data
     */
    public LiveData<Walk> getWalkLiveData() {
        return walkLiveData;
    }

    /**
     * Update walk.
     *
     * @param walk the walk
     */
    public void updateWalk(Walk walk) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getInstance(getApplication()).walkDao().update(walk);
        });
    }

}
