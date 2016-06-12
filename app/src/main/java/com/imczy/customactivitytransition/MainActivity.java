package com.imczy.customactivitytransition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    View mCommentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommentImg = findViewById(R.id.comment_img);

        mCommentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                transitionTo(intent);
            }
        });
    }


    void transitionTo(Intent i) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, new Pair<>(mCommentImg, "comment"));
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
