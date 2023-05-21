package com.lab_team_projects.my_walking_pet.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomBarChartView extends View {

    private Bar backGroundBar;
    private Bar contentBar;

    public CustomBarChartView(Context context) {
        super(context);
        customBarInit();
    }

    public CustomBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        customBarInit();
    }

    public CustomBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customBarInit();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        customBarInit();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backGroundBar.draw(canvas);
        contentBar.draw(canvas);
    }

    private void customBarInit(){
        backGroundBar = new Bar(Color.WHITE, 1);
        contentBar = new Bar(Color.GREEN, 0);

    }


    public void setContentBarRatio(float nowGrowth, float maxGrowth) {
        contentBar.growthRatio = nowGrowth / maxGrowth;
        invalidate(); // 바의 성장 정도가 변경되었으므로 다시 그리기 위해 invalidate() 호출
    }


    private class Bar {
        private float growthRatio;
        private final Paint paint;

        public Bar(int color, int ratio) {
            this.growthRatio = ratio;
            this.paint = new Paint();
            this.paint.setColor(color);
        }

        public void draw(Canvas canvas) {
            float width = getWidth() * 0.4f; // bar의 넓이는 커스텀 view 의 0.4배
            float height = getHeight() * 0.9f; // bar의 높이는 커스텀 view 의 0.9배

            float bottom = getHeight();
            float left = (getWidth() - width) / 2;
            float top = bottom - (int) (growthRatio * height);
            float right = left + width;

            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rectF, 20, 20, paint);
        }

    }

}
