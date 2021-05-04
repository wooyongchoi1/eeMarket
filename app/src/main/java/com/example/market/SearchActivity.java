package com.example.market;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    final int PERMISSION = 1;
    Button stt, searchKeyword;
    SpeechRecognizer mRecognizer;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        editText = findViewById(R.id.editText_search);
        searchKeyword = findViewById(R.id.searchKeyword);
        searchKeyword.setOnClickListener(v ->{
            Intent intent = new Intent(this, SearchList.class);
            intent.putExtra("keyword",editText.getText().toString());
            Log.d("keyword", editText.getText().toString());
            startActivity(intent);
        });
        stt = findViewById(R.id.buttonstt);
        stt.setOnClickListener(v -> {
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
            mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(new RecognitionListener(){
                @Override
                public void onReadyForSpeech(Bundle params) { Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show(); }
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
                    String message;
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
                    Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResults(Bundle results) { // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    for(int i = 0; i < matches.size() ; i++){ editText.setText(matches.get(i)); }
                }
                @Override
                public void onPartialResults(Bundle partialResults) {}
                @Override
                public void onEvent(int eventType, Bundle params) {}
            });
            mRecognizer.startListening(intent);
        });
    }
}
