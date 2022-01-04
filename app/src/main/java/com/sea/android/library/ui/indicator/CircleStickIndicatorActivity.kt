package com.sea.android.library.ui.indicator

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.sea.android.library.databinding.ActivityIndicatorCircleStickBinding
import java.util.*

class CircleStickIndicatorActivity : AppCompatActivity() {

    private val binding: ActivityIndicatorCircleStickBinding by lazy { ActivityIndicatorCircleStickBinding.inflate(layoutInflater) }

    private val pageAdapter = MyPageAdapter()

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        itemList.add(Color.RED)
        itemList.add(Color.BLUE)
        itemList.add(Color.YELLOW)
        itemList.add(Color.GREEN)
        itemList.add(Color.LTGRAY)
        itemList.add(Color.MAGENTA)
        itemList.add(Color.BLACK)
        itemList.add(Color.CYAN)

        binding.indicator.setCount(itemList.size)
        binding.viewPager.adapter = pageAdapter
        binding.viewPager.addOnPageChangeListener(
            object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    binding.indicator.setCurrentIndex(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            }
        )
    }

    var itemList: ArrayList<Int> = ArrayList()

    private inner class MyPageAdapter() : PagerAdapter() {

        override fun getCount(): Int {
            return itemList.size
        }

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return view === any
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView: View = generateItem(position, itemList[position])
            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

    }

    private fun generateItem(pos: Int, color: Int): View {
        val itemView = View(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        itemView.layoutParams = params
        itemView.setBackgroundColor(color)
        itemView.setOnClickListener {
            // itemList.remove(pos)
            // binding.indicator.setCount(itemList.size)
        }
        return itemView
    }

}