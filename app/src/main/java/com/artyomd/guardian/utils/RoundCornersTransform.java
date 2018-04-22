package com.artyomd.guardian.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

public class RoundCornersTransform implements Transformation {
	private int radius;

	public RoundCornersTransform(int radius) {
		this.radius = radius;
	}

	@Override
	public Bitmap transform(Bitmap source) {

		int width = source.getWidth();
		int height = source.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
		canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
		source.recycle();

		return bitmap;
	}

	@Override
	public String key() {
		return "RoundedTransformation(radius=" + radius + ")";
	}
}