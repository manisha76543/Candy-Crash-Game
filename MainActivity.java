package com.example.candycrushgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] candies = {

            R.drawable.bluecandy,
            R.drawable.greencandy,
            R.drawable.redcandy,
            R.drawable.orangecandy,
            R.drawable.yellowcandy,
            R.drawable.purplecandy

    };

    int widthOfBlock, noOfBlocks = 8, widthOfScreen;

    ArrayList<ImageView> candy = new ArrayList<>();
    int candyToBeDragged,candToBeReplaced,notCandy= R.drawable.transparent;

    Handler mHandler = new Handler();
    int interval = 100;
    TextView scoreResult;
    int score=0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreResult = findViewById(R.id.score);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen = displayMetrics.widthPixels;
        int heightOfScreen = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen/noOfBlocks;

        createBoard();

        for(final ImageView imageView : candy)
        {
            imageView.setOnTouchListener(new OnSwipeListener(this)
            {
              /*  @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return super.onTouch(v, event);
                }*/

                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    candyToBeDragged = imageView.getId();
                    candToBeReplaced = candyToBeDragged -1;
                  candyInterchange();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();

                    candyToBeDragged = imageView.getId();
                    candToBeReplaced = candyToBeDragged +1;
                    candyInterchange();

                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();

                    candyToBeDragged = imageView.getId();
                    candToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterchange();

                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();

                    candyToBeDragged = imageView.getId();
                    candToBeReplaced = candyToBeDragged + noOfBlocks;
                    candyInterchange();

                }
            });
        }

        mHandler = new Handler();
        startRepeat();

    }

    private void checkRowForThree()
    {
        for (int i=0;i<62;i++)
        {
            int chosedCandy = (int) candy.get(i).getTag();
            boolean isBlank = (int) candy.get(i).getTag() == notCandy;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};

            List<Integer> list = Arrays.asList(notValid);
            if(!list.contains(i))
            {
                int x = i;
                if((int) candy.get(x++).getTag() == chosedCandy && !isBlank &&
                        (int) candy.get(x++).getTag() == chosedCandy &&
                        (int) candy.get(x++).getTag() == chosedCandy)
                {
                    score = score + 3;
                    scoreResult.setText(String.valueOf(score));

                     candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;


                }
            }

        }
        moveDownCandies();
    }


    private void checkColumnForThree()
    {
        for (int i=0;i<47;i++)
        {
            int chosedCandy = (int) candy.get(i).getTag();
            boolean isBlank = (int) candy.get(i).getTag() == notCandy;

                int x = i;
                if((int) candy.get(x).getTag() == chosedCandy && !isBlank &&
                        (int) candy.get(x+ noOfBlocks).getTag() == chosedCandy &&
                        (int) candy.get(x+ 2 * noOfBlocks).getTag() == chosedCandy)
                {
                    score = score + 3;
                    scoreResult.setText(String.valueOf(score));


                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);


                }

        }
        moveDownCandies();
    }


    private void moveDownCandies()
    {
        Integer[] firstRow = {0,1,2,3,4,5,6,7};
        List<Integer> list = Arrays.asList(firstRow);
        for(int i=55;i>=0;i--)
        {
            if((int) candy.get(i + noOfBlocks).getTag() == notCandy)
            {
                candy.get(i + noOfBlocks).setImageResource((int) candy.get(i).getTag());
                candy.get(i + noOfBlocks).setTag(candy.get(i).getTag());
                candy.get(i).setTag(notCandy);
                candy.get(i).setTag(notCandy);

                if(list.contains(i) && (int) candy.get(i).getTag() == notCandy)
                {
                    int randomColor = (int) Math.floor(Math.random() * candies.length);
                    candy.get(i).setTag(candies[randomColor]);
                    candy.get(i).setTag(candies[randomColor]);

                }

            }
        }

        for(int i=0;i<8;i++)
        {
            if((int) candy.get(i).getTag() == notCandy)
            {
                int randomColor = (int) Math.floor(Math.random() * candies.length);
                candy.get(i).setTag(candies[randomColor]);
                candy.get(i).setTag(candies[randomColor]);

            }
        }

    }


    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {

            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownCandies();
            }
            finally {

                mHandler.postDelayed(repeatChecker,interval);

            }

        }
    };

    void startRepeat()
    {
        repeatChecker.run();
    }

    private void candyInterchange()
    {
        int background = (int)candy.get(candToBeReplaced).getTag();
        int background1 = (int)candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candToBeReplaced).setTag(background1);



    }

    private void createBoard(){

        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().height=widthOfScreen;
        gridLayout.getLayoutParams().width=widthOfScreen;

        for(int i=0;i<noOfBlocks * noOfBlocks;i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);

            int randomCandy = (int) Math.floor(Math.random() * candies.length);

            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);
        }

    }
}