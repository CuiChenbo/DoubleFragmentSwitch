package www.ccb.com.doublefragmentswitch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class BelowFragment extends Fragment {


    public BelowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_below, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        ContinueSlideScrollView scrollView = v.findViewById(R.id.sc);
        scrollView.setonContinueSlideListener(s);
    }

    ContinueSlideScrollView.onContinueSlide s;
    public void setContinueSlideScrollView(ContinueSlideScrollView.onContinueSlide s){
        this.s = s;
    }

}
