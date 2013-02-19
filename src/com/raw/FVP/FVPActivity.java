package com.raw.FVP;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FVPActivity extends Activity {
	/** Called when the activity is first created. */
	ImageView imageViewPage1 ;
	ImageView imageViewPage2 ;
	RelativeLayout mainContainer;
	GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imageViewPage1 = (ImageView) findViewById(R.id.imageViewPage1);
		imageViewPage2 = (ImageView) findViewById(R.id.imageViewPage2);
		mainContainer  = (RelativeLayout) findViewById(R.id.MainContainer);
		gestureDetector = new GestureDetector(new MyGestureDetector());


		mainContainer.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					System.out.println("================111");
					return gestureDetector.onTouchEvent(event);
				}else{
					return true;
				}



			}
		});
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		LayoutParams lp = (LayoutParams) imageViewPage2.getLayoutParams();

		private Bitmap bitmap;
		private int bmpWidth;
		private int bmpHeight;

		private float curSkewX = 0.64F;

		private float curSkewY = 0.06F;

		private float curRotate = 96;


		private MyGestureDetector () {
			createBitmap(imageViewPage2);
		}

		private void drawMatrix(){

			Matrix matrix = new Matrix();
			matrix.reset();
			//			matrix.postScale(curScale, curScale);
			//			matrix.postSkew(100, 10, 100, 100);

			matrix.postRotate(curRotate);
			matrix.postSkew(curSkewX, curSkewY, 0, imageViewPage2.getHeight()/2);

			/*matrix.setSkew(curSkewX, curSkewY,0,900);
			matrix.postRotate(curRotate,500, 100);
			matrix.postScale( curSkewY+1,curSkewX+1);
			 */
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);



			//System.out.println("=========================   "+matrix.toString());
			//imageViewPage2.setImageMatrix(matrix);
			//imageViewPage2.invalidate();
			//	imageViewPage2.setLayoutParams(lp);

		}

		private void createBitmap(View iv){

			/*BitmapDrawable drawable = (BitmapDrawable) iv.getBackground();
			this.bitmap = drawable.getBitmap();*/

			iv.setDrawingCacheEnabled(true);
			iv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			iv.layout(0, 0, iv.getMeasuredWidth(), iv.getMeasuredHeight()); 

			imageViewPage2.buildDrawingCache();
			bitmap = iv.getDrawingCache();

			System.out.println("=================================  bitmap   "+bitmap);
			bmpWidth = bitmap.getWidth();
			bmpHeight = bitmap.getHeight();

			//iv.setDrawingCacheEnabled(false);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,	float distanceX, float distanceY) {
			// TODO Auto-generated method stub

			System.out.println("===========distanceY=====22 distanceY  "+distanceY);
			lp.width = LayoutParams.FILL_PARENT;	

			if(distanceY>0){
				if(imageViewPage2.getHeight() > 10){
					lp.height = imageViewPage2.getHeight()-10;
				}

			}else{
				lp.height = imageViewPage2.getHeight()+10;

			}

			imageViewPage2.setLayoutParams(lp);
			return false;
		}

	}
}