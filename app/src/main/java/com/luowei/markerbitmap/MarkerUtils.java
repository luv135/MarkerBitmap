package com.luowei.markerbitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;

import com.orhanobut.logger.Logger;

/**
 * Created by LuoWei on 2016/5/24.
 */
public class MarkerUtils {

    public static Bitmap getMarker(Bitmap bitmap, int bitmapRadius) {
        return getMarker(bitmap, bitmapRadius, Color.WHITE, 3, Color.RED);
    }

    public static Bitmap getMarker(Bitmap bitmap) {
        return getMarker(bitmap, 0, Color.WHITE, 3, Color.RED);
    }

    /**
     * @param bitmap
     * @param broadColor
     * @param broadWidth
     * @param arrowColor
     * @return
     */
    public static Bitmap getMarker(Bitmap bitmap, int bitmapRadius, @ColorInt int broadColor, int broadWidth, @ColorInt int arrowColor) {

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

//        if (broadWidth < 0) broadWidth = 0;
        int size = Math.min(bitmapWidth, bitmapHeight);

        int bitmapDiameter = bitmapRadius * 2;

        if (/*bitmapDiameter > size ||*/ bitmapDiameter <= 0) {
            bitmapDiameter = size;
            bitmapRadius = bitmapDiameter / 2;
        }
        Logger.d("width=%d,height=%d,diameter=%d", bitmapWidth, bitmapHeight, bitmapDiameter);


        float ratio = bitmapDiameter * 1.0f / bitmapHeight;
        if (ratio != 1) {
            bitmapWidth *= ratio;
            bitmapHeight *= ratio;
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true);
        }

        int offsetX = (bitmapWidth - bitmapDiameter) / 2;
        int offsetY = (bitmapHeight - bitmapDiameter) / 2;

        Logger.d("width=%d,height=%d,diameter=%d,offsetX=%d,offsetY=%d", bitmapWidth, bitmapHeight, bitmapDiameter, offsetX, offsetY);
        int width = (int) (bitmapDiameter * 11 / 10f)+broadWidth;//(int) Math.sqrt(bitmapDiameter / 2f * bitmapDiameter / 2f * 2) * 2;
        float outRadius = width / 2f;
        int degree = 45;
        int height = (int) (width / 2f / Math.sin(Math.PI / 180f * degree) + outRadius);
        Bitmap bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(broadColor);

        //截取bitmap一个圆形显示
        canvas.drawCircle(outRadius, outRadius, bitmapRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.save();
        Matrix matrix = new Matrix();
        float d = outRadius - bitmapRadius;
        int dx = (width - bitmapWidth) / 2;
        int dy = (width - bitmapHeight) / 2;
        Logger.d("dx=%d,dy=%d", dx, dy);
        matrix.setTranslate(dx, dy);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
        canvas.restore();

        //背景红色以及箭头
        Path path = new Path();
        RectF rectF = new RectF(0, 0, width, width);
        path.arcTo(rectF, 180 - degree, 180 + degree * 2);  //半圆多一点
        path.lineTo(outRadius, height);    //箭头底部位置

        paint.setColor(arrowColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawPath(path, paint);

//        //画图片边框的白色圆环
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint.setColor(broadColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(broadWidth);
        canvas.drawCircle(outRadius, outRadius, bitmapRadius, paint);

        return bitmap1;
    }

}
