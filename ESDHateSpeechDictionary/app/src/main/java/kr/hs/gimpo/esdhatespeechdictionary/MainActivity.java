package kr.hs.gimpo.esdhatespeechdictionary;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import kr.hs.gimpo.esdhatespeechdictionary.Fragment.QuitPromptDialogFragment;
import kr.hs.gimpo.esdhatespeechdictionary.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity
        implements QuitPromptDialogFragment.QuitResponseListener {
    
    ActivityMainBinding mainBinding;
    FirebaseRecyclerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        setRecyclerView();
    
        
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
    
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    
    public void setRecyclerView() {
        mainBinding.wordListView.setHasFixedSize(true);
    
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD").child("word");
    
        Query query = mDatabase.orderByChild("_Word");
    
        FirebaseRecyclerOptions<Word> options = new FirebaseRecyclerOptions
                .Builder<Word>()
                .setQuery(query, Word.class)
                .build();
    
        adapter = new FirebaseRecyclerAdapter<Word, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.word, parent, false);
            
                return new ViewHolder(view);
            }
        
            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, Word model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.wordTitle.setText(model.get_Word());
                holder.wordCategory.setText(model.get_Category());
            }
        };
        
        mainBinding.wordListView.setAdapter(adapter);
        
        mainBinding.wordListView.setLayoutManager(new LinearLayoutManager(this));
    }
}