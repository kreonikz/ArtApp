package org.kwang.art.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.kwang.art.direction.ActivityMainDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/1/14.
 */
public class WheelView extends ScrollView {
    public int i;


    public static final String TAG = WheelView.class.getSimpleName();



    public static class OnWheelViewListener {
        public void onSelected(int selectedIndex, String item) {
        }
    }


    private Context context;
//    private ScrollView scrollView;

    private LinearLayout views;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    //    String[] items;
    List<String> items;

    private List<String> getItems() {
        return items;
    }

    public void setItems(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);

        // 前面和后面补全 передняя и задняя комплементаё
        for (int i = 0; i < offset-1; i++) {
            items.add(0, "");
            items.add("");
        }

        initData();

    }


    public static final int OFF_SET_DEFAULT = 1;

    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全） Смещение (комплемент требуется в передней и дальней)

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) { //сколько итемов будет видно - установить смещение
        this.offset = offset;
    }

    int displayItemCount; // 每页显示的数量 выводить по

    int selectedIndex = 1;// не влияет на первый клик итема


    private void init(Context context) {
        this.context = context;

//        scrollView = ((ScrollView)this.getParent());
//        Log.d(TAG, "scrollview: " + scrollView);
        Log.d(TAG, "parent: " + this.getParent());
//        this.setOrientation(VERTICAL);
        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        views.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {

                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
//                    Log.d(TAG, "initialY: " + initialY);
//                    Log.d(TAG, "remainder: " + remainder + ", divided: " + divided);
                    if (remainder == 0) {
                        selectedIndex = divided + offset; //Выбранный индекс

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }


                    }


                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };


    }

    int initialY;

    Runnable scrollerTask;
    int newCheck = 50;

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }




    private void initData() {
        displayItemCount = offset * 2 + 1;

        for (String item : items) {
            views.addView(createView(item));

        }

        refreshItemView(0);
    }





    int itemHeight = 0;





    private TextView createView(String item) { //Textview создаю вьюшку в списке
        TextView tv = new TextView(context);
        views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (item.length()!=0){

            SpannableString ss1=  new SpannableString(item);
            ss1.setSpan(new RelativeSizeSpan(0.6f), 0,5, 0); // set size
            //ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, 0);// set color
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));



            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            //            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            tv.setSingleLine(false);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setText(ss1);
            tv.setGravity(Gravity.LEFT);

        } else {


            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setSingleLine(false);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
            tv.setTypeface(null, Typeface.ITALIC);
            tv.setText(item);

            tv.setGravity(Gravity.CENTER);


        }
        views.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int padding = dip2px(20);

        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            Log.d(TAG, "itemHeight: " + itemHeight);
            views.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

//            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));

//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.getLayoutParams();
//            this.setLayoutParams(new RelativeLayout.LayoutParams(lp.width, itemHeight * displayItemCount));



        }
        return tv;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

//        try {
//            Field field = ScrollView.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            OverScroller mScroller = (OverScroller) field.get(this);
//
//
//            if(mScroller.isFinished()){
//                Log.d(TAG, "isFinished...");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        refreshItemView(t);

        if (t > oldt) {
//            Log.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
//            Log.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }


    }



    private void refreshItemView(int y) {

        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }

//            if(remainder > itemHeight / 2){
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, ">down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, ">up...position: " + position);
//                }
//            }else{
////                position = y / itemHeight + offset;
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, "<down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, "<up...position: " + position);
//                }
//            }
//        }

//        if(scrollDirection == SCROLL_DIRECTION_DOWN){
//            position = divided + offset;
//        }else if(scrollDirection == SCROLL_DIRECTION_UP){
//            position = divided + offset + 1;
        }

        int childSize = views.getChildCount();




        for ( i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i); //Надпись



            itemView.setOnClickListener(new OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override

                public void onClick(View v) {

                    Intent intent = null;
                    String s = getSeletedItem();

                    if (s.equals("1850   Реализм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","0");
                    }

                    else if (s.equals("1860   Импрессионизм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","1");



                    } else if (s.equals("1880   Пост"+"\n"+"        -импрессионизм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","2");



                    } else if (s.equals("1910   Кубизм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","3");



                    } else if  (s.equals("1915   Экспрессионизм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","4");




                    } else if  (s.equals("1920   Сюрреализм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","5");


                    }  else if  (s.equals("1930   Абстракционизм")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","6");



                    }  else if  (s.equals("1950   Абстрактная"+"\n"+"            Живопись")) {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","7");




                    } else if (s.equals("1960   Поп арт"))
                    {
                        intent = new Intent(context, ActivityMainDirection.class);
                        intent.putExtra("Number_in_list","8");
                    }

                    context.startActivity(intent);
                }
            });



            if (null == itemView) {
                return;
            }



            if (position == i) {
                itemView.setTextColor(Color.parseColor("#000000"));// Текущий цвет Черныыыйййй


            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));//Цвет остальных Серый
            }
        }




    }




    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }


    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;

    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#000000"));
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };


        super.setBackgroundDrawable(background);

    }







    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        //setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset+2; // Вот тут я выбранный индекс увеличил на 2 так как после SetOffset меняется SelectedIndex
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override


    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }






    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

}







//        for ( i = 0; i < childSize; i++) {
//            TextView itemView = (TextView) views.getChildAt(i); //Надпись
//
//
//
//            itemView.setOnClickListener(new OnClickListener() {
//                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//                @Override
//
//                public void onClick(View v) {
//
//                    Intent intent = null;
//                    String s = getSeletedItem();
//
//                    if (s.equals("1850   Реализм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","0");
//                    }
//
//                    else if (s.equals("1860   Импрессионизм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","1");
//
//
//
//                    } else if (s.equals("1880   Пост"+"\n"+"        -импрессионизм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","2");
//
//
//
//                    } else if (s.equals("1910   Кубизм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","3");
//
//
//
//                    } else if  (s.equals("1915   Экспрессионизм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","4");
//
//
//
//
//                    } else if  (s.equals("1920   Сюрреализм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","5");
//
//
//                    }  else if  (s.equals("1930   Абстракционизм")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","6");
//
//
//
//                    }  else if  (s.equals("1950   Абстрактная"+"\n"+"            Живопись")) {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","7");
//
//
//
//
//                    } else if (s.equals("1960   Поп арт"))
//                    {
//                        intent = new Intent(context, ActivityMainDirection.class);
//                        intent.putExtra("Number_in_list","8");
//                    }
//
//                    context.startActivity(intent);
//                }
//            });
//
//
//
//            if (null == itemView) {
//                return;
//            }
//
//
//
//            if (position == i) {
//                itemView.setTextColor(Color.parseColor("#000000"));// Текущий цвет Черныыыйййй
//
//
//            } else {
//                itemView.setTextColor(Color.parseColor("#bbbbbb"));//Цвет остальных Серый
//            }
//        }
