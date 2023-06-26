package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.login.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 포그라운드 서비스 클래스
 * 백그라운드에서 걸음수 감지 센서를 이용하여 사용자가 걷거나 뛸 때 걸음 수를 카운트하여
 * 기기 내부 저장소에 저장합니다.
 * <p>
 * 가속도 적분을 이용하여 사용자의 지정 시간 동안의 속도를 구하여
 * 사용자가 걷고 있는지 뛰고 있는지 판단하여 걸음 객체의 걸음 수 관련 변수에 각각 저장합니다.
 */
public class WalkCountForeGroundService extends Service implements SensorEventListener {

    /**
     * 백그라운드에서 서비스가 동작할 수 있도록 하는 클래스
     */
    public static BackgroundTask task;
    /**
     * The constant value.
     */
    public static int value = 0;
    /**
     * 포그라운드 서비스가 동작하면 모바일 알림창에 앱이 동작중이라고 알려주는 클래스
     */
    private NotificationManager notificationManager;
    private Walk walk;
    private final GameManager gm = GameManager.getInstance();
    private final User user = gm.getUser();
    private final AppDatabase db = AppDatabase.getInstance(this);

    private int lastStep = 0;
    private boolean isFirstRun = false;

    private double currentVelocity = 0.0;  // 초기 속도는 0으로 설정
    private double lastUpdateVelocityKmH = 0.0; // 이전 속도
    private long lastUpdateTime = 0;  // 마지막 가속도 측정 시간을 저장하는 변수
    private long lastResetTime = 0;     // 1 초 기준 저장 변수
    private double accelerationMagnitudeAtLastUpdate = 0.0;  // 마지막 가속도 측정 값


    /**
     * Instantiates a new Walk count fore ground service.
     */
    public WalkCountForeGroundService() {
        // 빈 생성자
    }

    /**
     * 현재 포그라운드 서비스가 동작중인지 판단하는 메서드
     *
     * @return 동작을 판단하여 bool 형태로 반환
     */
    public boolean isRunning(){
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * 서비스가 생성되면 실행되는 메서드
     * 가속도 센서, 걸음 카운트 센서, 자이로스코프 센서를 설정합니다.
     * 각각의 센서 리스너를 등록하여 센서에서 신호를 감지하면 메서드를 실행합니다.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            task = new BackgroundTask(gm, getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        walk = gm.getWalk();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 가속도 센서, 걸음 감지 센서, 자이로스코프 센서 등록 등록
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // 센서 리스너 등록
        if (accelSensor != null) {
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (stepDetectorSensor!= null) {
            Log.d("__walk", "디텍터 있음");
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (stepCounter!= null) {
            Log.d("__walk", "카운터 있음");
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (gyroSensor!= null) {
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        task.execute();
        initializeNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    /**
     * Stop task.
     */
    public void stopTask() {
        task.onCancelled();
        task.cancel(true);
        task = null;
    }

    /**
     * 현재 선택된 펫의 진화 상태를 알립니다.
     * 서비스가 동작중일 때 알림창은 항상 표시되며 서비스가 중지되면 알림창은 종료됩니다.
     *
     * @return 알림 빌더 클래스를 반환합니다.
     */
// 진화 알람
    public Notification evolutionNotification() {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음\n\uD83D\uDC5F 목표 걸음까지 %d걸음 남았습니다!", walk.getCount(), walk.getGoal()))
                .setBigContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(bigTextStyle)
                .setContentText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음 남았습니다!", walk.getCount()))
                .setContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()))
                .setOngoing(true)
                .setNumber(0)    // 앱 뱃지 뜨는 것이 별로라서 없애고 싶은데 안없어짐
                .setWhen(0)
                .setShowWhen(false);

        // 알림창을 클릭했을 때 액티비티 이동
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1","포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
        }

        return builder.build();
    }

    /**
     * 현재 사용자의 걸음 수와 펫의 진화 상태를 알림창으로 보여줍니다.
     * 서비스가 동작중일 때 알림창은 항상 표시되며 서비스가 중지되면 알림창은 종료됩니다.
     *
     * @return 알림 빌더 클래스를 반환합니다.
     */
// 펫 현황
    public Notification makeNotification(){
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음\n\uD83D\uDC5F 목표 걸음까지 %d걸음 남았습니다!", walk.getCount(), walk.getGoal()))
                .setBigContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()));


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(bigTextStyle)
                .setContentText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음 남았습니다!", walk.getCount()))
                .setContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()))
                .setOngoing(true)
                .setNumber(0)    // 앱 뱃지 뜨는 것이 별로라서 없애고 싶은데 안없어짐
                .setWhen(0)
                .setShowWhen(false);

