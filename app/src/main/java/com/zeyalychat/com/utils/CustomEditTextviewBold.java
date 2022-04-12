package com.zeyalychat.com.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditTextviewBold extends AppCompatEditText {

	public CustomEditTextviewBold(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomEditTextviewBold(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomEditTextviewBold(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/CircularStd-Bold.ttf");
	    setTypeface(tf);
	}
}