package kr.hs.gimpo.esdhatespeechdictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class IntroActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        // Firebase DB를 오프라인에서도 사용할 수 있도록 설정
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        
        // 초기화가 완료되면 MainActivity로 건너뜀
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
