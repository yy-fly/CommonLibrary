package com.yyfly.android.common.ui.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.yyfly.android.common.R

/**
 * 基础Toolbar
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
open class BaseToolbarActivity : BaseActivity() {

    var tintManager: SystemBarTintManager? = null

    var mContext: Context? = null
    private var rootLayout: LinearLayout? = null
    var mToolbar: Toolbar? = null
    var layout_left: RelativeLayout? = null
    var layout_center: RelativeLayout? = null
    var layout_right: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base_toolbar)
        setTheme(R.style.ToolbarActivityTheme)

        // 在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        tintManager = SystemBarTintManager(this)
        tintManager?.isStatusBarTintEnabled = true
        setStatusBarTintResource(R.color.colorPrimary)
        initToolbar()
    }

    override fun setContentView(layoutId: Int) {
        setContentView(View.inflate(this, layoutId, null))
    }

    override fun setContentView(view: View) {
        rootLayout = findViewById(R.id.root_layout) as LinearLayout
        if (rootLayout == null) return
        rootLayout!!.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        initToolbar()
    }

    /**
     * Set status bar tint resource.
     *
     * @param color the color
     * @author : mingweigao / 2017-03-19
     */
    fun setStatusBarTintResource(@ColorRes color: Int) {
        if (null != tintManager) {
            tintManager?.setStatusBarTintResource(color)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val m = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                    m.isAccessible = true
                    m.invoke(menu, true)
                } catch (e: Exception) {
                }

            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    override fun onPrepareOptionsPanel(view: View?, menu: Menu?): Boolean {
        if (menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val m = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                    m.isAccessible = true
                    m.invoke(menu, true)
                } catch (e: Exception) {
                }

            }
        }
        return true
    }

    /**
     * 初始ToolBar
     */
    protected fun initToolbar() {
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        layout_left = findViewById(R.id.layout_left) as RelativeLayout
        layout_center = findViewById(R.id.layout_center) as RelativeLayout
        layout_right = findViewById(R.id.layout_right) as RelativeLayout

        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 设置头部标题
     *
     * @param title        自定义标题
     * @param showHomeAsUp 是否显示返回
     */
    @JvmOverloads fun setToolBarTitle(title: CharSequence, showHomeAsUp: Boolean = true) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp)
        val textView = TextView(this)
        textView.text = title
        textView.maxLines = 1
        textView.gravity = Gravity.CENTER
        textView.textSize = 20f
        textView.setTextColor(resources.getColor(R.color.color_text_ffffff))
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layout_center!!.removeAllViews()
        layout_center!!.addView(textView, layoutParams)
    }

    /**
     * 设置自定义头部标题
     *
     * @param view         自定义标题View
     * @param showHomeAsUp 是否显示返回
     */
    @JvmOverloads fun setToolBarTitle(view: View, showHomeAsUp: Boolean = true) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layout_center!!.removeAllViews()
        layout_center!!.addView(view, layoutParams)
    }

    /**
     * 设置toolbar右边自定义视图
     *
     * @param view 自定义视图
     */
    protected fun setToolBarRight(view: View) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layout_right!!.removeAllViews()
        layout_right!!.addView(view, layoutParams)
    }

    /**
     * 设置toolbar左边自定义视图
     *
     * @param view 自定义视图
     */
    protected fun setToolBarLeft(view: View) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layout_left!!.removeAllViews()
        layout_left!!.addView(view, layoutParams)
    }

    /**
     * 设置自定义toolbar
     *
     * @param view         自定义toolbar视图
     * @param showHomeAsUp 是否显示返回
     */
    @JvmOverloads fun setToolBar(view: View, showHomeAsUp: Boolean = false) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layout_center!!.removeAllViews()
        layout_center!!.addView(view, layoutParams)
    }

}