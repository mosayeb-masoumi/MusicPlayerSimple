package com.example.tornado.musicplayersimple;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar,volumeBar;
    TextView elapsedTimeLable,remainingTimeLable;
    MediaPlayer mp;
    int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn=findViewById(R.id.playBtn);
        elapsedTimeLable=findViewById(R.id.elapsedTimeLable);
        remainingTimeLable=findViewById(R.id.remaningTimeLable);

        //mediaPlayer
        mp=MediaPlayer.create(this,R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime=mp.getDuration();


        //Position Bar
        positionBar=findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //Volume Bar
        volumeBar=findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volumeNum = progress / 100f ;
                mp.setVolume(volumeNum,volumeNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //Thread (update positionBar & time lable)
       new Thread(new Runnable() {
           @Override
           public void run() {

               while (mp !=null){
                   try{
                       Message msg=new Message();
                       msg.what=mp.getCurrentPosition();
                       handler.sendMessage(msg);
                       Thread.sleep(1000);
                   }catch (InterruptedException e){}
               }

           }
       }).start();

    }

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //update position bar
            positionBar.setProgress(currentPosition);

            //update lable
            String elapsedTime = createTimeLable(currentPosition);
            elapsedTimeLable.setText(elapsedTime);

            String remainingTime = createTimeLable(totalTime-currentPosition);
            remainingTimeLable.setText("- "+remainingTime);
        }
    };

    public String createTimeLable(int time){
        String timeLable = "";
        int min =time/1000/60;
        int sec = time/ 1000 % 60 ;

        timeLable = min +  ":";
        if(sec<10)timeLable +="0";
        timeLable += sec;
         return timeLable;
    }


    public void playBtnClick(View view) {

        if(!mp.isPlaying()){
            //stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause);
        }else {
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }
}
