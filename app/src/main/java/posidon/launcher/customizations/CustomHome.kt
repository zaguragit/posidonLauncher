/*
 * Copyright (c) 2019 Leo Shneyderis
 * All rights reserved
 */

package posidon.launcher.customizations

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import posidon.launcher.Main
import posidon.launcher.R
import posidon.launcher.feed.news.chooser.FeedChooser
import posidon.launcher.tools.ColorTools
import posidon.launcher.tools.Settings
import posidon.launcher.tools.Tools

class CustomHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Tools.applyFontSetting(this)
        setContentView(R.layout.custom_home)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        findViewById<View>(R.id.settings).setPadding(0, 0, 0, Tools.navbarHeight)

        findViewById<View>(R.id.clockcolorprev).background = ColorTools.colorcircle(Settings["clockcolor", -0x1])

        val widget = Settings["widget", "posidon.launcher/posidon.launcher.external.widgets.ClockWidget"]
        when {
            widget.startsWith("posidon.launcher/posidon.launcher.external.widgets.ClockWidget") -> {}
            widget.startsWith("posidon.launcher/posidon.launcher.external.widgets.BigWidget") -> {}
            else -> findViewById<View>(R.id.dateFormatCard).visibility = View.GONE
        }

        val dateformat = Settings["datef", resources.getString(R.string.defaultdateformat)]
        val datefprev = findViewById<TextClock>(R.id.datefprev)
        val dateftxt = findViewById<EditText>(R.id.dateformat)
        dateftxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val i = dateftxt.text.toString()
                datefprev.format12Hour = i
                datefprev.format24Hour = i
            }
        })
        dateftxt.setText(dateformat, TextView.BufferType.EDITABLE)


        val showBehindDock = findViewById<Switch>(R.id.showBehindDock)
        showBehindDock.isChecked = Settings["feed:show_behind_dock", false]

        val feedswitch = findViewById<Switch>(R.id.feedenabled)
        feedswitch.isChecked = Settings["feedenabled", true]

        val hidefeedswitch = findViewById<Switch>(R.id.hidefeed)
        hidefeedswitch.isChecked = Settings["hidefeed", false]

        val newsCardMaxImageWidthSlider = findViewById<SeekBar>(R.id.newsCardMaxImageWidthSlider)
        val maxWidth = Settings["feed:max_img_width", Tools.getDisplayWidth(this)]
        newsCardMaxImageWidthSlider.progress = (maxWidth.toFloat() / Tools.getDisplayWidth(this).toFloat() * 6).toInt() - 1
        newsCardMaxImageWidthSlider.max = 5
        val newsCardMaxImageWidthNum = findViewById<TextView>(R.id.newsCardMaxImageWidthNum)
        newsCardMaxImageWidthNum.text = maxWidth.toString()
        newsCardMaxImageWidthSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) { Settings.apply() }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val newVal: Int = Tools.getDisplayWidth(this@CustomHome) / 6 * (progress + 1)
                newsCardMaxImageWidthNum.text = newVal.toString()
                Settings.putNotSave("feed:max_img_width", newVal)
            }
        })

        val newscardradiusslider = findViewById<SeekBar>(R.id.newscardradiusslider)
        newscardradiusslider.progress = Settings["feed:card_radius", 15]
        val newscardradiusnum = findViewById<TextView>(R.id.newscardradiusnum)
        newscardradiusnum.text = Settings["feed:card_radius", 15].toString()
        newscardradiusslider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                newscardradiusnum.text = progress.toString()
                Settings["feed:card_radius"] = progress
            }
        })

        val cardHorizontalMarginSeekbar = findViewById<SeekBar>(R.id.cardHorizontalMarginSeekbar)
        cardHorizontalMarginSeekbar.progress = Settings["feed:card_margin_x", 16]
        val cardHorizontalMarginNum = findViewById<TextView>(R.id.cardHorizontalMarginNum)
        cardHorizontalMarginNum.text = Settings["feed:card_margin_x", 16].toString()
        cardHorizontalMarginSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                cardHorizontalMarginNum.text = progress.toString()
                Settings["feed:card_margin_x"] = progress
            }
        })

        findViewById<View>(R.id.newscardbgprev).background = ColorTools.colorcircle(Settings["feed:card_bg", -0xdad9d9])
        findViewById<View>(R.id.newscardtxtprev).background = ColorTools.colorcircle(Settings["feed:card_txt_color", -0x1])
        findViewById<Switch>(R.id.newscardenableimg).isChecked = Settings["feed:card_img_enabled", true]
        findViewById<Switch>(R.id.newscardblackgradient).isChecked = Settings["feed:card_text_shadow", true]

        findViewById<View>(R.id.notificationtitlecolorprev).background = ColorTools.colorcircle(Settings["notificationtitlecolor", -0xeeeded])
        findViewById<View>(R.id.notificationtxtcolorprev).background = ColorTools.colorcircle(Settings["notificationtxtcolor", -0xdad9d9])
        findViewById<View>(R.id.notificationbgprev).background = ColorTools.colorcircle(Settings["notificationbgcolor", -0x1])

        findViewById<Switch>(R.id.actionButtonSwitch).isChecked = Settings["notificationActionsEnabled", false]
        findViewById<Switch>(R.id.collapseNotificationSwitch).isChecked = Settings["collapseNotifications", false]
        findViewById<View>(R.id.actionBGPreview).background = ColorTools.colorcircle(Settings["notificationActionTextColor", 0x88e0e0e0.toInt()])
        findViewById<View>(R.id.actionTextColorPreview).background = ColorTools.colorcircle(Settings["notificationActionTextColor", -0xdad9d9])

        findViewById<Spinner>(R.id.notificationGrouping).setSelection(when (Settings["notifications:groupingType", "os"]) {
            "os" -> 0; "byApp" -> 1; else -> 2
        })

        Main.customized = true
    }

    fun pickclockcolor(v: View) { ColorTools.pickColor(this, "clockcolor", -0x1) }
    fun picknewscardcolor(v: View) { ColorTools.pickColor(this, "feed:card_bg", -0xdad9d9) }
    fun picknewscardtxtcolor(v: View) { ColorTools.pickColor(this, "feed:card_txt_color", -0x1) }
    fun picknotificationtitlecolor(v: View) { ColorTools.pickColor(this, "notificationtitlecolor", -0xeeeded) }
    fun picknotificationtxtcolor(v: View) { ColorTools.pickColor(this, "notificationtxtcolor", -0xdad9d9) }
    fun picknotificationcolor(v: View) { ColorTools.pickColor(this, "notificationbgcolor", -0x1) }
    fun pickNotificationActionBGColor(v: View) { ColorTools.pickColor(this, "notificationActionBGColor", 0x88e0e0e0.toInt()) }
    fun pickNotificationActionTextColor(v: View) { ColorTools.pickColor(this, "notificationActionTextColor", -0xdad9d9) }

    fun chooseFeeds(v: View) { startActivity(Intent(this, FeedChooser::class.java)) }
    fun chooseLayouts(v: View) {
        val dialog = BottomSheetDialog(this, R.style.bottomsheet)
        dialog.setContentView(R.layout.custom_home_feed_card_layout_chooser)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.bottom_sheet)
        dialog.findViewById<View>(R.id.card0)!!.setOnClickListener {
            Tools.vibrate(this)
            Settings["feed:card_layout"] = 0
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.card1)!!.setOnClickListener {
            Tools.vibrate(this)
            Settings["feed:card_layout"] = 1
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.card2)!!.setOnClickListener {
            Tools.vibrate(this)
            Settings["feed:card_layout"] = 2
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onPause() {
        Settings.apply {
            putNotSave("datef", findViewById<EditText>(R.id.dateformat).text.toString())
            putNotSave("feedenabled", findViewById<Switch>(R.id.feedenabled).isChecked)
            putNotSave("hidefeed", findViewById<Switch>(R.id.hidefeed).isChecked)
            putNotSave("feed:card_img_enabled", findViewById<Switch>(R.id.newscardenableimg).isChecked)
            putNotSave("feed:card_text_shadow", findViewById<Switch>(R.id.newscardblackgradient).isChecked)
            putNotSave("notificationActionsEnabled", findViewById<Switch>(R.id.actionButtonSwitch).isChecked)
            putNotSave("collapseNotifications", findViewById<Switch>(R.id.collapseNotificationSwitch).isChecked)
            putNotSave("feed:show_behind_dock", findViewById<Switch>(R.id.showBehindDock).isChecked)
            putNotSave("notifications:groupingType", when (findViewById<Spinner>(R.id.notificationGrouping).selectedItemPosition) {
                0 -> "os"; 1 -> "byApp"; else -> "none"
            })
            apply()
        }
        super.onPause()
    }
}