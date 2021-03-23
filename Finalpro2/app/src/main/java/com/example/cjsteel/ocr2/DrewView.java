package com.example.cjsteel.ocr2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import static com.google.android.gms.internal.measurement.zzwx.init;


public class DrewView extends View {

    public LayoutParams params;
    private Path path = new Path();
    private Paint line = new Paint();
    private Button resetBtn;


    public DrewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        line.setAntiAlias(true);
        line.setColor(Color.BLACK);
        line.setStyle(Paint.Style.STROKE);
        line.setStrokeJoin(Paint.Join.ROUND);
        line.setStrokeWidth(24f);
    }



    public void State() {
        //imageView = (ImageView)findViewById(R.id.view);

        //textView = (TextView)findViewById(R.id.textview);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        //searchBtn = (Button) findViewById(R.id.searchBtn);


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code
                path.reset();
                invalidate();
                line.reset();

            }

        });
    }


    public DrewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public DrewView(Context context) {

        super(context);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            default:
                return false;
        }

        postInvalidate();
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, line);

    }

    public void clearCanvas() {
        path.reset();
        invalidate();
    }


}
