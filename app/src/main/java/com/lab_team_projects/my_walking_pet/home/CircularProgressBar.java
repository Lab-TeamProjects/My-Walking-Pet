package com.lab_team_projects.my_walking_pet.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lab_team_projects.my_walking_pet.R;

public class CircularProgressBar extends View{

    private RectF rectF;
    private RectF outlineRectF;  // 외곽선용 RectF를 생성합니다.
    private RectF innerRectF;

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint outlinePaint;
    private Paint innerOutlinePaint;

    private Path path = new Path();


    private float progress;
    private float strokeWidth = 33.0f;
    private float outlineWidth = 4.5f;
    private float innerOutlineWidth = 4.5f;

    private int progressColor;
    private int backgroundColor;

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = 360 * progress / 100;

        canvas.drawOval(rectF, backgroundPaint);
        canvas.drawArc(rectF, 270, angle, false, foregroundPaint);
        canvas.drawOval(outlineRectF, outlinePaint);
        canvas.drawOval(innerRectF, innerOutlinePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float halfStroke = strokeWidth / 2;
        float halfOutline = outlineWidth / 2;
        float halfInnerOutline = innerOutlineWidth * 4f;

        rectF = new RectF(halfStroke, halfStroke, w - halfStroke, h - halfStroke);
        outlineRectF = new RectF(halfOutline, halfOutline, w - halfOutline, h - halfOutline);
        innerRectF = new RectF(halfStroke + halfInnerOutline, halfStroke + halfInnerOutline, w - halfStroke - halfInnerOutline, h - halfStroke - halfInnerOutline);

        // set the shader for the foreground paint
        float centerX = w / 2.0f;
        float centerY = h / 2.0f;
        int startColor = progressColor;
        int endColor = Color.TRANSPARENT;  // or any other color for the gradient end
        SweepGradient gradient = new SweepGradient(centerX, centerY, new int[]{endColor, startColor}, null);

        // Rotate the gradient to start at the top
        Matrix matrix = new Matrix();
        gradient.getLocalMatrix(matrix);
        matrix.preRotate(-90, centerX, centerY); // The SweepGradient starts drawing from the right (3 o'clock) so need to rotate it by 270 degrees
        gradient.setLocalMatrix(matrix);

        // 그라데이션 추가하는 코드 일단 비활성화 둥글게 하는데 끝에 색깔이 이상해져서 일단 보류
        //foregroundPaint.setShader(gradient);

    }



    public CircularProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);

        progressColor = typedArray.getColor(R.styleable.CircularProgressBar_progressColor, 0xFF00CC00);
        backgroundColor = typedArray.getColor(R.styleable.CircularProgressBar_backgroundColor, 0xFFDDDDDD);

        typedArray.recycle();

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setColor(backgroundColor);

        foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
        foregroundPaint.setColor(progressColor);
        foregroundPaint.setStrokeCap(Paint.Cap.ROUND);  // Add this line


        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(outlineWidth);
        outlinePaint.setColor(Color.parseColor("#282828"));  // 외곽선의 색상을 정의합니다.

        innerOutlinePaint = new Paint();
        innerOutlinePaint.setAntiAlias(true);
        innerOutlinePaint.setStyle(Paint.Style.STROKE);
        innerOutlinePaint.setStrokeWidth(innerOutlineWidth);
        innerOutlinePaint.setColor(Color.parseColor("#282828"));  // 내곽선의 색상을 정의합니다.

    }


}
