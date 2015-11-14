package carnegieart.supplyandcode.duco.Helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import carnegieart.supplyandcode.duco.R;

/**
 * Created by dkukreja on 9/30/15.
 */
public class Spanner extends View {
    int circles = 3;
    int circleRadius = 20;
    float offset = 0;

    int resID = 0;

    ArrayList<Float> innerRadianAngles = new ArrayList<Float>();
    Paint mPaint;
    int width = 0;
    int height = 0;
    int centerx = 0;
    int centery = 0;
    float radius = 75;
    float baseRadius = 75;

    public float baseSpeed = 0.05f;
    float maxSpeed = 0.15f;
    public float tarSpeed = 0.15f;
    float speed = 0.05f;
    float radiusSpeed = 0f;
    float radiusAcceleration = 0.0f;

    float acceleration = 0.005f;
    int color;

    public static boolean touchDown = false;

    public Spanner(Context context) {
        super(context);
    }

    public Spanner(Context context, Integer coorcl, Integer rad, Float spood, Integer kirkrad, Integer kirkCol, Float accel, Float offsoot){
        super(context);
        circles = coorcl;
        radius = rad;
        speed = spood;
        circleRadius = kirkrad;
        color = kirkCol;
        acceleration = accel;
        offset = offsoot;

        baseSpeed = speed;
        baseRadius = radius;
    }

    public Spanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                0, 0);

        try {
            circles = a.getInteger(R.styleable.CustomView_sides, 3);
            radius = a.getFloat(R.styleable.CustomView_rotation_radius, 75);
            speed = a.getFloat(R.styleable.CustomView_rotation_speed, 0.05f);
            circleRadius = a.getInteger(R.styleable.CustomView_circle_size, 20);
            color = a.getColor(R.styleable.CustomView_circle_color, getResources().getColor(R.color.colorPrimary));
            acceleration = a.getFloat(R.styleable.CustomView_acceleration, 0.005f);
            offset = a.getFloat(R.styleable.CustomView_offset,0f);
            resID = a.getResourceId(R.styleable.CustomView_resource_id,0);

            baseSpeed = speed;
            baseRadius = radius;
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchDown = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            touchDown = false;
        }
        return false;
    }

    void update(){
        for (int i=0; i< innerRadianAngles.size(); i++){
            innerRadianAngles.set(i, innerRadianAngles.get(i)+speed);
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) init();
        update();

        invalidate();
        for (Float angle : innerRadianAngles) {
            float y = (float)(radius * Math.sin(angle));
            float x = (float)(radius * Math.cos(angle));
            if (resID == 0)
                canvas.drawCircle(centerx+x, centery+y, circleRadius, mPaint);
            else {
                View v = getRootView().findViewById(resID);
                v.setVisibility(VISIBLE);
                canvas.save();
                canvas.translate(centerx+x - v.getMeasuredWidth()/2, centery+y - v.getMeasuredHeight()/2);
                v.draw(canvas);
                canvas.restore();
                System.out.println("x,y"+"("+x+", "+y+")");
            }
        }
    }

    ArrayList<Float> generateAngles(){
        ArrayList<Float> result = new ArrayList<Float>();
        double totalRadians = Math.toRadians(360);
        float increment = (float)(totalRadians/circles);
        for (int i=0; i<circles; i++){
            result.add(increment*i + offset);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        centerx = width/2;
        centery = height/2;
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        innerRadianAngles = generateAngles();
    }
}
