
package com.m1pay.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;

public class DrawableFactory {
    public static Drawable makeRoundedRectange(String hexColor) {//can hoi
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(hexColor));
        drawable.setCornerRadius(8);
        return drawable;
    }

    public static Drawable makeRoundedButtonDefaultState(String hexSolidColor,// can hoi
            String hexBoundColor, int cornerRadius) {
        StateListDrawable stateListDrawables = new StateListDrawable();
        //press-state default
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(Color.parseColor(hexSolidColor.replace("#", "#bb")));
        pressDrawable.setStroke(1, Color.parseColor(hexBoundColor));
        pressDrawable.setCornerRadius(cornerRadius);
        stateListDrawables.addState(new int[] {
            android.R.attr.state_pressed
        }, pressDrawable);

        //Normal state
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(Color.parseColor(hexSolidColor));
        normalDrawable.setStroke(1, Color.parseColor(hexBoundColor));
        normalDrawable.setCornerRadius(cornerRadius);

        stateListDrawables.addState(new int[] {}, normalDrawable);
        return stateListDrawables;
    }

    public static Drawable makeRoundedRectangeBox(String hexSolidColor, String hexBoundColor,
            int connerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        try {
            drawable.setCornerRadius(connerRadius);
            if (hexSolidColor != null) {
                drawable.setColor(Color.parseColor(hexSolidColor));
            }
            if (hexBoundColor != null) {
                drawable.setStroke(1, Color.parseColor(hexBoundColor));
            }
        } catch (IllegalArgumentException e) {
            Log.e(DrawableFactory.class.getName(), "" + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(DrawableFactory.class.getName(), "" + e.getLocalizedMessage());
        }
        return drawable;
    }

    public static Drawable makeRoundedRectangeBox(String hexSolidColor, String hexBoundColor,
            int connerRadius, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        try {
            drawable.setCornerRadius(connerRadius);
            if (hexSolidColor != null) {
                drawable.setColor(Color.parseColor(hexSolidColor));
            }
            if (hexBoundColor != null) {
                drawable.setStroke(strokeWidth, Color.parseColor(hexBoundColor));
            }
        } catch (IllegalArgumentException e) {
            Log.e(DrawableFactory.class.getName(), "" + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(DrawableFactory.class.getName(), "" + e.getLocalizedMessage());
        }
        return drawable;
    }

    public static Drawable makeRoundedButtonWithConner(int cornerTopLeft, int cornerTopRight,
            int cornerBottomRight, int cornerBottomLeft, String hexSolidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(hexSolidColor));
        drawable.setCornerRadii(new float[] {
                cornerTopLeft, cornerTopLeft, cornerTopRight, cornerTopRight, cornerBottomRight,
                cornerBottomRight, cornerBottomLeft, cornerBottomLeft
        });
        return drawable;
    }
}
