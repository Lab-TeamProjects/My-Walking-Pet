package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.databinding.CustomWalkViewDialogBinding;

public class CustomWalkViewDialog extends Dialog {

    public interface DialogCancelListener {
        void onDialogCancel();
    }

    private DialogCancelListener dialogCancelListener;

    public void setDialogCancelListener(DialogCancelListener dialogCancelListener) {
        this.dialogCancelListener = dialogCancelListener;
    }

    private CustomWalkViewDialogBinding binding;
    private WindowManager.LayoutParams params;

    public CustomWalkViewDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CustomWalkViewDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        this.getWindow().setAttributes(params);

        binding.tvWalkCount.setText("12020");
        binding.tvGoalCount.setText("120");
    }

    @Override
    public void cancel() {
        super.cancel();
        dialogCancelListener.onDialogCancel();
    }
}
