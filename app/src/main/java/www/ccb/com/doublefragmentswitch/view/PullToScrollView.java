package www.ccb.com.doublefragmentswitch.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import www.ccb.com.doublefragmentswitch.R;


public class PullToScrollView extends LinearLayout {

    private final String TAG = "ccb";

    private Context mContent;
    private View headView, footView;
    private ScrollView contentView;
    private int headerHeight;
    private int footHeight;
    private RelativeLayout rootView;

    public PullToScrollView(Context context) {
        super(context);
        mContent = context;
        init();

    }

    public PullToScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContent = context;
        init();
    }

    public PullToScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContent = context;
        init();
    }

    private void init() {
//        this.setClickable(true);
        View mView = LayoutInflater.from(mContent).inflate(R.layout.pullto_content, this);
        headView = mView.findViewById(R.id.viewHead);
        contentView = mView.findViewById(R.id.viewContent);
        footView = mView.findViewById(R.id.viewFoot);
        rootView = mView.findViewById(R.id.rootView);

        this.post(new Runnable() {
            @Override
            public void run() {
                headerHeight = headView.getMeasuredHeight();
                footHeight = footView.getMeasuredHeight();
                setViewPadding(-headerHeight, -footHeight);
            }
        });

    }

    public void setViewPadding(int h, int f) {
        this.setPadding(this.getPaddingLeft(), h, this.getPaddingRight(), f);
    }

    int dowmY = 0;
    boolean isDown,isUp , isMove;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = isCanPullDown();
                isUp = isCanPullUp();
                dowmY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (!isDown && !isUp) {
                    dowmY = (int) event.getY();
                    isDown = isCanPullDown();
                    isUp = isCanPullUp();
                    break;
                }



                int off = (int) (event.getY()) - dowmY;
                off = (int) (off*0.6);

                //是否应该移动布局

                boolean shouldMove =
                        (isDown && off > 0)    //可以下拉， 并且手指向下移动
                                || (isUp && off < 0)    //可以上拉， 并且手指向上移动
                                || (isDown && isUp); //既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）
//                if (shouldMove) {
//                    isMove = true;
//                    setViewPadding(-headerHeight + off, -footHeight);
//                }
                if (shouldMove) {
                    isMove = true;
                    setViewPadding(-headerHeight + off, -(footHeight + off));
                    Log.i(TAG, "dispatchTouchEvent:布局移动 "+off);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    setViewPadding(-headerHeight, -footHeight);
                    isMove = false;
                    isUp = false;
                    isDown = false;
                }

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private View zView;

    @Override

    protected void onFinishInflate() {

        super.onFinishInflate();
        if (contentView.getChildCount() > 0) {
            zView = contentView.getChildAt(0);
        }

    }


    /**
     * 判断是否滚动到顶部
     */

    private boolean isCanPullDown() {

        return contentView.getScrollY() == 0 ||

                zView.getHeight() < contentView.getHeight() + contentView.getScrollY();

    }


    /**
     * 判断是否滚动到底部
     */

    private boolean isCanPullUp() {

        return zView.getHeight() <= contentView.getHeight() + contentView.getScrollY();

    }


}
