package com.knoxpo.khyati.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.valueOf;


public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    //private static final String IS_CHEATER = "cheater";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButtom;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private Boolean mButtonDisable = false;
    private int mCorrectedAnswerCount = 0;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
           // mIsCheater = savedInstanceState.getBoolean(IS_CHEATER,false);
        }
        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButtom = findViewById(R.id.true_button);
        mTrueButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mButtonDisable = true;
                disableButtons(mButtonDisable);
            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mButtonDisable = true;
                disableButtons(mButtonDisable);
            }
        });
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
                mButtonDisable = false;
                disableButtons(mButtonDisable);
            }

        });
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answer_is_true = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answer_is_true);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                String request_code_cheat = valueOf(REQUEST_CODE_CHEAT);

            }
        });
        updateQuestion();

    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (resultcode != Activity.RESULT_OK) {
            return;
        }
        if (requestcode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            Log.d("QuizActivity", "from Child" + mIsCheater);
        }

    }

    public void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

    }

    private void disableButtons(boolean disableButton) {
        if (disableButton == true) {
            mTrueButtom.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButtom.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgemental_Toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mCorrectedAnswerCount++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if (mCurrentIndex == mQuestionBank.length - 1) {
            int totalPercentage = (mCorrectedAnswerCount * 100) / mQuestionBank.length;
            String percentageString = valueOf(totalPercentage);

            Log.d(TAG, "Percentage: " + percentageString);

            Toast.makeText(this, percentageString, Toast.LENGTH_SHORT).show();
            mCorrectedAnswerCount = 0;
        }

    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG, "onSaveInstanceState() ");
        saveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