        Intent intent = new Intent(this, MainActivity.class);

        // 알림창을 클릭했을 때 액티비티 이동
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1","포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
        }

        return builder.build();
    }

    /**
     * 기본 알림창을 포그라운드 서비스로 실행합니다.
     */
    public void initializeNotification() {
        startForeground(1, makeNotification());
    }

    /**
     * 리스너로 등록된 센서가 이벤트를 감지하면 실행되는 메서드입니다.
     * 걸음 계수기 센서를 이용하여 사용자의 정확한 걸음 수를 반환받습니다.
     * 가속도 센서를 이용하여 사용자의 일정 시간 단위당 속도를 계산하여 사용자가 걷는지 뛰는지 판단합니다.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!isFirstRun) {
                lastStep = (int) event.values[0];
                isFirstRun = true;
            } else {
                int increaseValue = (int) event.values[0] - lastStep;
                step(increaseValue, lastUpdateVelocityKmH > 5.0);
                userMoneyUpdate(increaseValue);
                lastStep = (int) event.values[0];
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double xAcceleration = event.values[0];
            double yAcceleration = event.values[1];
            double zAcceleration = event.values[2];


            double accelerationMagnitude = Math.sqrt(Math.pow(xAcceleration, 2) + Math.pow(yAcceleration, 2) + Math.pow(zAcceleration, 2)); // 총 가속도 계산

            long currentTime = System.currentTimeMillis();  // 현재 시간을 저장
            if (lastUpdateTime == 0) {
                // 첫 번째 가속도 측정 값인 경우, 시간과 가속도를 저장하고 리턴

                lastUpdateTime = currentTime;
                accelerationMagnitudeAtLastUpdate = accelerationMagnitude;
                lastUpdateVelocityKmH = 0;
                return;
            }

            // 시간 간격과 마지막 가속도 측정 값에서 현재 가속도 측정 값까지의 변화량을 구함
            double deltaTime = (currentTime - lastUpdateTime) / 1000.0;  // 시간 간격을 초 단위로 변환
            double accelerationDelta = accelerationMagnitude + accelerationMagnitudeAtLastUpdate;


            if (currentTime - lastResetTime > 1000) {
                // 현재 속도를 구함 m/s
                currentVelocity = 0.5 * accelerationDelta * deltaTime;

                // 현재 시간, 가속도, 속도를 저장
                lastUpdateTime = currentTime;
                accelerationMagnitudeAtLastUpdate = accelerationMagnitude;
                lastUpdateVelocityKmH = currentVelocity / 3.6;  // km/h로 변환

                // 가만히 있는것으로 간주 하고 현재 속도 0으로 초기화
                if ((accelerationMagnitude > -1 && accelerationMagnitude < 1) && (Math.floor(accelerationMagnitude * 100) / 100 == Math.floor(accelerationMagnitudeAtLastUpdate * 100) / 100)) {
                    currentVelocity = 0;
                    lastUpdateTime = 0;
                }

                lastResetTime = currentTime;
            }
        }
    }

    /**
     * 사용자가 걸은 수 만큼 사용자의 재화로 저장합니다.
     */
    private void userMoneyUpdate(int addMoney) {
        UserPreferenceHelper helper =
                new UserPreferenceHelper(getApplicationContext()
                        , UserPreferenceHelper.UserTokenKey.login.name());

        helper.saveIntValue(UserPreferenceHelper.UserPreferenceKey.money.name()
                , addMoney + gm.getUser().getMoney());
    }

    /**
     * 걸음 센서가 감지하면 메서드를 호출합니다.
     * 현재 걸음 상태 변수에 따라서 현재 걸음 객체에 걸음 수를 저장합니다.
     * 저장된 걸음 수 만큼 현재 선택된 동물의 수치에 영향을 줍니다.
     * @param step 걸음 수
     * @param isRunning 현재 사용자가 뛰는지 걷는지 판단하는 플래그 변수
     */
    private void step(int step, boolean isRunning) {
        if (isRunning) {
            walk.setRunCount(walk.getRunCount() + step);
            walk.setDistance(walk.getDistance() + (step * (user.calculateRunStride() * 0.01) * 0.001));
        } else {
            walk.setWalkCount(walk.getWalkCount() + step);
            walk.setDistance(walk.getDistance() + (step * (user.calculateStride() * 0.01) * 0.001));
        }

        // 평균 수치(배고픔, 목마름, 청결도)에 따른 성장치 증가
        Animal animal = user.getAnimalList().get(user.getNowSelectedPet());
        float ave = animal.getStateAverage();
        if(ave >= 90) {
            animal.setGrowth(animal.getGrowth() + 3.0f);
        } else if (ave < 90 && ave >= 50) {
            animal.setGrowth(animal.getGrowth() + 2.0f);
        } else {
            animal.setGrowth(animal.getGrowth() + 1.0f);
        }

        Log.e("성장치", String.valueOf(animal.getGrowth()));

        if (animal.getGrowthCallback() != null) {
            animal.getGrowthCallback().onCall();
        }

        // 걷는 도중에 진화할 시 알람
        if (animal.getGrowth() >= animal.getMaxGrowth()) {
            startForeground(2, evolutionNotification());
        }

        walk.setCount(walk.getWalkCount() + walk.getRunCount());
        walk.setSec(walk.calculateSec(user));
        if (gm != null) {
            walk.calculateKcal(user);
        }
        db.walkDao().update(walk);

        // 센서 리로딩
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, makeNotification());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ForegroundService", "onDestroy");

    }

    /**
     * Println.
     *
     * @param msg the msg
     */
    public static void println(String msg) {
        Log.d("Foreground", msg);
    }


    /**
     * 백그라운드에서 동작하는 클래스입니다.
     * 날짜가 변경되는 00시에 현재 싱글톤 객체에 저장되어 있는 걸음 객체를 새로운 날짜에 맞춰서
     * 새로운 걸음 객체를 생성하고 교체합니다.
     */
