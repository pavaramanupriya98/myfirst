package in.championswimmer.libsocialbuttons;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * Created by talat on 08-07-2016.
 */
public class SocialFab extends FloatingActionButton {


    public SocialFab(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Social,
                0, 0);
        try {

            int social = a.getInteger(R.styleable.Social_social, 0);
            String buttonClass = getResources().getStringArray(R.array.available_buttons)[social];
            TypedArray ar = context.getResources().obtainTypedArray(getResources().getIdentifier(buttonClass, "array", context.getPackageName()));
            int colorNormal = ar.getColor(0, 0);
            @SuppressWarnings("ResourceType")
            Drawable logo = ar.getDrawable(1);
            ar.recycle();


            int colorRipple = ContextCompat.getColor(context, R.color.ripple);
            int colorPressed = Utils.blendColors(colorNormal, colorRipple, 0.8f);


            setBackgroundColor(colorNormal);
            setBackgroundTintList(ColorStateList.valueOf(colorNormal));
            setRippleColor(colorRipple);
            setImageDrawable(logo);


        } finally {
            a.recycle();
        }


    }
}
