package cabbage63.github.com.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.animation.ValueAnimator;

/**
 * TODO: document your custom view class.
 */
public class StripeTextView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private int mDuration = 1500;
    private boolean mIsVisible;

    private static final String TAG = StripeTextView.class.getSimpleName();

    private Bitmap bitmap;
    private double rate = 0.2;

    ValueAnimator animator;
    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //any process
            invalidate();
        }
    };

    public StripeTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public StripeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StripeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StripeTextView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.StripeTextView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.StripeTextView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.StripeTextView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.StripeTextView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.StripeTextView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        //animation setting
        this.mIsVisible = false;
        animator = ValueAnimator.ofFloat(0.0f,1.0f);
        animator.addUpdateListener(listener);
        animator.setDuration(mDuration);

        bitmap = ((BitmapDrawable)mExampleDrawable).getBitmap();
        Log.v(TAG, "width:" + bitmap.getWidth());

    }

    public void show(){
        mIsVisible = true;
        animator.start();
    }

    public void hide(){
        mIsVisible = false;
        animator.start();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = (int)(bitmap.getWidth() * rate);
        int contentHeight = (int)(bitmap.getHeight() * rate);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(0,0,contentWidth,contentHeight);
            mExampleDrawable.draw(canvas);
        }

        float animValue = (Float)(animator.getAnimatedValue());
        float coordinateX = (animValue * contentWidth);
        Paint p = new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.BLACK);
        canvas.drawLine(coordinateX, 0, coordinateX, getHeight(), p);
        Log.v(TAG, "contentWidth:" + contentWidth + "contentHeight" + contentHeight);

        for(int y=0; y<contentHeight; y++){
            int pixel = bitmap.getPixel((int)coordinateX,(int)(y/0.2));
            p = new Paint();
            p.setStrokeWidth(1);

            p.setColor(Color.argb(
                    Color.alpha(pixel),
                    0xFF - Color.red(pixel),
                    0xFF - Color.green(pixel),
                    0xFF - Color.blue(pixel)));

            canvas.drawLine(coordinateX, y, getWidth(),y,p);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    public void setDuration(int duration){
        this.mDuration = duration;
        animator.setDuration(duration);
    }
}
