package util;

import Bean.Word;

/**
 * Created by falling on 2016/12/25.
 */

public interface QueryCallBack {

    void execute(Word word,int tag);
    void loading();
    void loaded();

}
