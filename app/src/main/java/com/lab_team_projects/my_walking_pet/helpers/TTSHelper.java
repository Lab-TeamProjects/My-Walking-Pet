package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * 걸음 패턴 설계 다이얼로그에서 사용자의 안전을 위해
 * 현재 상태를 TTS 를 이용하여 사용자에게 알려줍니다.
 * <p>
 * 구글 TextToSpeech클래스를 이용합니다.
 */
public class TTSHelper {
    private final String SPEAK_ID = "TTS_HELPER";    // 음성 출력 식별 아이디
    private TextToSpeech tts;
    private boolean isTtsInitialized = false; // TTS 사용 가능 여부 체크 변수

    /**
     * Instantiates a new Tts helper.
     *
     * @param context the context
     */
    public TTSHelper(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);    // 한국어
                    isTtsInitialized = true;
                    tts.setPitch(2.0f);    // 목소리 높낮이 설정
                    tts.setSpeechRate(1.0f);    // 목소리 속도 설정
                }
            }
        });
    }

    /**
     * Speak.
     *
     * @param text the text
     */
    public void speak(String text) {
        int speakMode = TextToSpeech.QUEUE_ADD;

        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);

        if (isTtsInitialized) {
            tts.speak(text, speakMode, params, SPEAK_ID);
        }
    }

    /**
     * Destroy.
     */
    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
