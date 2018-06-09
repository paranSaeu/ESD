package kr.hs.gimpo.esdhatespeechdictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
    
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD");
        
        mDatabase.child("test").setValue("Hello, World!");
        
        mDatabase.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.textView);
                
                textView.setText(dataSnapshot.getValue(String.class));
            }
    
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        });
        
    }
}