package com.lab_team_projects.my_walking_pet.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 동물 설장치 바차트 클래스 시각적으로 볼 수 있도록 자바 스윙으로 구현되었습니다.
 */
public class CustomBarChartView extends View {

    /**
     * 바차트 배경 클래스
     */
    private Bar backGroundBar;
    /**
     * 바차트 내용 클래스
     */
    private Bar contentBar;
    /**
     * 바차트의 눈금 클래스
     */
    private DashedOverlay dashedOverlay;

    /**
     * 커스텀 바차트 뷰 생성자
     * @param context 안드로이드 context
     */
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

    /**
     * 화면 크기에 맞춰서 바차트의 크기도 변경됩니다.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        customBarInit();
    }

    /**
     * 화면에 바차트를 표시합니다.
     * @param canvas 자바 스윙 canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backGroundBar.draw(canvas);

        contentBar.draw(canvas);

        float width = getWidth() * 0.4f - 30.0f;
        float height = getHeight() * 0.9f;
        float bottom = getHeight();
        float left = (getWidth() - width) / 2;
        float dashTop = bottom - (int) (height);
        float right = left + width;
        dashedOverlay.draw(canvas, left, dashTop, right, bottom, 5f);


    }

    /**
     * 바차트 클래스를 자바 스윙으로 구현하여 설정합니다.
     */
    private void customBarInit() {
        backGroundBar = new Bar(Color.parseColor("#FAEBD7"), Color.parseColor("#282828")
                , 1, true, 5f, 20f);

        contentBar = new Bar(Color.parseColor("#FFCB9C"), Color.parseColor("#282828")
                , 0, true, 5f, 11f);

        dashedOverlay = new DashedOverlay(Color.parseColor("#000000"));
    }

    /**
     * 동물 성장치가 변경되면 바차트의 내용도 변경됩니다.
     * 퍼센트 비율로 바차트가 상승합니다.
     * @param nowGrowth 동물의 현재 성장치
     * @param maxGrowth 동물의 현재 레벨의 최대 성장치
     */
    public void setContentBarRatio(float nowGrowth, float maxGrowth) {
        contentBar.growthRatio = nowGrowth / maxGrowth;
        invalidate(); // 바의 성장 정도가 변경되었으므로 다시 그리기 위해 invalidate() 호출
    }


    /**
     * 커스텀 뷰에 구현되는 바차트 클래스
     */
    private class Bar {
        private float growthRatio;
        private final Paint fillPaint;
        private final Paint strokePaint;
        private final boolean drawBorder;
        private final float strokeWidth;
        private final float cornerRadius;

        public Bar(int fillColor, int strokeColor, float ratio, boolean drawBorder, float strokeWidth, float cornerRadius) {
            this.growthRatio = ratio;
            this.drawBorder = drawBorder;
            this.strokeWidth = strokeWidth;
            this.cornerRadius = cornerRadius;

            this.fillPaint = new Paint();
            this.fillPaint.setColor(fillColor);

            this.strokePaint = new Paint();
            this.strokePaint.setColor(strokeColor);
            this.strokePaint.setStyle(Paint.Style.STROKE);
            this.strokePaint.setStrokeWidth(strokeWidth);
        }

        public void draw(Canvas canvas) {
            float width = getWidth() * 0.4f - 0.2f;
            float height = getHeight() * 0.9f;

            float bottom = getHeight() - strokeWidth / 2;
            float left = (getWidth() - width) / 2;
            float top = bottom - (int) (growthRatio * height);
            float right = left + width;

            if (growthRatio != 0) {
                RectF rectF = new RectF(left, top, right, bottom);

                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);

                if (drawBorder) {
                    canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
                }
            }
        }

    }

    /**
     * 빗금을 구현한 클래스
     */
    private static class DashedOverlay {
        private final Paint dashedPaint;

        public DashedOverlay(int color) {
            this.dashedPaint = new Paint();
            this.dashedPaint.setColor(color);
            this.dashedPaint.setStyle(Paint.Style.STROKE);
            this.dashedPaint.setStrokeWidth(3);
        }

        public void draw(Canvas canvas, float left, float top, float right, float bottom, float cornerRadius) {
            Path path = new Path();

            float dashHeight = (bottom - top) * 0.1f;

            for (float y = top + dashHeight; y <= bottom; y += dashHeight) {
                path.moveTo(left, y);
                path.lineTo(right, y);
                canvas.drawPath(path, dashedPaint);
                path.reset();
            }
        }
    }

}
