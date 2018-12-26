package www.ccb.com.doublefragmentswitch;

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
    public ContinueSlideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /** 滑动到顶部和底部的监听接口 */
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
            if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                // 小心踩坑2: 这里不能是 >=
                // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners();
        }
        //为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
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
    private int TRIGGER_DISTANCE = 160;  //触发间距
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
