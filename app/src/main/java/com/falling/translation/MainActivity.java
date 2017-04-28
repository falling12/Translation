package com.falling.translation;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.service.dreams.DreamService;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.util.List;

import Bean.BaiduWord;
import Bean.Word;
import util.DealMessage;
import util.MyToast;
import util.QueryCallBack;


public class MainActivity extends AppCompatActivity implements QueryCallBack {

    private static final String TAG = "MainActivity";
    private DealMessage dealMessage = new DealMessage();
    private ContentLoadingProgressBar progressBar = null;
    private ImageButton searchButton = null;
    private EditText searchWord = null;
    private TextView ph_en = null;
    private TextView ph_am = null;
    private TextView means = null;
    private TextView word = null;
    private TextView word_means = null;
    private TextView ph_am_mp3 = null;
    private TextView ph_en_mp3 = null;
    private QueryCallBack callBack = this;

    private SimpleDraweeView ph_en_gif;
    private SimpleDraweeView ph_am_gif;
    private DraweeController dcGif;
    private DraweeController dcNotGif;
    private LinearLayout voice = null;
    private LinearLayout word_content = null;
    private LinearLayout baidu_word = null;

    private TextView baiduDst = null;
    private TextView baiduWord = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dealMessage.setCallback(callBack);
        init();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSearchButton();
            }
        });

        searchWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ((InputMethodManager) searchWord.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    clickSearchButton();
                    return true;
                }
                return false;
            }
        });


    }

    private void init() {
        Typeface ph = Typeface.createFromAsset(getAssets(), "NotoSans-Italic.ttf");
        Typeface black = Typeface.createFromAsset(getAssets(), "NotoSans-Bold.ttf");

        word = (TextView) this.findViewById(R.id.word);
        word.setTypeface(black);

        searchButton = (ImageButton) this.findViewById(R.id.search_button);
        searchWord = (EditText) this.findViewById(R.id.search_text);

        baidu_word = (LinearLayout) this.findViewById(R.id.baidu_content);
        baiduDst = (TextView) this.findViewById(R.id.baidu_dst);
        baiduWord = (TextView) this.findViewById(R.id.baidu_word);
        baiduWord.setTypeface(black);

        ph_am_mp3 = (TextView) findViewById(R.id.ph_am_mp3);
        ph_en_mp3 = (TextView) findViewById(R.id.ph_en_mp3);

        ph_en = (TextView) this.findViewById(R.id.ph_en);
        ph_en.setTypeface(ph);
        ph_am = (TextView) this.findViewById(R.id.ph_am);
        ph_am.setTypeface(ph);

        means = (TextView) this.findViewById(R.id.means);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress);
        progressBar.hide();
        voice = (LinearLayout) this.findViewById(R.id.voice);
        word_content = (LinearLayout) this.findViewById(R.id.word_content);

        word_means = (TextView) this.findViewById(R.id.word_means);

        ph_am_gif = (SimpleDraweeView) this.findViewById(R.id.ph_am_gif);
        ph_en_gif = (SimpleDraweeView) this.findViewById(R.id.ph_en_gif);

        Uri uri = Uri.parse("res://com.falling.translation/" + R.drawable.voice);
        Uri notgif = Uri.parse("res://com.falling.translation/" + R.drawable.voice_not_gif);
        dcGif = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        dcNotGif = Fresco.newDraweeControllerBuilder()
                .setUri(notgif)
                .build();
        ph_am_gif.setController(dcNotGif);
        ph_en_gif.setController(dcNotGif);

        ph_am_gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph_am_gif.setController(dcGif);
                ph_am_gif.setClickable(false);
                playMp3(ph_am_mp3.getText().toString(), ph_am_gif);
            }
        });
        ph_en_gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph_en_gif.setController(dcGif);
                ph_en_gif.setClickable(false);
                playMp3(ph_en_mp3.getText().toString(), ph_en_gif);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            word_content.setVisibility(View.GONE);
            baidu_word.setVisibility(View.GONE);
            Word wWord = (Word) msg.obj;
            int tag = msg.arg1;
            if (tag == DealMessage.YOUDAO) {
                word_content.setVisibility(View.VISIBLE);
                setWord(wWord);
                setExchange(wWord);
                setPh(wWord);
                setMeans(wWord);
            }
            if (tag == DealMessage.BAIDU) {
                baidu_word.setVisibility(View.VISIBLE);
                List<BaiduWord.TransResultBean> results = wWord.getBaiduWord().getTrans_result();
                for (BaiduWord.TransResultBean transResultBean : results) {
                    baiduWord.setText(transResultBean.getSrc());
                    baiduDst.setText(transResultBean.getDst());
                }
            }

