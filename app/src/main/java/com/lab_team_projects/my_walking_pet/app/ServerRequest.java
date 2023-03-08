package com.lab_team_projects.my_walking_pet.app;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerRequest {

    private String serverUrl = "http://203.232.193.164:5000/";


    private ClientCallBackListener clientCallBackListener = null;

    public void setClientCallBackListener(ClientCallBackListener clientCallBackListener) {
        this.clientCallBackListener = clientCallBackListener;
    }

    public interface ClientCallBackListener {
        void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
    }



    public ServerRequest(String url, JSONObject jsonInput) {
        serverUrl = serverUrl.concat(url);

        RequestBody reqBody = RequestBody.create(
                jsonInput.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        requestClient(reqBody, serverUrl);
    }

    public ServerRequest(String url, List<String[]> requestBodyElements) {
        serverUrl = serverUrl.concat(url);

        FormBody.Builder builder = new FormBody.Builder();
        for(String[] strings : requestBodyElements) {
            builder.add(strings[0], strings[1]);
        }

        RequestBody requestBody = builder.build();
        requestClient(requestBody, serverUrl);
    }

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
