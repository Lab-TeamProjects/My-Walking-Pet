package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSHelper {
    private final String SPEAK_ID = "TTS_HELPER";    // 음성 출력 식별 아이디
    private TextToSpeech tts;
    private boolean isTtsInitialized = false; // TTS 사용 가능 여부 체크 변수

    public TTSHelper(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);    // 한국어
                    isTtsInitialized = true;
                    tts.setPitch(1.0f);    // 목소리 높낮이 설정
                    tts.setSpeechRate(1.0f);    // 목소리 속도 설정
                }
            }
        });
    }

    public void speak(String text) {
        int speakMode = TextToSpeech.QUEUE_ADD;

        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);

        if (isTtsInitialized) {
            tts.speak(text, speakMode, params, SPEAK_ID);
        }
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
