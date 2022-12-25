package com.example.trackmydooit;

import com.google.android.material.R;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.elevation.ElevationOverlayProvider;

public enum SurfaceColors {
    SURFACE_0(R.dimen.m3_sys_elevation_level0),
    SURFACE_1(R.dimen.m3_sys_elevation_level1),
    SURFACE_2(R.dimen.m3_sys_elevation_level2),
    SURFACE_3(R.dimen.m3_sys_elevation_level3),
    SURFACE_4(R.dimen.m3_sys_elevation_level4),
    SURFACE_5(R.dimen.m3_sys_elevation_level5);

    private final int elevationResId;

    SurfaceColors(@DimenRes int elevationResId) {
        this.elevationResId = elevationResId;
    }

    /**
     * Returns the tonal surface color value in RGB.
     */
    @ColorInt
    public int getColor(@NonNull Context context) {
        return getColorForElevation(
                context, context.getResources().getDimension(elevationResId));
    }

    /**
     * Returns the corresponding surface color in RGB with the given elevation.
     */
    @ColorInt
    public static int getColorForElevation(@NonNull Context context, @Dimension float elevation) {
        return new ElevationOverlayProvider(context).compositeOverlay(
                MaterialColors.getColor(context, R.attr.colorSurface, Color.TRANSPARENT), elevation);
    }
}
