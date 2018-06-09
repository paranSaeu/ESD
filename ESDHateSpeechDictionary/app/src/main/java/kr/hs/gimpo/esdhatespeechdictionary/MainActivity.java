package kr.hs.gimpo.esdhatespeechdictionary;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import kr.hs.gimpo.esdhatespeechdictionary.Fragment.QuitPromptDialogFragment;


public class MainActivity extends AppCompatActivity
        implements QuitPromptDialogFragment.QuitResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD").child("word");
        
        Query query = mDatabase.orderByChild("_Word");
        
        FirebaseListOptions<Word> option = new FirebaseListOptions
                .Builder<Word>()
                .setLayout(android.R.layout.simple_list_item_1)
                .setQuery(query, Word.class)
                .build();
    
        FirebaseListAdapter<Word> adapter = new FirebaseListAdapter<Word>(option) {
            @Override
            protected void populateView(View v, Word model, int position) {
                TextView textView = v.findViewById(android.R.id.text1);
                Log.d("populateView", model.get_Word());
                textView.setText(model.get_Word());
            }
        };
    
        ListView wordList = findViewById(R.id.wordList);
        wordList.setAdapter(adapter);
        Log.d("wordList", "\n==========\ninit completed.\n==========\n");
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