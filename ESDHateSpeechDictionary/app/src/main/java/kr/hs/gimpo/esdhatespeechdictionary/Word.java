package kr.hs.gimpo.esdhatespeechdictionary;

public class Word {
    
    // Firebase DB에 적용하기 위해서 빈 생성자를 설정합니다.
    public Word() {
    
    }
    
    // 한 단어가 포함하는 데이터를 명시합니다. 단어, 의미, 어원, 분류의 4가지입니다.
    public String _Word, _Meaning, _Etymology, _Category;
    
    // Firebase DB에는 올리지 않는 검색용 임시 변수입니다. 검색어의 음절이 몇 개 포함되어 있는지 임시로 저장하게 됩니다. 오프라인에서만 사용하므로 접근 권한을 private으로 설정하여 Firebase DB가 사용하지 않도록 설정합니다.
    private int isContain = 0;
    
    // 단어를 생성할 때 사용합니다. ESDWordRegister에서 사용되는 함수입니다.
    public Word(String _word, String _meaning, String _etymology, String _category) {
        this._Word = _word;
        this._Meaning = _meaning;
        this._Etymology = _etymology;
        this._Category = _category;
        this.initContain();
    }
    
    // 각각 단어, 의미, 어원, 분류를 받아옵니다.
    public String get_Word() {
        return this._Word;
    }
    
    public String get_Meaning() {
        return this._Meaning;
    }
    
    public String get_Etymology() {
        return this._Etymology;
    }
    
    public String get_Category() {
        return this._Category;
    }
    
    // System.out.println() 등에 넘겨졌을 때 String 형으로 변형해 줍니다.
    public String toString() {
        return this._Word;
    }
    
    // 검색 중에 사용되는 함수입니다. 각 문자를 포함하는지 검색합니다.
    public void isContainChar(char a) {
        if(this._Word.contains(String.valueOf(a))) {
            isContain++;
        }
    }
    
    // 검색 중에 사용되는 함수입니다. 검색어를 몇 번 포함했는지 검사합니다. 즉 총 포함수를 검사합니다.
    public int get_Contain() {
        return this.isContain;
    }
    
    // 검색이 끝난 다음 임시 변수를 0으로 리셋합니다.
    public void initContain() {
        this.isContain = 0;
    }
}
