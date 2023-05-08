package com.lab_team_projects.my_walking_pet.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomBarChartView extends View {

    /*
    * 성장치 그래프 커스텀 뷰
    * */

    private final Bar bgBar = new Bar(0.4f, 0.8f, 0.0f, Color.WHITE);
    private final Bar contentBar = new Bar(0.4f, 0.8f, 0.0f, Color.GREEN);

    /**
     *
     * @param s : only '+' or '-'
     */
    public void setBarLength(String s) {
        if (s.equals("+")) {
            contentBar.increaseLength(1);
        } else if (s.equals("-")) {
            contentBar.decreaseLength(970);
        }
        invalidate();
    }

    public void setContentBar(float nowGrowth, float maxGrowth) {
        contentBar.setAmount(nowGrowth, maxGrowth);
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

    private class Bar {
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
        float amount;
        Canvas canvas;

        public Bar(float inWidth, float inHeight, float amount, int color) {
            this.inWidth = inWidth;
            this.inHeight = inHeight;
            this.amount = amount;
            this.color = color;
        }

        public void init() {
            this.paint = new Paint();
            this.paint.setColor(this.color);
            this.rect = new RectF();
        }

        public void draw(Canvas canvas) {
            this.canvas = canvas;
            this.width = getWidth();
            this.height = getHeight();
            this.barHeight = height * inHeight;
            this.barWidth = width * inWidth;
            this.barX = (width - barWidth) / 2;
            this.barY = (height - barHeight) / 2;
            this.rect.set(barX, barY + this.amount, barX + barWidth, barY + barHeight);
            this.canvas.drawRoundRect(rect, 20, 20, paint);
        }

        public void setAmount(float nowGrowth, float maxGrowth) {
            this.amount = (nowGrowth / maxGrowth) * this.barHeight;
            this.rect.set(barX, barY + this.amount, barX + barWidth, barY + barHeight);
        }

        public void increaseLength(float amount) {
            this.amount -= amount;
            this.rect.set(barX, barY + this.amount, barX + barWidth, barY + barHeight);
        }

        public void decreaseLength(float amount) {
            this.amount += amount;
            this.rect.set(barX, barY + this.amount, barX + barWidth, barY + barHeight);
        }
    }
}
