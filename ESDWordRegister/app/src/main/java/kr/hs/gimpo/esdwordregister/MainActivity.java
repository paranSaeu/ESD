package kr.hs.gimpo.esdwordregister;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD");
        
        final TextView word = findViewById(R.id.word_text),
                category = findViewById(R.id.word_directory),
                meaning = findViewById(R.id.word_meaning),
                etymology = findViewById(R.id.word_fromwhat);
        final EditText word_text = findViewById(R.id.word),
                category_text = findViewById(R.id.editText),
                category2_text = findViewById(R.id.editText2),
                meaning_text = findViewById(R.id.meaning),
                etymology_text = findViewById(R.id.from);
        
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("word").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(word_text.getText().toString().compareTo("")!=0) {
                            if(dataSnapshot.child(word_text.getText().toString()).exists()){
                                Word wordData = dataSnapshot.child(word_text.getText().toString()).getValue(Word.class);
                                if(wordData != null) {
                                    word.setText(wordData.get_Word());
                                    word_text.setText(wordData.get_Word());
                                    category.setText(wordData.get_Category());
                                    String[] cat = wordData.get_Category().split("/");
                                    category_text.setText(cat[0]);
                                    category2_text.setText(cat[1]);
                                    meaning.setText(wordData.get_Meaning());
                                    meaning_text.setText(wordData.get_Meaning());
                                    etymology.setText(wordData.get_Etymology());
                                    etymology_text.setText(wordData.get_Etymology());
                                } else {
                                    Log.d("wordData", "wordData is NULL! but... why?");
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "에러가 발생했습니다.\n데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "에러가 발생했습니다.\n조회할 단어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    
                    }
                });
            }
        });
        
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(word_text.getText().toString().compareTo("")!=0 &&
                        category_text.getText().toString().compareTo("")!=0 &&
                        category2_text.getText().toString().compareTo("")!=0 &&
                        meaning_text.getText().toString().compareTo("")!=0 &&
                        etymology_text.getText().toString().compareTo("")!=0) {
                    mDatabase.child("word").child(word_text.getText().toString()).setValue(new Word(word_text.getText().toString(), meaning_text.getText().toString(), etymology_text.getText().toString(), category_text.getText().toString() + "/" + category2_text.getText().toString()));
                    Log.d("Register", "Registration completed successfully!");
                    word.setText(word_text.getText());
                    word_text.setText("");
                    category.setText(category_text.getText() + "/" + category2_text.getText());
                    category_text.setText("");
                    category2_text.setText("");
                    meaning.setText(meaning_text.getText());
                    meaning_text.setText("");
                    etymology.setText(etymology_text.getText());
                    etymology_text.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "에러가 발생했습니다.\n모든 항목이 작성되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("Register", "Registration failed!");
                }
            }
        });
    }
}
