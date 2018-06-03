package kr.hs.gimpo.esdhatespeechdictionary;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kr.hs.gimpo.esdhatespeechdictionary.Fragment.QuitPromptDialogFragment;


public class MainActivity extends AppCompatActivity implements kr.hs.gimpo.esdhatespeechdictionary.Fragment.QuitPromptDialogFragment.QuitResponseListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button search = (Button) findViewById(R.id.search);
        
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                TextView word = (TextView) findViewById(R.id.word_hint);
                
                word.setText(editText.getText());
                editText.setText("");
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        DialogFragment dialogFragment = new QuitPromptDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "quit");
    }
    
    @Override
    public void onPositiveResponse(DialogFragment dialogFragment) {
        finish();
    }
    
    @Override
    public void onNegativeResponse(DialogFragment dialogFragment) {
    
    }
}