// 스레드
    class BackgroundTask extends AsyncTask<Integer, String, Integer> {

        /**
         * The Current date.
         */
        Date currentDate;
        /**
         * The Sdf.
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        /**
         * The Walk date.
         */
        Date walkDate;
        /**
         * The Context.
         */
        Context context;
        /**
         * The Gm.
         */
        GameManager gm;

        /**
         * Instantiates a new Background task.
         *
         * @param gm      the gm
         * @param context the context
         * @throws ParseException the parse exception
         */
        public BackgroundTask(GameManager gm, Context context) throws ParseException {
            this.gm = gm;
            this.walkDate = sdf.parse(gm.getWalk().getDate());
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Integer... values) {

            while (!isCancelled()) {
                try {
                    currentDate = Calendar.getInstance().getTime(); // 현재 시간을 Date 객체로 변환

                    if (currentDate.before(walkDate)) {
                        Toast.makeText(context, "날짜가 변경되었습니다", Toast.LENGTH_SHORT).show();

                        Walk walk = new Walk();
                        walk.setDate(sdf.format(currentDate));
                        gm.setWalk(walk);
                        AppDatabase.getInstance(context).walkDao().insert(walk);

                        // 센서 리로딩


                        notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.notify(1, makeNotification());
                    }

                    Thread.sleep(1000);
                } catch (Exception ex) { }
            }
            return value;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            println("onProgressUpdate()업데이트");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            println("onPostExecute()");
        }

        @Override
        protected void onCancelled() {
            println("onCancelled()");
        }

    }
}