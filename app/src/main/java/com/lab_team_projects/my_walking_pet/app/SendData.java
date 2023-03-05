package com.lab_team_projects.my_walking_pet.app;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SendData extends AsyncTask<Void, Void, String> {
    private final String urls;
    private JSONObject jsonInput;

    /**
     * 헤더, 값 문자열 배열, 주소 문자열을 입력받아 서버로 전송하는 클래스입니다.
     * @param jsonInput 서버로 전송할 JSON 객체
     * @param url 전송할 서버 주소
     *
     * @throws IOException 잘못된 url을 입력했을 경우
     */
    public SendData(JSONObject jsonInput, String url) {
        this.jsonInput = jsonInput;
        this.urls = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            // OkHttp 호출
            OkHttpClient client = new OkHttpClient();

            // RequestBody 에 json 객체 전달
            RequestBody reqBody = RequestBody.create(
                    jsonInput.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .post(reqBody)
                    .url(urls)
                    .build();

            Response responses;
            responses = client.newCall(request).execute();
            System.out.println(responses.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
