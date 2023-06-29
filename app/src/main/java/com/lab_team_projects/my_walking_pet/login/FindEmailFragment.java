package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.FAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.FIND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentFindEmailBinding;
import com.lab_team_projects.my_walking_pet.databinding.FragmentHomeBinding;
import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * The type Find email fragment.
 */
public class FindEmailFragment extends Fragment {

    private FragmentFindEmailBinding binding;

    /**
     * Instantiates a new Find email fragment.
     */
    public FindEmailFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindEmailBinding.inflate(inflater, container, false);

        // 이메일 찾기 버튼 클릭 리스너
        binding.btnFindEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", binding.etEmail.getText().toString());
                } catch (JSONException e) { Log.e("JsonInput", "JSONException", e); }
                ServerConnectionHelper sc = new ServerConnectionHelper(FIND_EMAIL, jsonObject);
                sc.setClientCallBackListener(((call, response) -> {
                    try {
                        JSONObject responseJson = null;
                        responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                        String result = responseJson.getString("result");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        switch(result) {
                            case SUCCESS:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog dialog;
                                        builder.setMessage("가입되어 있는 이메일입니다")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        dialog = builder.create();
                                        dialog.show();
                                    }
                                });
                                break;
                            case FAIL:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog dialog;
                                        builder.setMessage("가입되어 있지 않은 이메일입니다")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        dialog = builder.create();
                                        dialog.show();
                                    }
                                });
                                break;
                        }
                    } catch (JSONException e) {
                        Log.e("JsonOutput", "JSONException", e);
                    }
                }));
            }
        });

        return binding.getRoot();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}