package ytx.app.View;

/**
 * Created by vi爱 on 2018/5/3.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RangeProgressBar extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    float firstValue = 50;
    float maxValue = 100;

    float secondValue = 75;
    boolean isTwoProgress = true;


    int progressColor = Color.RED;
    int bgColor = Color.BLUE;
    int controlColor = Color.BLACK;

    int controlRadius;

    int leftRightSpace;

    int topBottomSpace;

    public RangeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float len;

    private void initViews() {
        dip5 = dip2px(getContext(), 5);
        leftRightSpace = dip5 * 3;
        topBottomSpace = dip5*2;
        len = getWidth() - 2 * leftRightSpace;
        mPaint.setStyle(Paint.Style.FILL);
        controlRadius = dip5 * 3;

    }

    public void exchangeProgressBar() {
        isTwoProgress = !isTwoProgress;
        invalidate();
    }

    RectF rectf = new RectF();

    private float getRate(float value) {
        return value / maxValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) {
            return;
        }
        //画背景
        mPaint.setColor(bgColor);//
        rectf.left = leftRightSpace;
        rectf.top = topBottomSpace;
        rectf.right = len + leftRightSpace;
        rectf.bottom = getHeight() -  topBottomSpace;
        canvas.drawRoundRect(rectf, dip5, dip5, mPaint);

        //画进度条
        //单进度条
        if (isTwoProgress == false) {
            fistPointX = getValueLen(firstValue);
            rectf.right = fistPointX;
            mPaint.setColor(progressColor);
            canvas.drawRoundRect(rectf, dip5, dip5, mPaint);
            mPaint.setColor(controlColor);
            canvas.drawCircle(fistPointX, getHeight() / 2f, controlRadius, mPaint);
        } else {//双进度条

            fistPointX = getValueLen(firstValue);
            secondPointX = getValueLen(secondValue);
            rectf.left = fistPointX;
            rectf.right = secondPointX;

            mPaint.setColor(progressColor);
            canvas.drawRoundRect(rectf, dip5, dip5, mPaint);

            mPaint.setColor(controlColor);
            canvas.drawCircle(fistPointX, getHeight() / 2, controlRadius, mPaint);
            canvas.drawCircle(secondPointX, getHeight() / 2, controlRadius, mPaint);

        }
    }

    private float getValueLen(float value) {
        float rate = getRate(value);
        float positionX = leftRightSpace + len * rate;
        return positionX;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    int dip5;

    float fistPointX, secondPointX;


    private boolean isInArea(float lastPosition, float x) {
        return x < lastPosition + controlRadius && lastPosition - controlRadius < x;
    }

    boolean isMovedFirst = false;
    boolean isMovedSecond = false;

    public void setFirstValue(float firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(float secondValue) {
        this.secondValue = secondValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInArea(fistPointX, event.getX())) {
                    isMovedFirst = true;
                }
                if (isInArea(secondPointX, event.getX())) {
                    isMovedSecond = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float newValue = maxValue * x2Rate(event.getX());
                if (isMovedFirst) {
                    if (isTwoProgress) {
                        if (newValue + 10 < secondValue) {
                            if (ondataChanged != null) {
                                ondataChanged.onFirstDataChange(newValue);
                            }
                            setFirstValue(newValue);
                            invalidate();
                        }
                    } else {
                        if (ondataChanged != null) {
                            ondataChanged.onFirstDataChange(newValue);
                        }
                        setFirstValue(newValue);
                        invalidate();
                    }
                } else if (isMovedSecond) {
                    if (newValue > firstValue + 10) {
                        if (ondataChanged != null) {
                            ondataChanged.onSecondDataChange(newValue);
                        }
                        setSecondValue(newValue);
                        invalidate();
                    }
                }
                break;
            default:
                isMovedSecond = false;
                isMovedFirst = false;
                break;
        }
        return true;
    }

    private float x2Rate(float x) {
        float f = (x - leftRightSpace) / len;
        if (f < 0) {
            f = 0;
        }
        if (f > 1) {
            f = 1;
        }
        return f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViews();
    }


    OnDataChanged ondataChanged;

    public void setOndataChanged(OnDataChanged ondataChanged) {
        this.ondataChanged = ondataChanged;
    }

    public interface OnDataChanged {

        void onFirstDataChange(float var);

        void onSecondDataChange(float var);
    }
}
