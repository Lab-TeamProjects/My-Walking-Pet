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

/**
 * 원형 프로그래스바 커스텀 뷰를 생성할 수 있는 클래스입니다.
 * 자바 스윙을 기반으로 제작되었으며 안드로이드 XML에서 사용가능합니다.
 */
public class CircularProgressBar extends View{

    /**
     * 프로그레스바의 몸통 리엑트
     */
    private RectF rectF;
    /**
     * 프로그레스바 외곽선 리엑트
     */
    private RectF outlineRectF;  // 외곽선용 RectF를 생성합니다.
    /**
     * 프로그레스바의 내곽선 리엑트
     */
    private RectF innerRectF;

    /**
     * 프로그레스바 배경
     */
    private Paint backgroundPaint;/**
     * 프로그레스바 내용
     */
    private Paint foregroundPaint;/**
     * 프로그레스바 외곽선
     */
    private Paint outlinePaint;/**
     * 프로그레스바 내곽선
     */
    private Paint innerOutlinePaint;

    /**
     * 프로그레스바 수치
     */
    private float progress;
    /**
     * 프로그레스바 두께
     */
    private float strokeWidth = 34.0f;/**
     * 외곽선 두께
     */
    private final float outlineWidth = 4.8f;/**
     * 내곽선 두께
     */
    private final float innerOutlineWidth = 4.8f;
    /**
     * 프로그레스바 색상
     */
    private int progressColor;
    /**
     * 프로그레스바 배경 색상
     */
    private int backgroundColor;

    /**
     * 프로그레스바 수치를 설정하고 invalidate() 메서드를 호출합니다.
     *
     * @param progress the progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 각각의 RectF와 Pint를 화면에 그립니다.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = 360 * progress / 100;

        canvas.drawOval(rectF, backgroundPaint);
        canvas.drawArc(rectF, 270, angle, false, foregroundPaint);
        canvas.drawOval(outlineRectF, outlinePaint);
        canvas.drawOval(innerRectF, innerOutlinePaint);
    }

    /**
     * xml에서 width, height로 설정했던 크기에 맞춰서 실제 크기도 변경합니다.
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float halfStroke = strokeWidth / 2.0f + 2;
        float halfOutline = outlineWidth / 2.0f + 2;

        rectF = new RectF(halfStroke, halfStroke, w - halfStroke, h - halfStroke);
        outlineRectF = new RectF(halfOutline, halfOutline, w - halfOutline, h - halfOutline);

        float outlineGap = (strokeWidth + innerOutlineWidth);
        innerRectF = new RectF(outlineGap, outlineGap, w - outlineGap, h - outlineGap);


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


    /**
     * 원형 프로그래스바 생성자
     *
     * @param context 안드로이드 context
     */
    public CircularProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Circular progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Circular progress bar.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Instantiates a new Circular progress bar.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 프로그래스바를 스윙으로 만듭니다.
     * @param context
     * @param attrs xml에서 설정한 width, hegiht를 배열로 가져옵니다.
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);

        progressColor = typedArray.getColor(R.styleable.CircularProgressBar_progressColor, 0xFF00CC00);
        backgroundColor = typedArray.getColor(R.styleable.CircularProgressBar_backgroundColor, 0xFFDDDDDD);

        float strokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_strokeWidth, 34.0f);
        if (strokeWidth != 34.0f) {
            this.strokeWidth = strokeWidth;
        }

        typedArray.recycle();

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(this.strokeWidth);
        backgroundPaint.setColor(this.backgroundColor);

        foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(this.strokeWidth);
        foregroundPaint.setColor(this.progressColor);
        foregroundPaint.setStrokeCap(Paint.Cap.ROUND);  // Add this line


        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(this.outlineWidth);
        outlinePaint.setColor(Color.parseColor("#282828"));  // 외곽선의 색상을 정의합니다.

        innerOutlinePaint = new Paint();
        innerOutlinePaint.setAntiAlias(true);
        innerOutlinePaint.setStyle(Paint.Style.STROKE);
        innerOutlinePaint.setStrokeWidth(this.innerOutlineWidth);
        innerOutlinePaint.setColor(Color.parseColor("#282828"));  // 내곽선의 색상을 정의합니다.

    }


}
