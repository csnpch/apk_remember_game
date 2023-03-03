package com.example.assign06_6306021621138

import android.widget.ImageView

class Item(
    var imgView: ImageView,
    var drawableSource: Int,
    var valueMatch: String,
    var statusHidden: Boolean = true,
    var statusFound: Boolean = false
) {

    init {
        this.hiddenItem()
    }

    private fun showItem() {
        this.imgView.setImageResource(drawableSource)
    }

    private fun hiddenItem() {
        this.imgView.setImageResource(R.drawable.hidden)
    }

    private fun changeImgHiddenByStatusHidden() {
        if (this.statusHidden)
            this.showItem()
        else
            this.hiddenItem()
    }

    fun setHidden(status: Boolean) {
        this.statusHidden = status
        this.changeImgHiddenByStatusHidden()
    }

    fun toggleHidden() {
        this.statusHidden = !this.statusHidden
        this.changeImgHiddenByStatusHidden()
    }

    fun setFound(status: Boolean) {
        this.statusFound = status
    }

}