package com.shashank.mentalhealth.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.material.slider.Slider;
import com.shashank.mentalhealth.R;

import java.util.Random;

public class MusicFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;
    private Slider volume, seekBar;
    private int length, duration;
    private TextView totalTime, currentTime;
    private Button button;
    boolean stopped;
    private BarVisualizer mVisualizer;
    Random random = new Random();
    String[] links = {"https://feelingandhealing.000webhostapp.com/anxiety1.mp3", "https://feelingandhealing.000webhostapp.com/anxiety2.mp3", "https://feelingandhealing.000webhostapp.com/anxiety3.mp3", "https://feelingandhealing.000webhostapp.com/anxiety4.mp3", "https://feelingandhealing.000webhostapp.com/anxiety5.mp3",
            "https://feelingandhealing.000webhostapp.com/depression1.mp3", "https://feelingandhealing.000webhostapp.com/depression2.mp3", "https://feelingandhealing.000webhostapp.com/depression3.mp3", "https://feelingandhealing.000webhostapp.com/depression4.mp3", "https://feelingandhealing.000webhostapp.com/depression5.mp3",
            "https://feelingandhealing.000webhostapp.com/bipolar1.mp3", "https://feelingandhealing.000webhostapp.com/bipolar2.mp3", "https://feelingandhealing.000webhostapp.com/bipolar3.mp3", "https://feelingandhealing.000webhostapp.com/bipolar4.mp3", "https://feelingandhealing.000webhostapp.com/bipolar5.mp3"};
    AudioManager.OnAudioFocusChangeListener mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause();
