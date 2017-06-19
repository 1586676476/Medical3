package com.witnsoft.interhis.mainpage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import com.witnsoft.interhis.Chufang.ChuFangChinese;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.db.model.ChineseDetailModel;
import com.witnsoft.interhis.inter.DialogListener;
import com.witnsoft.libnet.model.DataModel;
import com.witnsoft.libnet.model.OTRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WritePadDialog extends Dialog {
	private static final String TAG = "WritePadDialog";
	private Activity act;
	Context context;
	LayoutParams p;
	DialogListener dialogListener;
	private OTRequest otRequest;
	List<ChineseDetailModel> list=new ArrayList<>();
	String str,acsm,zdsm,aiid;

	private static final String TN_DOC_KAIYAO = "F27.APP.01.06";

	public WritePadDialog(List<ChineseDetailModel> list,String str,String acsm,String zdsm,String aiid,Activity a,Context context, int themeResId, DialogListener dialogListener) {
		super(context,themeResId);
		this.aiid=aiid;
		this.zdsm=zdsm;
        this.acsm=acsm;
		this.str=str;
		this.list=list;
		this.act=a;
		this.context = context;
		this.dialogListener = dialogListener;
	}

	static final int BACKGROUND_COLOR = Color.WHITE;
	static final int BRUSH_COLOR = Color.BLACK;
	PaintView mView;
	/** The index of the current color to use. */
	int mColorIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.write);
		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.x=190;
		p.y=130;
		p.height = 500; //高度设置为屏幕的0.4
		p.width = 880; //宽度设置为屏幕的0.6
		p.alpha=0.8f;
		int statusBarHeight = getStatusBarHeight(context);
//		p.height = (int)(height - statusBarHeight + 0.5);   //减去系统的宽高
//		p.width = (int)(width + 0.5);
		getWindow().setAttributes(p); // 设置生效,全屏填充
		mView = new PaintView(context);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
		frameLayout.addView(mView);
		mView.requestFocus();
		Button btnClear = (Button) findViewById(R.id.tablet_clear);
		btnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mView.clear();
			}
		});
		Button btnOk = (Button) findViewById(R.id.tablet_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dialogListener.refreshActivity(mView.getCachebBitmap());
					WritePadDialog.this.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

				//生成json串 并上传服务器
				final ChuFangChinese chufang=new ChuFangChinese();
				chufang.fromJSON(list,str,acsm,zdsm,aiid);
                otRequest=new OTRequest(getContext());
				otRequest.setTN(TN_DOC_KAIYAO);
				DataModel data = new DataModel();
				data.setDataJSONStr(String.valueOf(chufang.fromJSON(list,str,acsm,zdsm,aiid)));
				otRequest.setDATA(data);
				NetTool.getInstance().startRequest(false, true , act , null, otRequest, new CallBack<Map, String>() {
					@Override
					public void onSuccess(Map map, String s) {
						Intent intent=new Intent("CHUSHIHUA");
						getContext().sendBroadcast(intent);
						Log.e(TAG, "onSuccess: "+chufang.fromJSON(list,str,acsm,zdsm,aiid) );
					}

					@Override
					public void onError(Throwable throwable) {
						Log.e(TAG, "onError!!!!!!!!!!!!!: "+"请求失败" );
					}
				});

			}
		});

	}

	/**
	 * 获取通知栏的高度
	 * @param context
	 * @return
	 */
	public int getStatusBarHeight(Context context){
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * This view implements the drawing canvas.
	 *
	 * It handles all of the input events and drawing functions.
	 */
	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(10);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, (int) (p.height * 0.8),
					Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			//设置背景为白色，不然点击缩略图查看的时候是全黑色
			//cacheCanvas.drawColor(R.color.transparent);
		}

		public void clear() {
			if (cacheCanvas != null) {
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}
			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;
			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					cur_x = x;
					cur_y = y;
					path.moveTo(cur_x, cur_y);
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
					break;
				}
				case MotionEvent.ACTION_UP: {
					cacheCanvas.drawPath(path, paint);
					path.reset();
					break;
				}
			}
			invalidate();
			return true;
		}
	}
}




