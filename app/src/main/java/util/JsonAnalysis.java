package util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Bean.BaiduWord;
import Bean.Word;

/**
 * Created by falling on 2016/12/15.
 */

public class JsonAnalysis {
    private final String TAG = "JsonAnalysis";


    public Word analysisFromYOUDAO(String json) throws JSONException,NullPointerException {

        Word word = new Word();

        JSONObject jsonObject;
        JSONObject exchange;
        JSONArray symbols;
        JSONObject symbol;
        JSONArray parts;

        JSONArray word_pl;
        JSONArray word_past;
        JSONArray word_done;
        JSONArray word_ing;
        JSONArray word_third;
        JSONArray word_er;
        JSONArray word_est;

        //暂时不知道这个有什么用
        JSONArray items;

        jsonObject = new JSONObject(json);

        exchange = jsonObject.getJSONObject("exchange");
        symbols = jsonObject.getJSONArray("symbols");
        symbol = symbols.getJSONObject(0);
        parts = symbol.getJSONArray("parts");

        word.setWord_name(jsonObject.getString("word_name"));
        word.setIs_CRI(jsonObject.getInt("is_CRI"));

        if (!exchange.get("word_pl").equals("")) {
            word_pl = exchange.getJSONArray("word_pl");
            word.setWord_pl(traversalJSONArray(word_pl));
        }
        if (!exchange.get("word_past").equals("")) {
            word_past = exchange.getJSONArray("word_past");
            word.setWord_past(traversalJSONArray(word_past));
        }
        if (!exchange.get("word_done").equals("")) {
            word_done = exchange.getJSONArray("word_done");
            word.setWord_done(traversalJSONArray(word_done));
        }
        if (!exchange.get("word_ing").equals("")) {
            word_ing = exchange.getJSONArray("word_ing");
            word.setWord_ing(traversalJSONArray(word_ing));
        }
        if (!exchange.get("word_third").equals("")) {
            word_third = exchange.getJSONArray("word_third");
            word.setWord_third(traversalJSONArray(word_third));
        }

        if (!exchange.get("word_er").equals("")) {
            word_er = exchange.getJSONArray("word_er");
            word.setWord_er(traversalJSONArray(word_er));
        }
        if (!exchange.get("word_est").equals("")) {
            word_est = exchange.getJSONArray("word_est");
            word.setWord_est(traversalJSONArray(word_est));
        }


        word.setPh_en(symbol.getString("ph_en"));
        word.setPh_am(symbol.getString("ph_am"));
        word.setPh_other(symbol.getString("ph_other"));
        word.setPh_am_mp3(symbol.getString("ph_am_mp3"));
        word.setPh_en_mp3(symbol.getString("ph_en_mp3"));
        word.setPh_tts_mp3(symbol.getString("ph_tts_mp3"));

        List<Word.Part> tempListPart = new ArrayList<>();
        for (int i = 0; i < parts.length(); i++) {
            Word.Part part = word.new Part();
            JSONObject tempPart = (JSONObject) parts.get(i);
            JSONArray tempMeans = tempPart.getJSONArray("means");
            part.setPart(tempPart.getString("part"));
            part.setMeans(traversalJSONArray(tempMeans));
            tempListPart.add(part);
        }
        word.setParts(tempListPart);

        return word;
    }

    public BaiduWord analysisFromBAIDU(String json)throws JSONException,NullPointerException{
        BaiduWord baiduWord=new BaiduWord();

        JSONObject jsonObject=new JSONObject(json);
        JSONArray jsonTrans_result=jsonObject.getJSONArray("trans_result");

        List<BaiduWord.TransResultBean> trans_result=new ArrayList<>();
        for (int i = 0; i < jsonTrans_result.length(); i++) {
            BaiduWord.TransResultBean trb= new BaiduWord.TransResultBean();
            JSONObject j=jsonTrans_result.getJSONObject(i);
            trb.setDst(j.getString("dst"));
            trb.setSrc(j.getString("src"));
            trans_result.add(trb);
        }

        baiduWord.setFrom(jsonObject.getString("from"));
        baiduWord.setTo(jsonObject.getString("to"));
        baiduWord.setTrans_result(trans_result);

        return baiduWord;
    }

    private List<String> traversalJSONArray(JSONArray jsonArray) throws JSONException {
        List<String> date = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            date.add(jsonArray.getString(i));
        }
        return date;
    }
}