//                button.setBackgroundResource(R.drawable.pause96);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaResources();
                //pause the music go to Spotify play and pause song in it come back to feeling and healing and press play button,
                //because Spotify is getting complete focus so when we release MediaPlayer it set to null and than the error occurs.
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseMediaResources();
    }

    public MusicFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_music, container, false);
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Music Therapy");
        button = rootView.findViewById(R.id.play);
        Button next = rootView.findViewById(R.id.next);
        Button pre = rootView.findViewById(R.id.previous);
        currentTime = rootView.findViewById(R.id.currentTime);
        totalTime = rootView.findViewById(R.id.totalTime);
        volume = rootView.findViewById(R.id.volume);
        totalTime.setText("00:00");
        currentTime.setText("00:00");
        //get reference to visualizer
        mVisualizer = rootView.findViewById(R.id.visualizer);
        seekBar = rootView.findViewById(R.id.seekBar);
        mAudioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        volume.setValueTo(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume.setValue(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        try {
            new Thread(() -> {
                mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(getCurrentLink())); //peace music
                requireActivity().runOnUiThread(() -> {
                    if (mAudioManager != null && isNetworkAvailable()) {
                        mediaPlayer.setLooping(true);
                        duration = mediaPlayer.getDuration();
                        seekBar.setValueTo(duration / 1000f);
                        int Minutes = (int) duration / (1000 * 60);
                        int Seconds = (int) duration % ((1000 * 60 * 60)) % (1000 * 60) / 1000;
                        if (Seconds < 10) {
                            totalTime.setText(Minutes + ":0" + Seconds);
                        } else {
                            totalTime.setText(Minutes + ":" + Seconds);
                        }
                        button.setForeground(requireActivity().getDrawable(R.drawable.play96));
                        int audioSessionId = mediaPlayer.getAudioSessionId();
                        if (audioSessionId != -1) {
                            mVisualizer.setAudioSessionId(audioSessionId);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error Loading Music", Toast.LENGTH_SHORT).show();
                    }

                });
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        button.setForeground(requireActivity().getDrawable(R.drawable.loading96));

        button.setOnClickListener(v -> {
            mAudioManager.requestAudioFocus(mFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            if (button.getForeground().getConstantState().equals(getResources().getDrawable(R.drawable.play96).getConstantState()) && mediaPlayer != null) {
                button.setForeground(requireActivity().getDrawable(R.drawable.pause96));
                if (length != 0) {
                    // mediaPlayer = MediaPlayer.create(MusicActivity.this, Uri.parse(getCurrentLink())); //peace music
                    mediaPlayer.seekTo(length);
                }
                mediaPlayer.start();
                stopped = false;
            } else if (button.getForeground().getConstantState().equals(getResources().getDrawable(R.drawable.pause96).getConstantState()) && mediaPlayer != null) {
                button.setForeground(requireActivity().getDrawable(R.drawable.play96));
                mediaPlayer.pause();
                length = mediaPlayer.getCurrentPosition();
                //  releaseMediaResources();
            }
        });
        next.setOnClickListener(v -> {
            button.setForeground(requireActivity().getDrawable(R.drawable.loading96));
            new Thread(() -> {
                releaseMediaResources();
                mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(getCurrentLink())); //peace music
                requireActivity().runOnUiThread(() -> {
                    if (isNetworkAvailable()) {
                        duration = mediaPlayer.getDuration();
                        seekBar.setValueTo(duration / 1000f);
                        int Minutes = (int) duration / (1000 * 60);
                        int Seconds = (int) duration % ((1000 * 60 * 60)) % (1000 * 60) / 1000;
                        if (Seconds < 10) {
                            totalTime.setText(Minutes + ":0" + Seconds);
                        } else {
                            totalTime.setText(Minutes + ":" + Seconds);
                        }
                        int audioSessionId = mediaPlayer.getAudioSessionId();
                        if (audioSessionId != -1) {
                            mVisualizer.setAudioSessionId(audioSessionId);
                        }
                        mediaPlayer.start();
                        button.setForeground(requireActivity().getDrawable(R.drawable.pause96));
                    }
                });
            }).start();
        });
        pre.setOnClickListener(v -> {
            button.setForeground(requireActivity().getDrawable(R.drawable.loading96));
            new Thread(() -> {
                releaseMediaResources();
                mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(getCurrentLink())); //peace music
                requireActivity().runOnUiThread(() -> {
                    if (isNetworkAvailable()) {
                        duration = mediaPlayer.getDuration();
                        seekBar.setValueTo(duration / 1000f);
                        int Minutes = (int) duration / (1000 * 60);
                        int Seconds = (int) duration % ((1000 * 60 * 60)) % (1000 * 60) / 1000;
                        if (Seconds < 10) {
                            totalTime.setText(Minutes + ":0" + Seconds);
                        } else {
                            totalTime.setText(Minutes + ":" + Seconds);
                        }
                        int audioSessionId = mediaPlayer.getAudioSessionId();
                        if (audioSessionId != -1) {
                            mVisualizer.setAudioSessionId(audioSessionId);
                        }
                        mediaPlayer.start();
                        button.setForeground(requireActivity().getDrawable(R.drawable.pause96));
                    }
                });
            }).start();
        });
        try {
            Handler mHandler = new Handler(Looper.getMainLooper());
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition();
                        if (!stopped) {
                            seekBar.setValue(mCurrentPosition / 1000f);
                            volume.setValue(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                            int Minutes = mCurrentPosition / (1000 * 60);
                            int Seconds = mCurrentPosition % ((1000 * 60 * 60)) % (1000 * 60) / 1000;
                            if (Seconds < 10) {
                                currentTime.setText(Minutes + ":0" + Seconds);
                            } else {
                                currentTime.setText(Minutes + ":" + Seconds);
                            }
                        }
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        seekBar.addOnChangeListener((slider, value, fromUser) -> {
            if (mediaPlayer != null && fromUser) {
                mediaPlayer.seekTo((int) value * 1000);
                length = (int) value * 1000;
            }
        });

        volume.addOnChangeListener((slider, value, fromUser) -> mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) value,
                AudioManager.FLAG_VIBRATE));
        return rootView;
    }

    public void releaseMediaResources() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mFocusChangeListener);
        }
        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }

    public String getCurrentLink() {
        return links[random.nextInt(15)];
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && ((NetworkInfo) activeNetworkInfo).isConnected();
    }
}