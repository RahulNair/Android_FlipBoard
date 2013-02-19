package com.raw.FVP;

import FlipAnimation.AnimationFactory;
import FlipAnimation.AnimationFactory.FlipDirection;
import FlipAnimation.FlipAnimationFactory;
import FlipAnimation.FlipAnimationFactory.ViewAction;
import FlipAnimation.FlipAnimationFactory.ViewPosition;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FlipBoardNewsPaper extends Activity{

	RelativeLayout captureFrame;
	FrameLayout parentView;
	FrameLayout paperOneFrameLayout;
	ImageView realTopCard;
	ImageView realBottomCard;

	RelativeLayout topCacheImageViewRelativeLayout;
	//	RelativeLayout topCacheImageViewContainer;
	RelativeLayout bottomCacheImageViewRelativeLayout;

	ImageView topCacheImageView;
	ImageView bottomCacheImageView;

	LayoutParams topCacheImageViewLayoutParams;
	LayoutParams bottomCacheImageViewLayoutParams;

	Context _context;
	int topImgID = 0x001;
	int bottomImgID = 0x002;
	private Display display;

	private int displayWidth;
	private int displayHeight;

	boolean isFingerDown;
	Float touchDownPosition;
	int countForMotionTest;

	private GestureDetector gestureDetector ;
	private String BOTTOMIMAGE = "bottomImage";
	private String TOPIMAGE = "topImage";
	private String BOTHIMAGE = "bothImage";

	Integer[] listOfNewsFeed  = {R.drawable.news1,R.drawable.news2,R.drawable.indianexpress,R.drawable.guardian20070511,R.drawable.times_123,R.drawable.article_g,
			R.drawable.sunday_times,R.drawable.theottawacitizen1};
	
//	Integer[] listOfNewsFeed  = {R.drawable.news1,R.drawable.news2};
	int countOfPage = 0;
	int countExOne = 0;
	int countExTwo = 0;
	private int countExThree = 0;
	private int countExFour = 0;
	private float distanceYcpy ;
	boolean moveingBottom;
	private Matrix matrixMirrorY;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_context = this;
		setContentView(R.layout.newpapers);
		getScreenDimensions();

		gestureDetector = new GestureDetector(new MyGestureDetector());
		parentView = (FrameLayout) findViewById(R.id.MainContainerNews);
		paperOneFrameLayout = (FrameLayout) findViewById(R.id.paperOne);
		
		
//		realTopCard = (ImageView)findViewById(R.id.realTopImageView);
//		realBottomCard = (ImageView)findViewById(R.id.realbottomTopImageView);
//		RelativeLayout.LayoutParams realTopCardParam = (LayoutParams) realTopCard.getLayoutParams();
//		realTopCardParam.height = 200;
//		realTopCard.setLayoutParams(realTopCardParam);
	
//		RelativeLayout.LayoutParams realBottomCardParam = (LayoutParams) realBottomCard.getLayoutParams();
//		realTopCardParam.height = displayHeight/2;
//		realBottomCard.setLayoutParams(realBottomCardParam);


		/*LayoutParams lp = new LayoutParams(displayWidth,displayHeight+76);
		paperOneFrameLayout.setLayoutParams(lp);
		paperOneFrameLayout.invalidate();*/

		System.out.println("========================== displayWidth width  "+displayWidth);
		System.out.println("========================== displayHeight()  "+displayHeight);




		paperOneFrameLayout.setBackgroundResource(listOfNewsFeed[countOfPage]);
		
//		realTopCard.setImageResource(listOfNewsFeed[countOfPage]);
//		countOfPage ++;
//		realBottomCard.setImageResource(listOfNewsFeed[countOfPage]);
		countOfPage ++;
		
		paperOneFrameLayout.setDrawingCacheEnabled(true);

		
		createCaptureFrame();
		parentView.addView(captureFrame);
	
		captureFrame.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					countForMotionTest = 10;
			//	return false;	
				return gestureDetector.onTouchEvent(event);
				}else if (event.getAction() == MotionEvent.ACTION_DOWN){
					System.out.println("================QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQAAAAAAAAAAAAAAAAAAAAAAA   "+countOfPage);
					captureImageAndAssing();
					
					paperOneFrameLayout.setBackgroundResource(listOfNewsFeed[countOfPage]);
//					realTopCard.setImageResource(listOfNewsFeed[countOfPage]);
//					if (listOfNewsFeed.length > countOfPage) {
//						countOfPage ++;	
//					}
//					realBottomCard.setImageResource(listOfNewsFeed[countOfPage]);
					
					touchDownPosition = event.getY();
					countForMotionTest = -1;
					isFingerDown = true;
					System.out.println("===============11==========paperOneFrameLayout= displayWidth width  "+paperOneFrameLayout.getWidth());
					System.out.println("===============22==========paperOneFrameLayout= displayHeight()  "+paperOneFrameLayout.getHeight());
					bottomCacheImageView.setAnimation(null);
					return true;
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					
					isFingerDown = false;
					countForMotionTest = -1;
					countExOne = 0;
					countExTwo = 0;
					countExThree = 0;
					countExFour = 0;
					countOfPage ++;

					if(countOfPage >= listOfNewsFeed.length){
						countOfPage = 0;
					}

					System.out.println("==MotionEvent.ACTION_UP======countOfPage===="+countOfPage);
					System.out.println("==MotionEvent.ACTION_UP=================bottomCacheImageView.getHeight()  "+bottomCacheImageView.getHeight());
					System.out.println("==MotionEvent.ACTION_UP================topCacheImageView.getHeight() "+topCacheImageView.getHeight());
					System.out.println("==MotionEvent.ACTION_UP========@@=======movingBottom "+moveingBottom);
					System.out.println("==MotionEvent.ACTION_UP================distanceY "+distanceYcpy);
				
					
					if (moveingBottom) {
						
						if (bottomCacheImageView.getHeight() == 0) {
							// swipe started from bottom crossed to top half 
							topCacheImageViewLayoutParams = null;
							topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);
							topCacheImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
							topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
							
							if (topCacheImageView.getAnimation() != null) {
								topCacheImageView.getAnimation().cancel();	
							}
							startAnimation(topCacheImageView, ViewAction.CLOSE_VIEW, ViewPosition.VIEW_TOP_HALF,150);
						}else{
							if (bottomCacheImageView.getAnimation() != null) {
								bottomCacheImageView.getAnimation().cancel();	
							}
							startAnimation(bottomCacheImageView, ViewAction.OPEN_VIEW, ViewPosition.VIEW_BOTTOM_HALF,250);	
						}
						
					}else{
						if (topCacheImageView.getHeight() == 0) {
							System.out.println("==MotionEvent.ACTION_UP=====****************==========");
							//started from top crossed to bottom half
							bottomCacheImageViewLayoutParams = null;
							bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);
							bottomCacheImageViewLayoutParams.addRule(RelativeLayout.BELOW,topCacheImageView.getId());
							bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
							
							if (bottomCacheImageView.getAnimation() != null) {
								bottomCacheImageView.getAnimation().cancel();	
							}
							startAnimation(bottomCacheImageView, ViewAction.CLOSE_VIEW, ViewPosition.VIEW_BOTTOM_HALF,150);
							
						}else{
							if (topCacheImageView.getAnimation() != null) {
								topCacheImageView.getAnimation().cancel();	
							}
							startAnimation(topCacheImageView, ViewAction.OPEN_VIEW, ViewPosition.VIEW_TOP_HALF,250);	
						}
						
						
					}
					
				

					return true;
				}else{
					isFingerDown = false;
					//countForMotionTest = -1;
					return true;
				}

			}
		});

	}

	
	
	private void startAnimation(final View imgView,final ViewAction viewAction,final ViewPosition viewPosition,int duration) {
		Animation[] animc = FlipAnimationFactory.flipAnimation(imgView,viewPosition,viewAction, duration,null);
		animc[0].setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				// TODO Auto-generated method stub
				
				
					setImagesToDefaultPlaces(BOTHIMAGE);
					clearImagesFromImageViews();	
				
				System.out.println("---------------- animation finish");
				
			}
		});
		imgView.startAnimation(animc[0]);
		
	}
	

	private void initMirrorMatrix()
	{
		/*
		 * inverted matrix
		 * 
		 */
		float[] mirrorY = 
			{   1, 0, 0, 
				0, -1,0,  
				0, 0, 1  		
			};
		matrixMirrorY = new Matrix();
		matrixMirrorY.setValues(mirrorY);
	}

	private Bitmap drawMatrix(Bitmap bitmapToBeInverted){
		initMirrorMatrix();
		Matrix matrix = new Matrix();
		matrix.postConcat(matrixMirrorY); 
		Bitmap mirrorBitmap = Bitmap.createBitmap(bitmapToBeInverted, 0, 0, bitmapToBeInverted.getWidth(), bitmapToBeInverted.getHeight(), matrix, true);
		return mirrorBitmap;	 
	}
	private void clearImagesFromImageViews() {
		topCacheImageView.setImageBitmap(null);
		bottomCacheImageView.setImageBitmap(null);

		topCacheImageViewRelativeLayout.setBackgroundDrawable(null);
		bottomCacheImageViewRelativeLayout.setBackgroundDrawable(null);


		
		topCacheImageViewRelativeLayout.setVisibility(View.GONE);
		bottomCacheImageViewRelativeLayout.setVisibility(View.GONE);
		
		
		topCacheImageView.invalidate();
		bottomCacheImageView.invalidate();
		topCacheImageViewRelativeLayout.invalidate();
		bottomCacheImageViewRelativeLayout.invalidate();

		
		
		System.out.println("==clearImagesFromImageViews.======countOfPage===="+countOfPage);
		System.out.println("==clearImagesFromImageViews.============bottomCacheImageView.getHeight()  "+bottomCacheImageView.getHeight());
		System.out.println("==clearImagesFromImageViews.=========topCacheImageView.getHeight() "+topCacheImageView.getHeight());
		System.out.println("==clearImagesFromImageViews.=========topCacheImageViewLayoutParams.height "+topCacheImageViewLayoutParams.height);
		System.out.println("==clearImagesFromImageViews.========bottomCacheImageViewLayoutParams.height "+bottomCacheImageViewLayoutParams.height);
		System.out.println("==clearImagesFromImageViews.========@@=======movingBottom "+moveingBottom);
		System.out.println("==clearImagesFromImageViews.==========distanceY "+distanceYcpy);
		System.out.println("------clearImagesFromImageViews--touchDownPosition-   "+touchDownPosition);
		System.out.println("------clearImagesFromImageViews--countForMotionTest-   "+countForMotionTest);
		
		
		
	}

	private void captureImageAndAssing() {
		topCacheImageViewRelativeLayout.setVisibility(View.VISIBLE);
		bottomCacheImageViewRelativeLayout.setVisibility(View.VISIBLE);

		paperOneFrameLayout.setDrawingCacheEnabled(false);
		paperOneFrameLayout.setDrawingCacheEnabled(true);

		Bitmap bitmapCapture = paperOneFrameLayout.getDrawingCache();

		bitmapCapture = Bitmap.createScaledBitmap(bitmapCapture, displayWidth, displayHeight, false);

		//bitmapCapture = Bitmap.createBitmap(bitmapCapture, 0, 0, displayWidth,displayHeight);
		Bitmap bm1 = Bitmap.createBitmap(bitmapCapture, 0, 0, displayWidth, (bitmapCapture.getHeight() / 2));
		Bitmap bm2 = Bitmap.createBitmap(bitmapCapture, 0, (bitmapCapture.getHeight() / 2),displayWidth, (bitmapCapture.getHeight()/ 2));

		System.out.println("========================== bitmapCapture width  "+bitmapCapture.getWidth());
		System.out.println("========================== bitmapCapture.getHeight()  "+bitmapCapture.getHeight());
		System.out.println("==========================  topCacheImageViewRelativeLayout.getWidth() "+topCacheImageViewRelativeLayout.getWidth());
		System.out.println("==========================  topCacheImageViewRelativeLayout.getHeight() "+topCacheImageViewRelativeLayout.getHeight());
		System.out.println("========================== paperOneFrameLayout.width  "+paperOneFrameLayout.getWidth());
		System.out.println("==========================  paperOneFrameLayout.getHeight() "+paperOneFrameLayout.getHeight());

	
		/*topCacheImageView.setImageBitmap(bm1);
		bottomCacheImageView.setImageBitmap(bm2);*/

		topCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bm1));
		bottomCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bm2));
		
		

		topCacheImageViewRelativeLayout.invalidate();
		bottomCacheImageViewRelativeLayout.invalidate();

