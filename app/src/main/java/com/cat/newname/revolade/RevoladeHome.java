package com.cat.newname.revolade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cat.newname.login.Login;
import com.cat.newname.R;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DecimalFormat;

public class RevoladeHome extends AppCompatActivity {


    SharedPreferences shared;
    int target, scored, unscored;
    double scoredPercentage, unscoredPercentage;

    TextView scaoredtv, unscoredtv, targettv, logout;
    ImageView addBtn;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revolade_home);

        scaoredtv = findViewById(R.id.scoredtxt);
        unscoredtv = findViewById(R.id.unscoredtxt);
        targettv = findViewById(R.id.targettxt);
        logout = findViewById(R.id.logout);
        addBtn = findViewById(R.id.addBtn);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);
        target = shared.getInt("target", 0);
        scored = shared.getInt("scored", 0);
        unscored = target - scored;
        scoredPercentage = Double.valueOf(shared.getString("percentage", ""));
        unscoredPercentage = 100 - scoredPercentage;
        scaoredtv.setText(String.valueOf(scored));
        unscoredtv.setText(String.valueOf(unscored));
        targettv.setText(String.valueOf(target));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RevoladeAddPatient.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
                finish();
            }
        });


        AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();

        config
                .strokeMode(false)
//                .floatExpandAngle(15f)
//                .floatShadowRadius(18f)
                .floatUpDuration(1000)
                .floatDownDuration(1000)
                .splitAngle(1f)
//                .floatExpandSize(15)
//                .strokeWidth(15)
                .duration(1000)
//                .startAngle(-90f)
                .drawText(true)
                .textSize(34)
//                .textMargin(8)
//                .autoSize(true)
                .pieRadius(200)
                .pieRadiusRatio(0.8f)
//                .guidePointRadius(2)
//                .guideLineWidth(4)
//                .guideLineMarginStart(8)
        ;


        config.addData(new SimplePieInfo(unscored, ContextCompat.getColor(this, R.color.graphYellow), "Unscored : " + df2.format(unscoredPercentage) + " %"))
                .addData(new SimplePieInfo(scored, ContextCompat.getColor(this, R.color.colorAccent), "Scored : " + df2.format(scoredPercentage) + " %"));

        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    }
}
