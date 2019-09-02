package com.birthday.common.ui

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.birthday.ui.R
import android.util.TypedValue

class ItemDivider(context: Context) : RecyclerView.ItemDecoration() {
  private var mDivider: Drawable? = null
  private val PADDING_IN_DIPS = 24.0F
  private var mPadding: Int = 0

  init {
    mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
    val metrics = context.resources.displayMetrics
    mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_IN_DIPS, metrics).toInt()
  }

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    val left = parent.paddingLeft
    val right = parent.width - parent.paddingRight

    val childCount = parent.childCount
    mDivider?.setBounds(left, parent.top, right, 2)
    mDivider?.draw(c)

    for (i in 0 until childCount - 1) {
      val child = parent.getChildAt(i)

      val params = child.layoutParams as RecyclerView.LayoutParams

      val top = child.bottom + params.topMargin
      var bottom = 0
      mDivider?.getIntrinsicHeight()?.let {
        bottom = top + it

      }

      mDivider?.setBounds(left + mPadding, top, right - mPadding, bottom)
      mDivider?.draw(c)
    }
  }
}