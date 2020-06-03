package com.cat.newname.tasigna

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cat.newname.R
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import kotlinx.android.synthetic.main.tasigna_home_fragment.*
import java.text.DecimalFormat


class TasignaHomeActivity : AppCompatActivity() {
    private val df2 = DecimalFormat("#.##")
    lateinit var shared: SharedPreferences
    private var scoredPercentage: Double = 0.0
    private var unscoredPrecentage: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasigna_home_fragment)
        addButtonAction()
        dropButtonPatientUser()
        switchButtonPatientUser()
        initializePieChart()
        back.setOnClickListener { onBackPressed() }
    }

    private fun initializePieChart() {
        shared = this.getSharedPreferences("id", Context.MODE_PRIVATE)
        val target = shared.getInt("target", 0)
        val score = shared.getInt("score", 0)
        scoredPercentage = java.lang.Double.valueOf(shared.getString("percentage", "")!!)
        unscoredPrecentage = 100 - scoredPercentage
        // scoredPercentage = shared.getString("percentage", "0")
        //unscoredPrecentage = 100 - scoredPercentage
        val unscored = target - score
        target_text.text = target.toString()
        scoredtxt.text = score.toString()
        unscoredtxt.text = unscored.toString()
        val mAnimatedPieView = findViewById<AnimatedPieView>(R.id.animatedPieView)
        val config = AnimatedPieViewConfig()

        config
                .strokeMode(false)
                .floatUpDuration(1000)
                .floatDownDuration(1000)
                .splitAngle(1f)
                .duration(1000)
                .drawText(true)
                .textSize(34f)
                .pieRadius(200f)
                .pieRadiusRatio(0.8f)

        config.addData(
                SimplePieInfo(
                        unscored.toDouble(),
                        ContextCompat.getColor(this, R.color.red),
                        "" + df2.format(unscoredPrecentage) + " %"
                )
        )
                .addData(
                        SimplePieInfo(
                                score.toDouble(),
                                ContextCompat.getColor(this, R.color.yellow),
                                "" + df2.format(scoredPercentage) + " %"
                        )
                )
        mAnimatedPieView.applyConfig(config)
        mAnimatedPieView.start()
    }

    private fun addButtonAction() {
        val button = findViewById<ImageView>(R.id.btn_Add)
        button.setOnClickListener {
            startActivity(Intent(this, TasignaAddPatient::class.java)
                    .putExtra("fromFragment", "fromAdd"))
        }
    }

    private fun dropButtonPatientUser() {

        val dropButton = findViewById<ImageView>(R.id.btn_drop)
        dropButton.setOnClickListener {
            startActivity(Intent(this, TasignaPatientsActivity::class.java)
                    .putExtra("fromFragment", "fromDrop"))
        }
    }

    private fun switchButtonPatientUser() {
        val dropButton = findViewById<ImageView>(R.id.btn_Switch)
        dropButton.setOnClickListener {
            startActivity(Intent(this, TasignaPatientsActivity::class.java)
                    .putExtra("fromFragment", "fromSwitch"))
        }
    }
}