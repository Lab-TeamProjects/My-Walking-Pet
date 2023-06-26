package com.lab_team_projects.my_walking_pet.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomExerciseDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.CustomExercisingDialogBinding;

/**
 * 운동 설계 커스텀 다이얼로그 클래스
 * 다이얼로그 클래스를 상속 받음
 */
public class CustomExerciseDialog extends Dialog {

    /**
     * 운동 설계 다이얼로그 바인딩 클래스
     * 안드로이드 xml 에서 id로 직접 접근 가능합니다.
     */
    private CustomExerciseDialogBinding exerciseBinding;
    /**
     * 설계된 운동의 현황 다이얼로그
     */
    private CustomExercisingDialogBinding exercisingBinding;
    private final Context context;
    /**
     * 운동하고자 하는 시간
     * 초기값 30
     */
    int selected = 30;
    /**
     * 포그라운드 서비스 스레드와 통신하기 위한 커스텀 바인더 클래스
     */
    private final MyBinder myBinder;

    private final boolean isExercising;
    /**
     * 운동 리스너
     */
    private OnExerciseListener onExerciseListener;

    /**
     * Sets on exercise listener.
     *
     * @param onExerciseListener the on exercise listener
     */
    public void setOnExerciseListener(OnExerciseListener onExerciseListener) {
        this.onExerciseListener = onExerciseListener;
    }

    /**
     * The interface On exercise listener.
     */
    public interface OnExerciseListener {
        /**
         * On exercise.
         *
         * @param flag     the flag
         * @param selected the selected
         */
        void onExercise(boolean flag, int selected);
    }

    /**
     * Instantiates a new Custom exercise dialog.
     *
     * @param context      the context
     * @param isExercising the is exercising
     * @param svc          the svc
     */
    public CustomExerciseDialog(@NonNull Context context, boolean isExercising, MyBinder svc) {
        super(context);
        this.context = context;
        this.getWindow().setDimAmount(0);    // 배경 어두워지는 것 없애기
        this.isExercising = isExercising;
        this.myBinder = svc;
    }


    /**
     * 다이얼로그가 생성되면 동작하는 메서드
     * 사용자는 시간을 설정할 수 있고 설정한 시간으로 인터벌 운동을 할 수 있습니다.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isExercising) {
            exerciseBinding = CustomExerciseDialogBinding.inflate(getLayoutInflater());
            setContentView(exerciseBinding.getRoot());

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
            setWindowPixels();
            selected = Integer.parseInt(exerciseBinding.tvTime.getText().toString());

            exerciseBinding.btnPlus.setOnClickListener(v->{
                if (selected < 100) {
                    selected += 10;
                    exerciseBinding.tvTime.setText(String.valueOf(selected));
                }
            });

            exerciseBinding.btnMinus.setOnClickListener(v->{
                if (selected > 30) {
                    selected -= 10;
                    exerciseBinding.tvTime.setText(String.valueOf(selected));
                }
            });

            exerciseBinding.tvOK.setOnClickListener(v->{
                // ok 버튼
                selected = Integer.parseInt(exerciseBinding.tvTime.getText().toString());
                Toast.makeText(context, String.valueOf(selected), Toast.LENGTH_SHORT).show();
                onExerciseListener.onExercise(true, selected);
                dismiss();
            });

            exerciseBinding.tvCancel.setOnClickListener(v->{
                dismiss();
            });

            exerciseBinding.tvResult.setOnClickListener(v->{
                CustomResultDialog customResultDialog = new CustomResultDialog(context);
                customResultDialog.show();

            });

        } else {
            exercisingBinding = CustomExercisingDialogBinding.inflate(getLayoutInflater());
            setContentView(exercisingBinding.getRoot());

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
            setWindowPixels();

            exercisingBinding.tvCurrent.setText(myBinder.getState());

            myBinder.setStateListener(new MyBinder.OnBinderStateListener() {
                @Override
                public void onChange(String state) {
                    exercisingBinding.tvCurrent.setText(state);
                }
            });

        }
    }

    /**
     * 스마트폰의 화면 크기 비율에 맞춰서 다이얼로그 창의 크기도 변경하는 메서드
     */
    private void setWindowPixels() {
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.5));
    }

}
