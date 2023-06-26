package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.databinding.CustomWalkViewDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 걸음 통계 차트를 클릭할 때 나오는 다이얼로그 정의 클래스
 */
public class CustomWalkViewDialog extends Dialog {

    /**
     * 차트에 해당하는 걸음 클래스
     */
    private final Walk walk;

    /**
     * 다이얼로그를 종료할 때 감지되는 리스너
     */
    public interface DialogCancelListener {
        /**
         * On dialog cancel.
         */
        void onDialogCancel();
    }

    private DialogCancelListener dialogCancelListener;

    /**
     * Sets dialog cancel listener.
     *
     * @param dialogCancelListener the dialog cancel listener
     */
    public void setDialogCancelListener(DialogCancelListener dialogCancelListener) {
        this.dialogCancelListener = dialogCancelListener;
    }

    private CustomWalkViewDialogBinding binding;
    private WindowManager.LayoutParams params;

    /**
     * 다이얼로그 생성자
     *
     * @param context 메인 스레드에 접근하기 위한 Context 클래스를 받습니다.
     * @param walk    차트에 해당하는 걸음 정보를 멤버변수 걸음 객체에 저장합니다.
     */
    public CustomWalkViewDialog(@NonNull Context context, Walk walk) {
        super(context);
        this.walk = walk;
    }

    /**
     * 걸음 정보 다이얼로그에서 걸음 정보 객체에 해당하는 정보를 다이얼로그 창에서 자세하게 볼 수 있습니다.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CustomWalkViewDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        this.getWindow().setAttributes(params);


        binding.tvWalkCount.setText(String.valueOf(walk.getCount()));
        binding.tvGoalCount.setText(String.valueOf(walk.getGoal()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date walkDate;
        try {
            walkDate = sdf.parse(walk.getDate());
            sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
            String time = sdf.format(walkDate);
            binding.tvTitle.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.tvCalories.setText(String.valueOf(walk.getKcal()));
        binding.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", walk.getDistance()));
        binding.tvTime.setText(walk.calculateHours());
    }

    /**
     * 클릭된 바차트의 색깔을 변경하기 위한 다이얼로그 취소 리스너 메서드를 호출합니다.
     */
    @Override
    public void cancel() {
        super.cancel();
        dialogCancelListener.onDialogCancel();
    }
}