//            TODO 在这里处理UI有关操作
        }
    };

    @Override
    public void execute(Word wWord, int tag) {
        Message message = new Message();
        message.obj = wWord;
        message.arg1 = tag;
        handler.sendMessage(message);
    }

    @Override
    public void loading() {
        progressBar.show();
    }

    @Override
    public void loaded() {
        progressBar.hide();
    }

    private void clickSearchButton() {

        ph_am_gif.setController(dcNotGif);
        ph_am_gif.setClickable(true);
        ph_en_gif.setController(dcNotGif);
        ph_en_gif.setClickable(true);

        if (!searchWord.getText().toString().equals("")) {
            dealMessage.getWord(searchWord.getText().toString());
        } else {
            MyToast.showToast(MainActivity.this, "请输入要翻译的信息");
        }
    }

    private void setWord(Word wWord) {
        if (word != null) {
            if (wWord != null) {
                word.setText(wWord.getWord_name());
            } else {
                word.setText("wWord为空");
            }
        }
    }

    private void setPh(Word wWord) {
        if (wWord.getPh_am() != null & wWord.getPh_en() != null) {
            voice.setVisibility(View.VISIBLE);
            ph_en.setText("[" + wWord.getPh_en() + "]");
            ph_am.setText("[" + wWord.getPh_am() + "]");

            ph_am_mp3.setText(wWord.getPh_am_mp3());
            ph_en_mp3.setText(wWord.getPh_en_mp3());

        }
    }

    private void setExchange(Word wWord) {
        String smeans = "";
        String word_third = listToString(wWord.getWord_third());
        String word_ing = listToString(wWord.getWord_ing());
        String word_done = listToString(wWord.getWord_done());
        String word_past = listToString(wWord.getWord_past());
        String word_pl = listToString(wWord.getWord_pl());
        String word_er = listToString(wWord.getWord_er());
        String word_est = listToString(wWord.getWord_est());
        if (!word_third.equals("")) {
            smeans = smeans + "THERD:" + word_third + "\n";
        }
        if (!word_ing.equals("")) {
            smeans = smeans + "ING:" + word_ing + "\n";
        }
        if (!word_done.equals("")) {
            smeans = smeans + "DONE:" + word_done + "\n";
        }
        if (!word_past.equals("")) {
            smeans = smeans + "PAST:" + word_past + "\n";
        }
        if (!word_pl.equals("")) {
            smeans = smeans + "PL:" + word_pl + "\n";
        }
        if (!word_er.equals("")) {
            smeans = smeans + "ER:" + word_er + "\n";
        }
        if (!word_est.equals("")) {
            smeans = smeans + "EST:" + word_est + "\n";
        }
        means.setText(smeans);
    }

    private void setMeans(Word wWord) {
        if (wWord.getParts() != null) {
            List<Word.Part> parts = wWord.getParts();
            String means = "";
            for (Word.Part part : parts) {
                String wordPart = part.getPart();
                String wordMeans = listToString(part.getMeans());
                String oneLine = wordPart + " " + wordMeans;
                means = means + oneLine + "\n";
            }
            word_means.setText(means);
        }
    }

    private String listToString(List<String> stringList) {
        String done = "";
        if (stringList != null) {
            for (int i = 0; i <= stringList.size() - 2; i++) {
                done = done + stringList.get(i) + "、";
            }
            done = done + stringList.get(stringList.size() - 1) + ";";
        }
        return done;
    }

    private void playMp3(String url, final SimpleDraweeView sDV) {
        Uri uri = Uri.parse(url);
        MediaPlayer m = new MediaPlayer();
        try {
            m.setDataSource(this, uri);
            m.prepare();
            m.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                sDV.setController(dcNotGif);
                sDV.setClickable(true);
            }
        });
    }
}
