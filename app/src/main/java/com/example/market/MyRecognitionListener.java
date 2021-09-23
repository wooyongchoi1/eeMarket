package com.example.market;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class MyRecognitionListener implements RecognitionListener {
    protected String message;
    @Override
    public void onReadyForSpeech(Bundle params) { sttStart(); }
    @Override
    public void onBeginningOfSpeech() {}
    @Override
    public void onRmsChanged(float rmsdB) {}
    @Override
    public void onBufferReceived(byte[] buffer) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onError(int error) {

        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO: message = "오디오 에러";
                break;
            case SpeechRecognizer.ERROR_CLIENT: message = "클라이언트 에러";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: message = "퍼미션 없음";
                break;
            case SpeechRecognizer.ERROR_NETWORK: message = "네트워크 에러";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: message = "네트웍 타임아웃";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH: message = "찾을 수 없음";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: message = "RECOGNIZER가 바쁨";
                break;
            case SpeechRecognizer.ERROR_SERVER: message = "서버가 이상함";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: message = "말하는 시간초과"; break;
            default: message = "알 수 없는 오류임"; break;
        }
        //Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        sttError();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {}
    @Override
    public void onEvent(int eventType, Bundle params) {}

    public abstract void sttStart();
    public abstract void sttError();
}
