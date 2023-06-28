package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PROFILE_PHOTO;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PROFILE_SETTING;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 서버와 통신을 담당하는 헬퍼
 */
public class ServerConnectionHelper {
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String serverUrl = "http://203.232.193.164:5000/"; // 서버 기본 url

    private ClientCallBackListener clientCallBackListener = null;



    /**
     * Sets client call back listener.
     *
     * @param clientCallBackListener the client call back listener
     */
    public void setClientCallBackListener(ClientCallBackListener clientCallBackListener) {
        this.clientCallBackListener = clientCallBackListener;
    }

    /**
     * The interface Client call back listener.
     */
    public interface ClientCallBackListener {
        /**
         * On response.
         *
         * @param call     the call
         * @param response the response
         * @throws IOException the io exception
         */
        void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
    }

    /**
     * Instantiates a new Server connection helper.
     *
     * @param url         the url
     * @param accessToken the access token
     */
    public ServerConnectionHelper(String url, String accessToken) {
        serverUrl = serverUrl.concat(url);

        Request request = new Request.Builder()
                .url(serverUrl)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        requestClient(request);
    }
    /**
     * 서버에 데이터를 담은 객체를 전송하기위한 클래스
     *
     * @param url         : 서버주소
     * @param is_get      : get 판별 변수
     */
    public ServerConnectionHelper(String url, Boolean is_get) {
        serverUrl = serverUrl.concat(url);

        Request request = new Request.Builder()
                .url(serverUrl)
                .get()
                .build();

        requestClient(request);
    }

    /**
     * 서버에 데이터를 담은 객체를 전송하기위한 클래스
     *
     * @param url        : 서버주소
     * @param jsonObject : 보낼 메시지를 담은 객체
     */
    public ServerConnectionHelper(String url, JSONObject jsonObject) {
        serverUrl = serverUrl.concat(url);

        RequestBody reqBody = RequestBody.create(
                jsonObject.toString(),
                JSON
        );

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(reqBody)
                .build();

        requestClient(request);
    }

    /**
     * 서버에 데이터를 담은 객체를 전송하기위한 클래스
     *
     * @param url         : 서버주소
     * @param jsonObject  : 보낼 메시지를 담은 객체
     * @param accessToken : 사용자를 구분하기위한 액세스 토큰
     */
    public ServerConnectionHelper(String url, JSONObject jsonObject, String accessToken) {
        serverUrl = serverUrl.concat(url);

        Log.e("json", jsonObject.toString());

        RequestBody reqBody = RequestBody.create(
                jsonObject.toString(),
                JSON
        );

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(reqBody)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        requestClient(request);
    }


    /**
     * Instantiates a new Server connection helper.
     *
     * @param dir         the dir
     * @param photoName   the photo name
     * @param accessToken the access token
     */
// 이미지 전송(인코딩 -> base64) 추가해야하고,
    public ServerConnectionHelper(File dir, String photoName, String accessToken) {
        serverUrl = serverUrl.concat(PROFILE_PHOTO);
        File file = new File(dir, photoName);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] imgByte = baos.toByteArray();
        String encodedImageString = Base64.getEncoder().encodeToString(imgByte);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("img", encodedImageString);
            RequestBody reqBody = RequestBody.create(encodedImageString, JSON);

            Request request = new Request.Builder()
                    .url(serverUrl)
                    .post(reqBody)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            requestClient(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 서버에 데이터를 보내고 반환값을 받는 함수
     * @param request : 보낼 메시지와 헤더를 담은 객체
     */
    private void requestClient(Request request) {
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