//		topCacheImageView.setImageDrawable(null);
//		bottomCacheImageView.setImageDrawable(null);
		
//		topCacheImageView.setImageResource(R.drawable.yellow);
//		bottomCacheImageView.setImageResource(R.drawable.red);

		

		bitmapCapture = null;
		bm1 = null;
		bm2 = null;


	}

	private void captureImageAndAssingBottomToUp(int i) {

		if(i  == 0){
			if(countExOne == 0){
				System.out.println("=======+++++++++++++++++++++++++++++++++++++++++++++BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB+++++++++++++++++++=");
				paperOneFrameLayout.setDrawingCacheEnabled(false);
				paperOneFrameLayout.setDrawingCacheEnabled(true);

				Bitmap bitmapCapture = paperOneFrameLayout.getDrawingCache();
				Bitmap bm1 = Bitmap.createBitmap(bitmapCapture, 0, 0, displayWidth, (bitmapCapture.getHeight() / 2));

				topCacheImageViewLayoutParams = null;
				topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
				topCacheImageViewLayoutParams.topMargin = displayHeight/2;

				topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
				topCacheImageView.setImageBitmap(bm1);

				//topCacheImageView.setImageResource(R.drawable.redbullmotorcycleconcept);
				topCacheImageView.invalidate();

				bitmapCapture = null;
				bm1 = null;

				countExOne++;
			}
		}else{
			if(countExThree == 0){
				System.out.println("=======+++++++++++++++++++++++++++++++++++++++++++++XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxx+++++++++++++++++++=");

				// --------------------- temp code--------
				bottomCacheImageViewRelativeLayout.setDrawingCacheEnabled(false);
				bottomCacheImageViewRelativeLayout.setDrawingCacheEnabled(true);

				Bitmap bitmapCapture2 = bottomCacheImageViewRelativeLayout.getDrawingCache();
				Bitmap bm1 = Bitmap.createBitmap(bitmapCapture2, 0, 0, displayWidth, (bitmapCapture2.getHeight()));

				bottomCacheImageViewLayoutParams = null;
				bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);

				bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
				bottomCacheImageView.setImageDrawable(bottomCacheImageViewRelativeLayout.getBackground());
				bottomCacheImageView.invalidate();
				// --------------------- temp code--------

				paperOneFrameLayout.setDrawingCacheEnabled(false);
				paperOneFrameLayout.setDrawingCacheEnabled(true);

				Bitmap bitmapCapture1 = paperOneFrameLayout.getDrawingCache();
				//Bitmap bm2 = Bitmap.createBitmap(bitmapCapture1, 0, 0, displayWidth, (bitmapCapture1.getHeight() / 2));
				Bitmap bm2 = 	Bitmap.createBitmap(bitmapCapture1, 0, (bitmapCapture1.getHeight() / 2),displayWidth, (bitmapCapture1.getHeight()/ 2));
				bottomCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bm2));
				//bottomCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bitmapCapture2));
				bottomCacheImageViewRelativeLayout.invalidate();
				
				
				
				topCacheImageViewLayoutParams = null;
				topCacheImageViewLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, 0);
				topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
				topCacheImageView.invalidate();
				countExThree++;
			}
		}
	}


	private void captureImageAndAssingUpToBottom(int i) {


		if(i == 0){
			if(countExTwo == 0){
				System.out.println("=======++++++++++++++++++++++++++++++++++++++++555555555555555555555555555555555555555+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");

				paperOneFrameLayout.setDrawingCacheEnabled(false);
				paperOneFrameLayout.setDrawingCacheEnabled(true);

				Bitmap bitmapCapture = paperOneFrameLayout.getDrawingCache();
				Bitmap bm2 = Bitmap.createBitmap(bitmapCapture, 0, (bitmapCapture.getHeight() / 2),displayWidth, (bitmapCapture.getHeight() / 2));

				bottomCacheImageViewLayoutParams = null;
				bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT,0);
				bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
			//	bottomCacheImageView.setImageResource(R.drawable.red);
			
				bottomCacheImageView.setImageBitmap(bm2);
				bottomCacheImageView.invalidate();
				bottomCacheImageViewRelativeLayout.invalidate();

				bitmapCapture = null;
				bm2 = null;
				
				System.out.println("----------- LLLLLLLLLLLLLLLLOOOOOO  "+bottomCacheImageViewLayoutParams.height);
				//bottomCacheImageView.setImageResource(R.drawable.redbullmotorcycleconcept);
//				paperOneFrameLayout.setDrawingCacheEnabled(false);
//				paperOneFrameLayout.setDrawingCacheEnabled(true);
//
//				Bitmap bitmapCapture1 = paperOneFrameLayout.getDrawingCache();
//				Bitmap bm3 = Bitmap.createBitmap(bitmapCapture1, 0, (bitmapCapture1.getHeight() / 2),displayWidth, (bitmapCapture1.getHeight()/ 2));
//
//				bottomCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bm2));
//				bottomCacheImageViewRelativeLayout.invalidate();
//				//bottomCacheImageView.setImageResource(R.drawable.redbullmotorcycleconcept);
//
//				bitmapCapture1 = null;
//				bm3 = null;
				
				countExTwo++;
			}

		}else{
			if(countExFour  == 0){
				System.out.println("=======++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++AAAAAAAAAAAAAAAAAAAa66666666666+++++++++++++++++++++++++++++++++++++++++++=");

				/*
				 * this section capture the screenshot of layer 3 n assign it to layer 2 bkground n 
				 * layer 2's bkgroung is captured and assigned to topimageview's bkground
				 * 
				 */

				// --------------------- temp code--------
				topCacheImageViewRelativeLayout.setDrawingCacheEnabled(false);
				topCacheImageViewRelativeLayout.setDrawingCacheEnabled(true);
				
				Bitmap bitmapCapture2 = topCacheImageViewRelativeLayout.getDrawingCache();
				Bitmap bm1 = Bitmap.createBitmap(bitmapCapture2, 0, 0, displayWidth, (bitmapCapture2.getHeight()));

				topCacheImageViewLayoutParams = null;
				topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
				topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);

				topCacheImageView.setImageDrawable(topCacheImageViewRelativeLayout.getBackground());
				//topCacheImageView.setImageResource(R.drawable.yellow);
				topCacheImageView.invalidate();
				
		
				
				
				// --------------------- temp code--------

				paperOneFrameLayout.setDrawingCacheEnabled(false);
				paperOneFrameLayout.setDrawingCacheEnabled(true);

				Bitmap bitmapCapture1 = paperOneFrameLayout.getDrawingCache();
				Bitmap bm2 = Bitmap.createBitmap(bitmapCapture1, 0, 0, displayWidth, (bitmapCapture1.getHeight() / 2));

				topCacheImageViewRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bm2));
				topCacheImageViewRelativeLayout.invalidate();
				
								
				bottomCacheImageViewLayoutParams = null;
				bottomCacheImageViewLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, 0);
				bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
