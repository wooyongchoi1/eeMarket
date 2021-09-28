package com.example.market;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CheckPasswordActivity extends AppCompatActivity {

    private String userID;
    private Boolean check;
    private EditText editText;
    private TextView useridView;
    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpassword);
        userID = getIntent().getExtras().getString("userID");
        editText = findViewById(R.id.editTextTextPassword);
        useridView = findViewById(R.id.checkpassword_useridview);
        checkButton = findViewById(R.id.check);


        useridView.setText(userID);

        checkButton.setOnClickListener((v)->{
            String pw = editText.getText().toString();
            BackgroundTask backgroundTask = new BackgroundTask(userID, pw);

            try {
                check = backgroundTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("check", check.toString());
            if(!check){
                Toast.makeText(getApplicationContext(),"비밀번호를 다시 확인해 주세요",Toast.LENGTH_SHORT).show();
                editText.setText("");
            } else{
                Intent intent = new Intent();
                intent.putExtra("check", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void, Void, Boolean> {
        //접속할 주소
        String target;
        String userid;
        String pw;

        public BackgroundTask(String id, String pw){userid = id;this.pw = pw; }

        @Override
        protected  void onPreExecute(){
            target = "https://hmm8816.cafe24.com/dbeditor/UserLogin.php?userID="+userid+"&userPassword="+pw;
            Log.d("url", target);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine())!=null){
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                return jsonObject.getBoolean("success");
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(Boolean result){
                //받아오는 데이터가 없음
                Log.d("password", result.toString());
        }
    }
}
