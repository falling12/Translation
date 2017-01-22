package util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.falling.translation.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Bean.BaiduWord;
import Bean.Word;
import baidu.HttpGet;
import baidu.TransApi;

/**
 * Created by falling on 2016/12/15.
 */

public class DealMessage {

    public final static int BAIDU=0;
    public final static int YOUDAO=1;

    public static QueryCallBack callback;
    private JsonAnalysis ja = new JsonAnalysis();

    public void setCallback(QueryCallBack callback) {
        this.callback = callback;
    }


    public void getWord(final String word)  {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                callback.loading();
                try {
                    query(word);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    TODO show Mistake
                }catch (Exception e){
                    e.printStackTrace();
                }
                callback.loaded();
            }
        });
        t.start();
    }


    private void query(String Stringword) throws JSONException {

        if (isContainChinese(Stringword)) {
//            TODO 用百度翻译查询 注意翻译后的英文的具体信息

            callback.execute(queryByBaidu(Stringword),BAIDU);
            return;
        } else {
            callback.execute(queryByYoudao(Stringword),YOUDAO);
            return;
        }
    }

    private Word queryByBaidu(String sw) throws JSONException {
        Word word=new Word();
        JsonAnalysis ja = new JsonAnalysis();
        final String APP_ID = "20161223000034592";
        String SECURITY_KEY = "7anUiaIQe5sKKlPD9dRq";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        BaiduWord baiduWord = ja.analysisFromBAIDU(api.getTransResult(sw, "auto", "en"));
        word.setBaiduWord(baiduWord);
        return word;
    }

    private Word queryByYoudao(String sw) throws JSONException {
        Word word = new Word();

        Map<String, String> params = new HashMap<String, String>();
        params.put("w", sw.toLowerCase());
        params.put("type", "json");
        params.put("key", "58E8D0BC91963CF6B9986E3962099AE8");
        String host = "http://dict-co.iciba.com/api/dictionary.php";
        String json = HttpGet.get(host, params);
        word = ja.analysisFromYOUDAO(json);

        return word;
    }


    @NonNull
    private Boolean isContainChinese(String word) {
        for (int i = 0; i < word.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(word.charAt(i));
            if (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                    ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                    ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                    ub == Character.UnicodeBlock.GENERAL_PUNCTUATION ||
                    ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                    ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }

        }
        return false;
    }
}
