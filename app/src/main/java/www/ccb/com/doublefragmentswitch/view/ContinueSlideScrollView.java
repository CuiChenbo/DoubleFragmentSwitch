package www.ccb.com.doublefragmentswitch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;


/**
 * 监听ScrollView滚动到顶部或者底部做相关事件拦截
 */
public class ContinueSlideScrollView extends ScrollView {

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private Context context;


    private ISmartScrollChangedListener mSmartScrollChangedListener;

    public ContinueSlideScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public ContinueSlideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ContinueSlideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 滑动到顶部或底部的监听接口
     * */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
    }

    public void setScanScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (getScrollY() == 0) {
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners();
        }
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }


    /**
     * 继续滑动的代码
     */
    private float down,move;
    private boolean isExecute = false; //是否要执行
    public int TRIGGER_DISTANCE = 100;  //继续滑动效果触发间距 dp
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                down = ev.getY();
                isExecute = true;
                break;
            case MotionEvent.ACTION_MOVE:
                move = ev.getY();

                if (isExecute && isScrolledToTop() && move - down > dp2px(context,TRIGGER_DISTANCE)){
                    isExecute = false;
                    Log.i("ccb", "onTouchEvent: 回到顶部，继续滑动"+(move - down));
                    if (continueSlide != null)continueSlide.onContinueSlideTop();
                }
                if (isExecute && isScrolledToBottom() && move - down < (-dp2px(context,TRIGGER_DISTANCE))){
                    isExecute = false;
                    Log.i("ccb", "onTouchEvent: 已经到底，继续滑动"+((move - down)));
                    if (continueSlide != null)continueSlide.onContinueSlideBottom();
                }
                break;
                case MotionEvent.ACTION_UP:
                    isExecute = true;
                    move = 0;
                    down = 0 ;
                    break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滑动到顶部或底部继续滑动到一定距离触发
     */
    public interface onContinueSlide{
        void onContinueSlideTop();
        void onContinueSlideBottom();
    }

    public onContinueSlide continueSlide;

    public void setonContinueSlideListener(onContinueSlide continueSlide){
         this.continueSlide = continueSlide;
    }


    /**
     * dip转换px
     */
    public static int dp2px(Context content, int dip) {
        final float scale = content.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
