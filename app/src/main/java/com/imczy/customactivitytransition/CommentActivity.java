package com.imczy.customactivitytransition;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import com.imczy.customactivitytransition.transition.ChangeColor;
import com.imczy.customactivitytransition.transition.ChangePosition;
import com.imczy.customactivitytransition.transition.CommentEnterTransition;
import com.imczy.customactivitytransition.transition.ShareElemEnterRevealTransition;
import com.imczy.customactivitytransition.transition.ShareElemReturnChangePosition;
import com.imczy.customactivitytransition.transition.ShareElemReturnRevealTransition;

/**
 * Created by chenzhiyong on 16/6/6.
 */
public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";


    View mBottomSendBar;
    View mTitleBarTxt;
    View mCommentBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment);
        mCommentBox = findViewById(R.id.comment_box);
        mTitleBarTxt = findViewById(R.id.txt_title_bar);
        mBottomSendBar = findViewById(R.id.bottom_send_bar);

        setTransition();

    }

    private void setTransition() {
        // 顶部 title 和底部输入框的进入动画
        getWindow().setEnterTransition(new CommentEnterTransition(this, mTitleBarTxt, mBottomSendBar));

        getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
        getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());

    }

    /**
     * 分享 元素 进入动画
     * @return
     */
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ChangePosition();
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_box);
        transitionSet.addTransition(changePos);
//
        Transition revealTransition = new ShareElemEnterRevealTransition(mCommentBox);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(R.id.comment_box);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(300);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.black_85_alpha), getResources().getColor(R.color.white));
        changeColor.addTarget(R.id.comment_box);
        changeColor.setDuration(350);

        transitionSet.addTransition(changeColor);

//        transitionSet.setDuration(300);

        return transitionSet;
    }

    /**
     * 分享元素返回动画
     * @return
     */
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ShareElemReturnChangePosition();
        changePos.addTarget(R.id.comment_box);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.white), getResources().getColor(R.color.black_85_alpha));
        changeColor.addTarget(R.id.comment_box);
        transitionSet.addTransition(changeColor);


        Transition revealTransition = new ShareElemReturnRevealTransition(mCommentBox);
        revealTransition.addTarget(R.id.comment_box);
        transitionSet.addTransition(revealTransition);

        transitionSet.setDuration(300);

        return transitionSet;
    }
}