package com.lab_team_projects.my_walking_pet.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CustomBarChartView extends View {

    private Bar bgBar = new Bar(0.4f, 0.8f, 0, Color.WHITE);
    private Bar contentBar = new Bar(0.4f, 0.8f, 0.4f, Color.LTGRAY);

    public void setBarLength(String s) {
        if (s.equals("+")) {
            contentBar.readjust(0.1f, true);
        } else if (s.equals("-")) {
            contentBar.readjust(0.1f, false);
        }
        invalidate();
    }

    public CustomBarChartView(Context context) {
        super(context);
        bgBar.init();
        contentBar.init();
    }

    public CustomBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgBar.init();
        contentBar.init();
    }

    public CustomBarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgBar.init();
        contentBar.init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgBar.draw(canvas);
        contentBar.draw(canvas);
    }

    class Bar {
        Paint paint;
        RectF rect;
        float width;
        float height;
        float barHeight;
        float barWidth;
        float barX;
        float barY;
        int color;
        float inWidth, inHeight;
        float ratio;

        public Bar(float inWidth, float inHeight, float ratio, int color) {
            this.inWidth = inWidth;
            this.inHeight = inHeight;
            this.ratio = ratio;
            this.color = color;
        }

        public void init() {
            this.paint = new Paint();
            this.paint.setColor(this.color);
            this.rect = new RectF();
        }

        public void draw(Canvas canvas) {
            // 커스텀 막대 그리기
            width = getWidth();
            height = getHeight();
            barHeight = height * inHeight;
            barWidth = width * inWidth;
            barX = (width - barWidth) / 2;
            barY = (height - barHeight) / 2;
            ratio = height * ratio;
            rect.set(barX, barY + ratio, barX + barWidth, barY + barHeight);
            canvas.drawRoundRect(rect, 20, 20, paint);
        }

        public void readjust(float ratio, boolean isAdd) {
            ratio = height * ratio;

            if (isAdd) {
                this.ratio -= ratio;
            } else {
                this.ratio += ratio;
            }
            rect.set(barX, barY + this.ratio, barX + barWidth, barY + barHeight);
        }

        public float getRatio() {
            return ratio;
        }
    }
}
