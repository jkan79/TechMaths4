package com.level4.technicalmathematics;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.View;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    private FrameLayout overlayLayout;
    private SoundPool soundPool;
    private int beepSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🚫 Disable screenshots and screen recordings
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        // 🛡️ Create overlay layout to appear when recording is detected
        overlayLayout = new FrameLayout(this);
        overlayLayout.setBackground(new ColorDrawable(Color.parseColor("#B3000000"))); // translucent black

        // Add app logo
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.mipmap.ic_launcher);
        logo.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        // Add warning text
        TextView warningText = new TextView(this);
        warningText.setText("⚠️ Screen recording is not allowed");
        warningText.setTextColor(Color.WHITE);
        warningText.setTextSize(18);
        warningText.setGravity(Gravity.CENTER);

        // Layout for logo and text
        FrameLayout innerLayout = new FrameLayout(this);
        innerLayout.setPadding(0, 0, 0, 100);
        innerLayout.addView(logo);

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        innerLayout.addView(warningText, textParams);

        // Add the inner layout to the overlay
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        overlayLayout.addView(innerLayout, params);

        overlayLayout.setVisibility(View.GONE);

        // Add overlay to main view
        addContentView(overlayLayout, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // 🔊 Load warning beep sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load beep from res/raw/beep.mp3
        beepSound = soundPool.load(this, R.raw.beep, 1); // make sure you have res/raw/beep.mp3
    }

    @Override
    public void onPause() {
        super.onPause();
        // Show overlay and play beep when recording or backgrounding
        overlayLayout.setVisibility(View.VISIBLE);
        soundPool.play(beepSound, 1, 1, 0, 0, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide overlay
        overlayLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