//				bottomCacheImageView.setImageBitmap(bm1);
				bottomCacheImageView.invalidate();
					
			
				
				bitmapCapture1 = null;
				bm2 = null;

				countExFour++;
			}
		}
	}



	private void setImagesToDefaultPlaces(String view){
		paperOneFrameLayout.setDrawingCacheEnabled(false);
		if(view.equalsIgnoreCase(BOTTOMIMAGE)){
			bottomCacheImageViewLayoutParams = null;
			bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);
			bottomCacheImageViewLayoutParams.addRule(RelativeLayout.BELOW,topCacheImageView.getId());
			bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
		}else if(view.equalsIgnoreCase(TOPIMAGE)){
			topCacheImageViewLayoutParams = null;
			topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);
			topCacheImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
		}else if(view.equalsIgnoreCase(BOTHIMAGE)){
			topCacheImageViewLayoutParams = null;
			bottomCacheImageViewLayoutParams = null;

			topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);
			bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, displayHeight/2);

			topCacheImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			bottomCacheImageViewLayoutParams.addRule(RelativeLayout.BELOW,topCacheImageView.getId());

			topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
			bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);	


			topCacheImageViewRelativeLayout.setBackgroundDrawable(null);
			bottomCacheImageViewRelativeLayout.setBackgroundDrawable(null);
			
			topCacheImageView.invalidate();
			bottomCacheImageView.invalidate();
			topCacheImageViewRelativeLayout.invalidate();
			bottomCacheImageViewRelativeLayout.invalidate();

		}

	}

	private void getScreenDimensions() {
		display = ((WindowManager) _context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		/*if(_context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getHeight()+ 48;// where 48 is sum of above and below action bar
			displayHeight = display.getWid;
		}else*/{
			displayWidth = display.getWidth();
			displayHeight = display.getHeight();
		}

	}

	private void createCaptureFrame(){
		
		captureFrame = new RelativeLayout(_context);
		topCacheImageViewRelativeLayout = new RelativeLayout(_context);
		bottomCacheImageViewRelativeLayout = new RelativeLayout(_context);




		topCacheImageView = new ImageView(_context);
		bottomCacheImageView = new ImageView(_context);

		topCacheImageView.setScaleType(ScaleType.FIT_XY);
		bottomCacheImageView.setScaleType(ScaleType.FIT_XY);

		topCacheImageViewRelativeLayout.setId(topImgID);
		bottomCacheImageViewRelativeLayout.setId(bottomImgID);

		LayoutParams captureFrameLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		captureFrame.setLayoutParams(captureFrameLayoutParams);

		LayoutParams topCacheImageViewRelativeLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, (displayHeight/2)-20);
		topCacheImageViewRelativeLayout.setLayoutParams(topCacheImageViewRelativeLayoutParams);


		LayoutParams bottomCacheImageViewRelativeLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, (displayHeight/2));
		bottomCacheImageViewRelativeLayout.setLayoutParams(bottomCacheImageViewRelativeLayoutParams);


		topCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		bottomCacheImageViewLayoutParams  = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);



		/*topCacheImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		bottomCacheImageViewLayoutParams.addRule(RelativeLayout.BELOW,topCacheImageView.getId());*/


		//topCacheImageViewRelativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP, RelativeLayout.TRUE);
		//bottomCacheImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);

		bottomCacheImageViewRelativeLayoutParams.addRule(RelativeLayout.BELOW,topCacheImageViewRelativeLayout.getId());




		topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
		bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);



		topCacheImageViewRelativeLayout.addView(topCacheImageView);
		bottomCacheImageViewRelativeLayout.addView(bottomCacheImageView);


		/*topCacheImageView.setBackgroundResource(R.drawable.news2);
		bottomCacheImageView.setBackgroundResource(R.drawable.news1);*/


		captureFrame.addView(topCacheImageViewRelativeLayout);
		captureFrame.addView(bottomCacheImageViewRelativeLayout);
		

	}



	class MyGestureDetector extends SimpleOnGestureListener{





		@Override
		public boolean onDown(MotionEvent e) {

			return super.onDown(e);
		}



		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,	float distanceX, float distanceY) {
			// TODO Auto-generated method stub

			topCacheImageViewRelativeLayout.invalidate();
			
			//paperOneFrameLayout.setBackgroundResource(R.drawable.redbullmotorcycleconcept);
		
			
			System.out.println("--------------  distanceY "+distanceY);
			if(e2.getY() > touchDownPosition && countForMotionTest > 0){
				moveingBottom = false;
			}else if(e2.getY() < touchDownPosition && countForMotionTest > 0){
	 	// need some changes here TODO
				moveingBottom = true;
			}else{
				return false ;
//				if(moveingBottom){
//					moveingBottom = false;
//				}else{
//					moveingBottom = true;
//				}
			}
			
			
			System.out.println("-----------------------  topCacheImageViewLayoutParams.topMargin "+topCacheImageViewLayoutParams.topMargin);
			System.out.println("-----------------------  bottomCacheImageViewLayoutParams.height "+bottomCacheImageViewLayoutParams.height);
			System.out.println("==============================   movingBottom   "+moveingBottom);
			distanceYcpy = distanceY;
			//updirection
			if(moveingBottom) {
				
				System.out.println("============================= +==================+++++++++=======+++++++++++++++++++=+++===================$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$   ");

				if(distanceY > 0 ){
					captureImageAndAssingBottomToUp(1);
					if(bottomCacheImageViewLayoutParams.height <= 0 ){
						bottomCacheImageViewLayoutParams.height = bottomCacheImageView.getHeight();
					}
					System.out.println("=============================  up1   "+	bottomCacheImageViewLayoutParams.height);
					if(bottomCacheImageView.getHeight()-10 > 1) {
						System.out.println("=============UP=================   3 ");
						System.out.println("=============UP=================   3  if if ifi ");
						bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height -20;
						bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
						bottomCacheImageView.invalidate();
					}else{
						System.out.println("=============UP======AAAAAAAAAAAA===========   3 "+topCacheImageViewLayoutParams.topMargin);
						bottomCacheImageViewLayoutParams.height = 0;
						bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
						captureImageAndAssingBottomToUp(0);
						if(topCacheImageViewLayoutParams.topMargin >= 0){
							topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin - 20;
							topCacheImageViewLayoutParams.bottomMargin = 0;
							topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
						}
					}
				}else{
					System.out.println("=============================  up AA2   ");
					if(topCacheImageViewLayoutParams.topMargin  <= topCacheImageViewRelativeLayout.getHeight() && bottomCacheImageView.getHeight() <= 0){
						topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin + 20;
						topCacheImageViewLayoutParams.bottomMargin = 0;
						topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
					}else{
						System.out.println("================================== upAA3    "+bottomCacheImageViewLayoutParams.height);
						if(bottomCacheImageViewLayoutParams.height <= bottomCacheImageViewRelativeLayout.getHeight()){
							bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height + 20;
							bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
							bottomCacheImageView.invalidate();
						}
					}
				}
			
			}else  
			{
//// downdirection
//
				System.out.println("========999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999=====================  distanceY   "+distanceY);
				if(distanceY > 0 ){
					//D2				
					System.out.println("=======rahul============   DOWN ===topCacheImageView.height=  1 " +topCacheImageViewLayoutParams.topMargin);

					//if(bottomCacheImageViewLayoutParams.height <= 0){
					if(topCacheImageView.getHeight() >= 0 && !(bottomCacheImageViewLayoutParams.height <= 0)){
						System.out.println("=========="+bottomCacheImageViewRelativeLayout.getHeight()+"======== A! bottomCacheImageView.getHeight()  "+bottomCacheImageView.getHeight());
						if(topCacheImageViewLayoutParams.topMargin > 0 && topCacheImageViewLayoutParams.topMargin < topCacheImageViewRelativeLayout.getHeight() ){
							System.out.println("=========== AAAAAAAAAA");
							if(bottomCacheImageView.getHeight() <= bottomCacheImageViewRelativeLayout.getHeight()-10 ){
								System.out.println("=========== A!!!!!");
								bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height - 10;
								bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
							}else if (bottomCacheImageView.getHeight() <= bottomCacheImageViewRelativeLayout.getHeight()){
								//captureImageAndAssingUpToBottom(0);
								System.out.println("=========== A2222");
								topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin - 10;
								topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
								
//								bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height +10;
//								bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
							}else{
								System.out.println("=========== A3333");

								topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin - 10;
								topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
							}
						}else{
							System.out.println("=========== BBBBBBBBBB");
							if(bottomCacheImageViewLayoutParams.height > 0){
								bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height - 10;
								bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
							}
						}
					}else{
						System.out.println("====DOWN======PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPp==="+topCacheImageViewLayoutParams.topMargin);
						//if(!(topCacheImageViewLayoutParams.topMargin >= topCacheImageViewRelativeLayout.getHeight()-50)){
						System.out.println("=DOWN=========1121222222222222222222222222222==="+topCacheImageViewRelativeLayout.getHeight());

						//						if(bottomCacheImageViewLayoutParams.height > 0){
						//							System.out.println("=DOWN=========333333333333333333333333333333333333===");
						//							bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height - 10;
						//							bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);
						//						}
						//						if(topCacheImageViewLayoutParams.topMargin <= topCacheImageViewRelativeLayout.getHeight()){
						if(topCacheImageViewLayoutParams.topMargin >= 0){
							System.out.println("===================   DOWN ====AAAA  3====if");
							topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin - 10;
							topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
						}
						//}

						//}
					}

				}else
				{
					//D1
					System.out.println("=================== D1  DOWN ====  3==== "+ topCacheImageViewLayoutParams.topMargin);
					captureImageAndAssingUpToBottom(1); 
					if(topCacheImageViewLayoutParams.topMargin+10 < topCacheImageViewRelativeLayout.getHeight()+10  ){
						
						//D3
						System.out.println("=================== D3  DOWN ====  3====if");
						topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin + 10;
						topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
					}else{
						//D4
						System.out.println("===================D4   DOWN ====  4 " );
						captureImageAndAssingUpToBottom(0);
						if(bottomCacheImageViewLayoutParams.height <= bottomCacheImageViewRelativeLayout.getHeight()){
							bottomCacheImageViewLayoutParams.height = bottomCacheImageViewLayoutParams.height +10;
							bottomCacheImageView.setLayoutParams(bottomCacheImageViewLayoutParams);

						}
					}




				}
			}

			return false ;
		}

	}

	class SimpleThread extends Thread {
		boolean upwardMove;
		public SimpleThread(boolean upward) {
			super();
			upwardMove = upward;
		}

		public void run() {

			try {

				if(upwardMove){
					while (topCacheImageViewLayoutParams.topMargin >= 0){
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
						sleep(10);
					}
				}else{
					while (topCacheImageViewLayoutParams.topMargin <= topCacheImageViewRelativeLayout.getHeight()){
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						sleep(10);
					}
				}
				Message msgReset = new Message();
				msgReset.what = 1;
				handler.sendMessage(msgReset);

			} catch (InterruptedException e) {

			}
		}

	}


	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				if(topCacheImageView.getHeight() >topCacheImageViewLayoutParams.height ){
					topCacheImageViewLayoutParams.height = topCacheImageView.getHeight();
				}
				topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin-10;
				topCacheImageViewLayoutParams.height = topCacheImageViewLayoutParams.height +10;
				topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);

				topCacheImageView.invalidate();
			}else if (msg.what == 1){
				
				//setImagesToDefaultPlaces(BOTHIMAGE);
				
				setImagesToDefaultPlaces(BOTTOMIMAGE);
				
				clearImagesFromImageViews();

			}else if (msg.what == 2){

				topCacheImageViewLayoutParams.topMargin = topCacheImageViewLayoutParams.topMargin +10;
				topCacheImageView.setLayoutParams(topCacheImageViewLayoutParams);
				topCacheImageView.invalidate();

			}
		}
	};

}
