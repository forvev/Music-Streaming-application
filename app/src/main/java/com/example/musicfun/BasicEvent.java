package com.example.musicfun;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

public class BasicEvent {
    private boolean hasBeenHandled;
    private final Object content;

    public final boolean getHasBeenHandled() {
        return this.hasBeenHandled;
    }

    @Nullable
    public final Object getContentIfNotHandled() {
        Object var10000;
        if (this.hasBeenHandled) {
            //Log.d("testThisEvent", "Has been handled in if:" + hasBeenHandled);
            var10000 = null;
        } else {
            //Log.d("testThisEvent", "Has been handled in else:" + hasBeenHandled);
            this.hasBeenHandled = true;
            var10000 = this.content;
        }

        return var10000;
    }

    public final Object peekContent() {
        return this.content;
    }

    public BasicEvent(Object content) {
        this.content = content;
        this.hasBeenHandled = false;
    }
}
