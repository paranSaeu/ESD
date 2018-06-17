package kr.hs.gimpo.esdhatespeechdictionary;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hs.gimpo.esdhatespeechdictionary.Fragment.QuitPromptDialogFragment;
import kr.hs.gimpo.esdhatespeechdictionary.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity
        implements QuitPromptDialogFragment.QuitResponseListener {
    
    // ActivityMainBinding은 activity_main.xml 파일에 있는 View들을 미리 준비하여 findViewById를 하지 않고도 사용할 수 있게 해주는 기능입니다.
    ActivityMainBinding mainBinding;
    // Firebase에서 데이터를 받아와 RecyclerView에 표시해 주는 기능입니다.
    //FirebaseRecyclerAdapter adapter;
    RecyclerAdapter adapter;
    
    boolean isInit = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity_main.xml 파일의 모든 View들을 bind합니다.
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD");
    
        mDatabase.child("word").addChildEventListener(new ChildEventListener() {
        
            int idx = 0;
        
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("onChildAdded", "s: "+ s + "d: " + dataSnapshot.toString());
                wordList.put(idx, dataSnapshot.getValue(Word.class));
                idx++;
                
                if(!isInit) {
                    adapter.notifyDataSetChanged();
                    
                    Word wordData = wordList.get(0);
    
                    mainBinding.word.setText(wordData.get_Word());
                    mainBinding.wordCategory.setText(wordData.get_Category());
                    mainBinding.wordEtymology.setText(wordData.get_Etymology());
                    mainBinding.wordMeaning.setText(wordData.get_Meaning());
                    isInit = true;
                }
                adapter.notifyDataSetChanged();
            }
        
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("onChildChanged", "s: "+ s + "d: " + dataSnapshot.toString());
                ArrayList<String> list = new ArrayList<>();
                
                for(int i = 0; i < wordList.size(); i++) {
                    list.add(wordList.get(i).get_Word());
                }
                
                wordList.put(list.indexOf(s) + 1, dataSnapshot.getValue(Word.class));
                adapter.notifyDataSetChanged();
            }
        
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            
            }
        
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("onChildMoved", "s: "+ s + "d: " + dataSnapshot.toString());
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
        
        // 단어 목록(RecyclerView)을 초기화합니다.
        setRecyclerView();
        
        // '검색' 버튼을 눌렀을 때 검색을 수행합니다.
        mainBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색창에 입력된 문자열을 불러옵니다.
                String searchText = mainBinding.searchBar.getText().toString();
                
                // 검색 문자열을 음절 단위로 분절합니다.
                char[] searchTextArray = searchText.toCharArray();
                
                // 각 단어가 음절 단위를 얼마나 포함하는지 검색합니다.
                for(int i = 0; i < wordList.size(); i++) {
                    for(char a : searchTextArray) {
                        wordList.get(i).isContainChar(a);
                    }
                }
                
                // 배열을 위해 List형을 사용합니다.
                List<Word> list = new ArrayList<>();
                
                // HashMap 리스트에 있는 데이터를 ArrayList 리스트로 이동합니다.
                for(int i = 0; i < wordList.size(); i++) {
                    list.add(wordList.get(i));
                }
                
                // isContain 크기대로 내림차순 배열을 시행합니다.
                Collections.sort(list, new CompareSeqDesc());
                
                // 배열된 리스트를 원래 리스트에 적용합니다.
                for(int i = 0; i < wordList.size(); i++) {
                    wordList.put(i, list.get(i));
                }
                
                // 리스트의 변경을 적용합니다.
                adapter.notifyDataSetChanged();
                
                // 검색이 종료되었으므로 각 단어의 총 포함수를 초기화합니다.
                for(int i = 0; i <wordList.size(); i++) {
                    wordList.get(i).initContain();
                }
                
                // 검색 결과 리스트의 맨 위에 표시된 결과를 화면에 띄워 줍니다.
                Word wordData = wordList.get(0);
                
                mainBinding.word.setText(wordData.get_Word());
                mainBinding.wordCategory.setText(wordData.get_Category());
                mainBinding.wordEtymology.setText(wordData.get_Etymology());
                mainBinding.wordMeaning.setText(wordData.get_Meaning());
            }
        });
    }
    
    // 뒤로가기 버튼을 누르면
    @Override
    public void onBackPressed() {
        // 종료할지 물어봅니다.
        DialogFragment dialogFragment = new QuitPromptDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "quit");
    }
    
    // '종료' 라고 답한다면
    @Override
    public void onPositiveResponse(DialogFragment dialogFragment) {
        // 어플리케이션을 종료합니다.
        finish();
    }
    
    // '취소'라고 답한다면
    @Override
    public void onNegativeResponse(DialogFragment dialogFragment) {
        // 아무것도 하지 않습니다.
    }
    
    // 단어 목록을 오프라인에서 관리합니다.
    Map<Integer, Word> wordList = new HashMap<>();
    
    // 단어 목록을 초기화합니다.
    public void setRecyclerView() {
        
        // 단어 목록 각 개체의 크기가 같도록 설정합니다.
        mainBinding.wordListView.setHasFixedSize(true);
        
        // Firebase DB와 연결합니다.
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ESD").child("word");
    
        // Firebase DB에서 단어 목록을 받아와 가나다순으로 배열합니다.
        Query query = mDatabase.orderByChild("_Word");
        
        // Firebase에서 데이터를 받아와 RecyclerAdapter를 만듭니다.
        // Word.class : 단어 목록의 각 항목에 해당하는 객체
        FirebaseRecyclerOptions<Word> options = new FirebaseRecyclerOptions
                .Builder<Word>()
                .setQuery(query, Word.class)
                .build();
        
        adapter = new RecyclerAdapter(wordList);
        
        // 단어 목록에 단어 목록 데이터를 연결합니다.
        mainBinding.wordListView.setAdapter(adapter);
        
        // 단어 목록을 세로로 스크롤할 수 있도록 설정합니다.
        mainBinding.wordListView.setLayoutManager(new LinearLayoutManager(this));
        
        // 단어 목록 중 하나의 단어를 눌렀을 때 반응합니다.
        mainBinding.wordListView.addOnItemTouchListener(new WordItemClickListener(getApplicationContext(), mainBinding.wordListView, new WordItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                // 그 단어에 해당하는 데이터를 불러옵니다.
                Word wordData = wordList.get(pos);
                mainBinding.word.setText(wordData.get_Word());
                mainBinding.wordMeaning.setText(wordData.get_Meaning());
                mainBinding.wordEtymology.setText(wordData.get_Etymology());
                mainBinding.wordCategory.setText(wordData.get_Category());
            }
        }));
    
        // 단어 목록의 각 개체마다 구분선을 삽입해 줍니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());
        mainBinding.wordListView.addItemDecoration(dividerItemDecoration);
    }
    
    class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        
        Map<Integer, Word> data;
        
        public RecyclerAdapter(Map<Integer, Word> data) {
            this.data = data;
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.wordTitle.setText(data.get(position).get_Word());
            holder.wordCategory.setText(data.get(position).get_Category());
        }
    
        @Override
        public int getItemCount() {
            return data.size();
        }
    
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.word, parent, false));
        }
    }
    
    static class CompareSeqDesc implements Comparator<Word> {
        @Override
        public int compare(Word o1, Word o2) {
            return o1.get_Contain() > o2.get_Contain() ? -1 : o1.get_Contain() < o2.get_Contain()? 1 : 0;
        }
    }
}