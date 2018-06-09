package kr.hs.gimpo.esdhatespeechdictionary;

public class Word {
    
    public Word() {
    
    }
    
    public String _Word, _Meaning, _Etymology, _Category;
    
    public Word(String _word, String _meaning, String _etymology, String _category) {
        this._Word = _word;
        this._Meaning = _meaning;
        this._Etymology = _etymology;
        this._Category = _category;
    }
    
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
    
    public String toString() {
        return this._Word;
    }
}
