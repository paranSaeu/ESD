package kr.hs.gimpo.esdhatespeechdictionary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    
    public TextView wordTitle;
    public TextView wordCategory;
    
    // ViewHolder는 레이아웃과 데이터를 연결해주는 객체입니다.
    public ViewHolder(View view) {
        super(view);
        wordTitle = (TextView) view.findViewById(R.id.textView3);
        wordCategory = (TextView) view.findViewById(R.id.textView5);
    }
    
}
