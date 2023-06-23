package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 사용자가 아이템을 사용할 때 텍스트를 드래그하여 사용할 수 있도록 터치 리스너를 구현하는 클래스
 * 사용자는 텍스트를 클릭하고 위로 드래그하면 아이템을 사용할 수 있습니다.
 */
public class OnSwipeTouchHelper implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchHelper(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        /*
        * 해당 뷰를 클릭하고 이동한 값을 계산하여
        * 위로 드래그 했는지 혹은 아래, 왼쪽, 오른쪽 등등
        * 각각 다른 함수를 설정할 수 있고
        * 사용 방법은 클래스를 선언하고 익명함수로 재정의하면 된다
        * */

        private static final int SWIPE_THRESHOLD = 1;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * 사용자가 터치한 좌표를 계산하여 방향을 판단하고 결과를 반환합니다.
         * @return 지정한 방향대로 드래그 했는지 판단한 결과를 반환
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}