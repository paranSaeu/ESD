package kr.hs.gimpo.esdhatespeechdictionary;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.chaos);
        textView.setText("살려줘...");
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                public void onClick(View v) {
                    TextView textView2 = (TextView) findViewById(R.id.error_text);
                    textView2.setText("please...");

                }
        });
    }



}