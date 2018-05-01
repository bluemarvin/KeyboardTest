package org.mozilla.keyboardtest;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler();
    KeyCharacterMap mMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
    char mChar[] = new char[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyboardView view = findViewById(R.id.keyboard);
        Keyboard keyboard = new Keyboard(getApplicationContext(), R.xml.qwerty);
        view.setPreviewEnabled(false);
        view.setKeyboard(keyboard);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("foo", "OnKeyListener: " + i + " " + keyEvent.toString());
                return false;
            }
        });
        view.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {
                Log.e("foo", "onPress: " + i);
            }

            @Override
            public void onRelease(int i) {
                Log.e("foo", "onRelease: " + i);

            }

            @Override
            public void onKey(int i, int[] ints) {
                String list = "";
                for (int value: ints) {
                    list = list + " " + value;
                }
                Log.e("foo", "onKey: " + i + " " + list);
                View current = getCurrentFocus();
                if (current != null) {
                    if (i >= 0) {
                        mChar = Character.toChars(i);
                        if (mChar == null) {
                            Log.e("foo","No chars found");
                            return;
                        }
                        KeyEvent events[] = mMap.getEvents(mChar);
                        if (events == null) {
                            Log.e("foo", "No events generated");
                            return;
                        }
                        for (KeyEvent event : events) {
                            if (current.dispatchKeyEvent(event)) {
                                Log.e("foo", "dispatched: " + event.toString());
                            } else {
                                Log.e("Foo", "Did NOT dispatch: " + event.toString());
                            }
                        }
                    }
                }
            }

            @Override
            public void onText(CharSequence charSequence) {
                Log.e("foo", "onText: " + charSequence.toString());

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });
        View top = findViewById(R.id.top);
        top.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldView, final View newView) {
                Log.e("foo", "old focus: " + ((oldView != null) ? oldView.toString() : "null") + "\nnew focus: " + ((newView != null) ? newView.toString() : "null"));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(newView.getWindowToken(), 0);
                    }
                }, 100);

            }
        });
    }

}
