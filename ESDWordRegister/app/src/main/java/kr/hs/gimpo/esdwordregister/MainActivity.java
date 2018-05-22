package kr.hs.gimpo.esdwordregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    
    Spinner category1, category2;
    EditText manualcategory1, manualcategory2;
    EditText word, meaning, from;
    TextView dir, word_m, word_f;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        category1 = (Spinner) findViewById(R.id.category1);
        category2 = (Spinner) findViewById(R.id.category2);
        manualcategory1 = (EditText) findViewById(R.id.manualcategory1);
        manualcategory2 = (EditText) findViewById(R.id.manualcategory2);
        word = (EditText) findViewById(R.id.word);
        meaning = (EditText) findViewById(R.id.meaning);
        from = (EditText) findViewById(R.id.from);
        dir = (TextView) findViewById(R.id.word_directory);
        word_m = (TextView) findViewById(R.id.word_meaning);
        word_f = (TextView) findViewById(R.id.word_fromwhat);
    
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference db = database.getReference("ESD");
        
        db.child("대분류").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("대분류", dataSnapshot.getValue(String.class));
                
                String[] category1List = (dataSnapshot.getValue(String.class) + ",추가하기...").split(",");
                
                ArrayAdapter<CharSequence> adapter =
                        //ArrayAdapter.createFromResource(getApplicationContext(), R.array.category1, android.R.layout.simple_spinner_item);
                        new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_spinner_item, category1List);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category1.setAdapter(adapter);
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
        
            }
        });
        
        category1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if(selected.compareTo("추가하기...") == 0) {
                    manualcategory1.setText("");
                    String[] category2List = {"추가하기..."};
                    ArrayAdapter<CharSequence> adapter =
                            new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_spinner_item, category2List);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category2.setAdapter(adapter);
                } else {
                    manualcategory1.setText(selected);
                    db.child("소분류").child(selected).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("소분류", dataSnapshot.getValue(String.class));
                            if(dataSnapshot.getValue(String.class).compareTo("null") != 0) {
                                String[] category2List = (dataSnapshot.getValue(String.class) + ",추가하기...").split(",");
                                ArrayAdapter<CharSequence> adapter =
                                        new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_spinner_item, category2List);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                category2.setAdapter(adapter);
                            } else {
                                String[] category2List = {"추가하기..."};
                                ArrayAdapter<CharSequence> adapter =
                                        new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_spinner_item, category2List);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                category2.setAdapter(adapter);
                            }
                        }
            
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                
                        }
                    });
                }
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
        
        category2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if(selected.compareTo("추가하기...") == 0) {
                    manualcategory2.setText("");
                } else {
                    manualcategory2.setText(selected);
                }
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
        
        
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String directory;
                if(category1.getSelectedItem().toString().compareTo("추가하기...") != 0) {
                    if(category2.getSelectedItem().toString().compareTo("추가하기...") != 0) {
                        directory = "분류: " + category1.getSelectedItem().toString() + " - " + category2.getSelectedItem().toString() + " - " + word.getText();
                        db.child(category1.getSelectedItem().toString()).child(category2.getSelectedItem().toString()).child(word.getText().toString()).child("의미").setValue(meaning.getText().toString());
                        db.child(category1.getSelectedItem().toString()).child(category2.getSelectedItem().toString()).child(word.getText().toString()).child("어원").setValue(from.getText().toString());
                    } else {
                        directory = "분류: " + category1.getSelectedItem().toString() + " - " + manualcategory2.getText().toString() + " - " + word.getText();
                        db.child(category1.getSelectedItem().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).child("의미").setValue(meaning.getText().toString());
                        db.child(category1.getSelectedItem().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).child("어원").setValue(from.getText().toString());
                    }
                } else {
                    directory = "분류: " + manualcategory1.getText().toString() + " - " + manualcategory2.getText().toString() + " - " + word.getText();
                    db.child(manualcategory1.getText().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).child("의미").setValue(meaning.getText().toString());
                    db.child(manualcategory1.getText().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).child("어원").setValue(from.getText().toString());
                }
                dir.setText(directory);
                word_m.setText(meaning.getText().toString());
                word_f.setText(from.getText().toString());
                word.setText("");
                meaning.setText("");
                from.setText("");
            }
        });
        
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(category1.getSelectedItem().toString().compareTo("추가하기...") != 0) {
                    if(category2.getSelectedItem().toString().compareTo("추가하기...") != 0) {
                        db.child(category1.getSelectedItem().toString()).child(category2.getSelectedItem().toString()).child(word.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    Log.d("find",dataSnapshot.toString());
                                    String directory =
                                            "분류: " + category1.getSelectedItem().toString() + " - " + category2.getSelectedItem().toString() + " - " + word.getText();
                                    dir.setText(directory);
                                    word_m.setText(dataSnapshot.child("의미").getValue(String.class));
                                    word_f.setText(dataSnapshot.child("어원").getValue(String.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "아직 등록되지 않은 단어입니다!", Toast.LENGTH_SHORT).show();
                                }
                            }
        
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
            
                            }
                        });
                    } else {
                        db.child(category1.getSelectedItem().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    Log.d("find",dataSnapshot.toString());
                                    String directory =
                                            "분류: " + category1.getSelectedItem().toString() + " - " + manualcategory2.getText().toString() + " - " + word.getText();
                                    dir.setText(directory);
                                    word_m.setText(dataSnapshot.child("의미").getValue(String.class));
                                    word_f.setText(dataSnapshot.child("어원").getValue(String.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "아직 등록되지 않은 단어입니다!", Toast.LENGTH_SHORT).show();
                                }
                            }
        
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
            
                            }
                        });
                    }
                } else {
                    db.child(manualcategory1.getText().toString()).child(manualcategory2.getText().toString()).child(word.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Log.d("find",dataSnapshot.toString());
                                String directory =
                                        "분류: " + manualcategory1.getText().toString() + " - " + manualcategory2.getText().toString() + " - " + word.getText();
                                dir.setText(directory);
                                word_m.setText(dataSnapshot.child("의미").getValue(String.class));
                                word_f.setText(dataSnapshot.child("어원").getValue(String.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "아직 등록되지 않은 단어입니다!", Toast.LENGTH_SHORT).show();
                            }
                        }
        
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
            
                        }
                    });
                }
            }
        });
    }
    
    
    
    
}
