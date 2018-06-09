package kr.hs.gimpo.esdhatespeechdictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        // 인트로 화면에서 해야 할 것은?
        
        // 초기화 완료 후...
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
