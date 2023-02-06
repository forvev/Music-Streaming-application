package com.example.musicfun.datatype;

import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;

public class RelativeSizeColorSpan extends RelativeSizeSpan {
    private int color;

    public RelativeSizeColorSpan(float spanSize, int spanColor) {
        super(spanSize);
        color = spanColor;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setColor(color);
    }
}
