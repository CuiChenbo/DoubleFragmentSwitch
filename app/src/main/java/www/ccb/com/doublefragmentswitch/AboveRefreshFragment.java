package www.ccb.com.doublefragmentswitch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import www.ccb.com.doublefragmentswitch.view.ContinueSlideScrollView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboveRefreshFragment extends Fragment {


    public AboveRefreshFragment() {
        // Required empty public constructor
    }


    public RefreshSwitchActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_refresh_above, container, false);
        initView(v);
        mActivity = (RefreshSwitchActivity) getActivity();
        return v;
    }

    private void initView(View v) {
        final PullToRefreshScrollView pullToRefreshScrollView = v.findViewById(R.id.sc);
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
        pullToRefreshScrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (direction == PullToRefreshBase.Mode.PULL_FROM_END){ //下拉
                    if (state == PullToRefreshBase.State.RELEASE_TO_REFRESH){ //到达触发临界点
                        mActivity.goBottom();
                    }
                }
            }
        });
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
               pullToRefreshScrollView.onRefreshComplete();
            }
        });
    }



}
