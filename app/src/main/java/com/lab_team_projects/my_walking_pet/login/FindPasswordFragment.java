package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.FAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.FIND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_AUTH_PASSWORD_RESET;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PASSWORD_RESET;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PASSWORD_RESET_AUTH;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PASSWORD_RESET_REQUEST;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentFindEmailBinding;
import com.lab_team_projects.my_walking_pet.databinding.FragmentFindPasswordBinding;
import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;
import com.lab_team_projects.my_walking_pet.helpers.UserAuthHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * The type Find password fragment.
 */
public class FindPasswordFragment extends Fragment {

    private FragmentFindPasswordBinding binding;
    private ServerConnectionHelper sch;
    /**
     * Instantiates a new Find password fragment.
     */
    public FindPasswordFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindPasswordBinding.inflate(inflater, container, false);


        binding.btnEmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", binding.etEmail.getText().toString());
                } catch (JSONException e) { Log.e("JsonInput", "JSONException", e); }

                sch = new ServerConnectionHelper(PASSWORD_RESET_REQUEST, jsonObject);
                sch.setClientCallBackListener(((call, response) -> {
                    try {
                        JSONObject responseJson = null;
                        responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                        String result = responseJson.getString("result");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        switch (result) {
                            case SUCCESS:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog dialog;
                                        builder.setMessage("이메일 인증을 완료한 후 확인 버튼을 눌러주세요")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // 인증여부 확인
                                                        ServerConnectionHelper isAuthCheck = new ServerConnectionHelper(PASSWORD_RESET_AUTH, jsonObject);
                                                        isAuthCheck.setClientCallBackListener(((call1, response1) -> {
                                                            try {
                                                                JSONObject responseJson = null;
                                                                responseJson = new JSONObject(Objects.requireNonNull(response1.body()).string());
                                                                String result = responseJson.getString("result");
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                                switch (result) {
                                                                    case SUCCESS:
                                                                        Log.e("result", result);
                                                                        getActivity().runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                binding.layoutEmailAuth.setVisibility(View.INVISIBLE);
                                                                                binding.layoutChangePW.setVisibility(View.VISIBLE);
                                                                            }
                                                                        });
                                                                        dialog.dismiss();
                                                                        break;
                                                                    case NOT_AUTH_PASSWORD_RESET:
                                                                        getActivity().runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(getActivity(), "인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                        break;
                                                                    default:
                                                                        getActivity().runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(getContext(), "알 수 없는 오류로 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                        break;
                                                                }
                                                            } catch (JSONException e) { Log.e("AUTH JSONException", "JSONException", e); }
                                                        }));
                                                    }
                                                })
                                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // 취소했을 경우
                                                        dialog.dismiss();
                                                    }
                                                });
                                        dialog = builder.create();
                                        dialog.show();
                                    }
                                });
                                break;
                            case NOT_FOUND_EMAIL:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "가입되지 않은 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            default:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "알 수 없는 오류로 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                    } catch (JSONException e) {
                        Log.e("btnEmailAuth", "JSONException", e);
                    }
                }));
            }
        });

        binding.etPassWordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = binding.etPassWord.getText().toString();
                String confirmPassword = s.toString();

                if (!password.isEmpty() && password.equals(confirmPassword)) {
                    // 비밀번호와 비밀번호 확인이 일치하는 경우
                    @SuppressLint("UseCompatLoadingForDrawables")
                    // 벡터 이미지를 Drawable 객체로 가져옵니다.
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_circle_green_24, null);
                    // 벡터 이미지의 크기를 설정합니다.
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    binding.etPassWordCheck.setCompoundDrawablesRelative(null,null, drawable, null);
                } else {
                    // 일치하지 않는 경우
                    binding.etPassWordCheck.setBackground(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnChangePW.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                UserAuthHelper uah = new UserAuthHelper(getActivity(),binding.etPassWord, binding.etPassWordCheck);
                if (uah.isPasswordConfirmed() && uah.isPasswordValid()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", binding.etEmail.getText().toString());
                        jsonObject.put("password", binding.etPassWord.getText().toString());
                    } catch (JSONException e) { Log.e("JsonInput", "JSONException", e); }
                    sch = new ServerConnectionHelper(PASSWORD_RESET, jsonObject );
                    sch.setClientCallBackListener(((call, response) -> {
                        try {
                            JSONObject responseJson = null;
                            responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                            String result = responseJson.getString("result");
                            switch (result) {
                                case SUCCESS:
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                            getActivity().finish();
                                        }
                                    });
                                    break;
                                case FAIL:
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default:
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "알 수 없는 오류로 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                            }
                        } catch (JSONException e) { Log.e("change password", "JSONException", e); }

                    }));
                } else {
                    uah.updatePasswordCheckValidationStatus();
                    uah.updatePasswordValidationStatus();
                }
            }
        });

        return binding.getRoot();
    }



}