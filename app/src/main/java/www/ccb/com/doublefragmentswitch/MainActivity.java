package www.ccb.com.doublefragmentswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout fl;
    private AboveFragment AF;
    private BelowFragment BF;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_fragment);
        fl = (FrameLayout) findViewById(R.id.fl);
        btn = (Button) findViewById(R.id.qiehuan);
        btn.setOnClickListener(this);
        AF = new AboveFragment();
        BF = new BelowFragment();
        initList(); //先给Fragment设置监听
        getSupportFragmentManager().beginTransaction().add(R.id.fl,AF).commit();
    }

    private void initList() {
        AF.setContinueSlideScrollView(new ContinueSlideScrollView.onContinueSlide() {
            @Override
            public void onContinueSlideTop() {

            }

            @Override
            public void onContinueSlideBottom() {
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
        });

        BF.setContinueSlideScrollView(new ContinueSlideScrollView.onContinueSlide() {
            @Override
            public void onContinueSlideTop() {
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

            @Override
            public void onContinueSlideBottom() {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if ("0".equals(btn.getTag().toString())){
            btn.setTag("1");
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
        }else{
            btn.setTag("0");
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
}