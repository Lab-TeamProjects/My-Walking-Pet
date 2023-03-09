package com.lab_team_projects.my_walking_pet.app;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kotlin._Assertions;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerConnection {


    private String serverUrl = "http://203.232.193.164:5000/"; // 서버 기본 url

    private ClientCallBackListener clientCallBackListener = null;

    public void setClientCallBackListener(ClientCallBackListener clientCallBackListener) {
        this.clientCallBackListener = clientCallBackListener;
    }

    public interface ClientCallBackListener {
        void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
    }

    /**
     * 서버에 http 요청을 보내고 반환값을 받는 클래스
     * @param url : 파일경로(path)
     * @param jsonObject : 보낼 메시지를 담은 객체
     */
    public ServerConnection(String url, JSONObject jsonObject) {
        serverUrl = serverUrl.concat(url);

        RequestBody reqBody = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        requestClient(reqBody, serverUrl);
    }

    /**
     * 서버에 데이터를 보내고 반환값을 받는 함수
     * @param requestBody : 보낼 메시지를 담은 객체
     * @param serverUrl : 서버 Url
     */
    private void requestClient(RequestBody requestBody, String serverUrl) {
        Request request = new Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("__star__", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                clientCallBackListener.onResponse(call, response);
            }
        });
    }
}
