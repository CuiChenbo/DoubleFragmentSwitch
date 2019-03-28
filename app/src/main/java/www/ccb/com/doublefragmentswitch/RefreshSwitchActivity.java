package www.ccb.com.doublefragmentswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import www.ccb.com.doublefragmentswitch.view.ContinueSlideScrollView;

public class RefreshSwitchActivity extends AppCompatActivity {

    private AboveRefreshFragment AF;
    private BelowRefreshFragment BF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_fragment);
        AF = new AboveRefreshFragment();
        BF = new BelowRefreshFragment();
        initList(); //先给Fragment设置监听
        getSupportFragmentManager().beginTransaction().add(R.id.fl,AF).commit();
    }

    private void initList() {

    }

    public void goBottom(){
        if (BF.isAdded()){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_below_in,R.anim.slide_below_out).show(BF).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_below_in,R.anim.slide_below_out)
                    .add(R.id.fl,BF).commit();
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_above_in,R.anim.slide_above_out)
                .hide(AF).commit();
    }
    public void goTop(){
        if (AF.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_above_in,R.anim.slide_above_out)
                    .show(AF).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_above_in,R.anim.slide_above_out)
                    .add(R.id.fl,AF).commit();
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_below_in,R.anim.slide_below_out)
                .hide(BF).commit();
    }
}