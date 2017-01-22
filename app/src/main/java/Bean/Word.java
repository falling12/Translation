package Bean;

import java.util.List;


/**
 * Created by falling on 2016/12/15.
 * JSON 字段解释(英文)
 * {
 * 'word_name':'' #单词
 * 'exchange': '' #单词的各种时态
 * 'symbols':'' #单词各种信息 下面字段都是这个字段下面的
 * 'ph_en': '' #英式音标
 * 'ph_am': '' #美式音标
 * 'ph_en_mp3':'' #英式发音
 * 'ph_am_mp3': '' #美式发音
 * 'ph_tts_mp3': '' #TTS发音
 * 'parts':'' #词的各种意思
 * }
 */

public class Word {

    /**
     * 这个是百度专有的
     */
    private BaiduWord baiduWord = null;

    public BaiduWord getBaiduWord() {
        return baiduWord;
    }

    public void setBaiduWord(BaiduWord baiduWord) {
        this.baiduWord = baiduWord;
    }

    /**
     * 这个是有道专有的
     */
    private String word_name;
    private int is_CRI;

    //    'exchange': '' #单词的各种时态
    private List<String> word_pl;
    private List<String> word_past;
    private List<String> word_done;
    private List<String> word_ing;
    private List<String> word_third;
    private List<String> word_er;
    private List<String> word_est;

//    'symbols':'' #单词各种信息 下面字段都是这个字段下面的

    private String ph_en;
    private String ph_am;
    private String ph_other;
    private String ph_en_mp3;
    private String ph_am_mp3;
    private String ph_tts_mp3;
    private List<Part> parts;


    private List<String> items;

    public int getIs_CRI() {
        return is_CRI;
    }

    public void setIs_CRI(int is_CRI) {
        this.is_CRI = is_CRI;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getPh_am() {
        return ph_am;
    }

    public void setPh_am(String ph_am) {
        this.ph_am = ph_am;
    }

    public String getPh_am_mp3() {
        return ph_am_mp3;
    }

    public void setPh_am_mp3(String ph_am_mp3) {
        this.ph_am_mp3 = ph_am_mp3;
    }

    public String getPh_en() {
        return ph_en;
    }

    public void setPh_en(String ph_en) {
        this.ph_en = ph_en;
    }

    public String getPh_en_mp3() {
        return ph_en_mp3;
    }

    public void setPh_en_mp3(String ph_en_mp3) {
        this.ph_en_mp3 = ph_en_mp3;
    }

    public String getPh_other() {
        return ph_other;
    }

    public void setPh_other(String ph_other) {
        this.ph_other = ph_other;
    }

    public String getPh_tts_mp3() {
        return ph_tts_mp3;
    }

    public void setPh_tts_mp3(String ph_tts_mp3) {
        this.ph_tts_mp3 = ph_tts_mp3;
    }

    public List<String> getWord_done() {
        return word_done;
    }

    public void setWord_done(List<String> word_done) {
        this.word_done = word_done;
    }

    public List<String> getWord_er() {
        return word_er;
    }

    public void setWord_er(List<String> word_er) {
        this.word_er = word_er;
    }

    public List<String> getWord_est() {
        return word_est;
    }

    public void setWord_est(List<String> word_est) {
        this.word_est = word_est;
    }

    public List<String> getWord_ing() {
        return word_ing;
    }

    public void setWord_ing(List<String> word_ing) {
        this.word_ing = word_ing;
    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public List<String> getWord_past() {
        return word_past;
    }

    public void setWord_past(List<String> word_past) {
        this.word_past = word_past;
    }

    public List<String> getWord_pl() {
        return word_pl;
    }

    public void setWord_pl(List<String> word_pl) {
        this.word_pl = word_pl;
    }

    public List<String> getWord_third() {
        return word_third;
    }

    public void setWord_third(List<String> word_third) {
        this.word_third = word_third;
    }


    public class Part {
        private String part;
        private List<String> means;

        public List<String> getMeans() {
            return means;
        }

        public void setMeans(List<String> means) {
            this.means = means;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

    }
}
