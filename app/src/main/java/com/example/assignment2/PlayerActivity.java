package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private TextView tvPlayerScore;
    private EditText etPlayerName;
    private RadioGroup rgAvatar;

    private int finalScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        tvPlayerScore = findViewById(R.id.tv_playerscore);
        etPlayerName = findViewById(R.id.et_playername);
        rgAvatar = findViewById(R.id.rg_avatar);

        // 接收分数
        Intent intent = getIntent();
        finalScore = intent.getIntExtra("FINAL_SCORE", 0);

        tvPlayerScore.setText("Score: " + finalScore);
    }

    // XML: android:onClick="onClickSubmit"
    public void onClickSubmit(View view) {

        // Read the name
        String name = etPlayerName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Read the selected avatar by the user
        int checkedId = rgAvatar.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please choose an avatar colour", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Obtain the avatar resource based on the selected RadioButton
        int avatarResId;

        if (checkedId == R.id.rb_blue) {
            avatarResId = R.drawable.img_blue_mole;
        } else if (checkedId == R.id.rb_orange) {
            avatarResId = R.drawable.img_orange_mole;
        } else if (checkedId == R.id.rb_green) {
            avatarResId = R.drawable.img_green_mole;
        } else if (checkedId == R.id.rb_purple) {
            avatarResId = R.drawable.img_purple_mole;
        } else if (checkedId == R.id.rb_pink) {
            avatarResId = R.drawable.img_pink_mole;
        } else {
            avatarResId = R.drawable.img_grey_mole;
        }

        // 4. Create Player
        Player player = new Player(name, avatarResId, finalScore);

        //5. Update the leaderboard
        Leaderboard.getInstance().updateLeaderboard(player);

        //6. Jump to the ranking page
        Intent intent = new Intent(PlayerActivity.this, LeaderboardActivity.class);
        startActivity(intent);

        finish();
    }
}