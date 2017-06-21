package com.oddshou.testall.animation;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddshou.testall.Logger;
import com.oddshou.testall.R;

import java.util.ArrayList;

/**
 * Created by oddshou on 2017/6/19.
 */

public class GridLayoutAnimation extends Activity implements View.OnLongClickListener,
        View.OnClickListener, View.OnDragListener {
    private static final String TAG = "GridLayoutAnimation";
    LinearLayout rootLayout;
    static final String[] CHANNELS_CHOOSED = {"要闻", "视频", "广东", "娱乐", "体育",
            "要闻2", "视频2", "广东2", "娱乐2", "体育2",
            "要闻3", "视频3", "广东3", "娱乐3", "体育3",
            "要闻4", "视频4", "广东4", "娱乐4", "体育4",

    };

    static final String[] CHANNELS_UNCHOOSED = {"宠物", "纪录片", "文化", "动漫", "股票",
            "宠物2", "纪录片2", "文化2", "动漫2", "股票2",
            "宠物3", "纪录片3", "文化3", "动漫3", "股票3",
            "宠物4", "纪录片4", "文化4", "动漫4", "股票4",

    };
    private GridLayout gridLayoutUnChoosed;
    private GridLayout gridLayoutChoosed;
    private ArrayList<BtnData> groupDataChoosed;
    private ArrayList<BtnData> groupDataUnChoosed;
    private LayoutTransition mTransitionerTop;
    private LayoutTransition mTransitionerBottom;
    //内部调换位置
    private boolean choosedChange;
    private int choosedChangeId;
    private View moveView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        rootLayout = (LinearLayout) findViewById(R.id.container);
        createPage();
    }

    protected void createPage() {
        //尝试过设置同一个LayoutTransition，发现会有错乱，大概是因为
        //动画是两块的，同步执行会有问题。
        mTransitionerTop = new LayoutTransition();
        mTransitionerBottom = new LayoutTransition();
        setupCustomAnimations();

        //1.titile 已选频道
        TextView chooseTitle = new TextView(this);
        chooseTitle.setWidth(200);
        chooseTitle.setHeight(100);
        chooseTitle.setText("已选频道");

        rootLayout.addView(chooseTitle, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //2.buttons 已选频道
        gridLayoutChoosed = new GridLayout(this);

        gridLayoutChoosed.setColumnCount(4);
        gridLayoutChoosed.setLayoutTransition(mTransitionerTop);
        groupDataChoosed = new ArrayList<BtnData>();
        createBtns(CHANNELS_CHOOSED, gridLayoutChoosed, groupDataChoosed);
        rootLayout.addView(gridLayoutChoosed);
        //3.title 推荐频道
        TextView unChoosedTitle = new TextView(this);
        unChoosedTitle.setWidth(200);
        unChoosedTitle.setHeight(100);
        unChoosedTitle.setText("未选频道");

        rootLayout.addView(unChoosedTitle, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        //4.buttons 推荐频道
        gridLayoutUnChoosed = new GridLayout(this);
        gridLayoutUnChoosed.setColumnCount(4);
        gridLayoutUnChoosed.setLayoutTransition(mTransitionerBottom);
        groupDataUnChoosed = new ArrayList<BtnData>();
        createBtns(CHANNELS_UNCHOOSED, gridLayoutUnChoosed, groupDataUnChoosed);
        rootLayout.addView(gridLayoutUnChoosed);

    }

    private LayoutTransition.TransitionListener mTransitionListner = new LayoutTransition.TransitionListener() {
        int[] locationSrc = new int[2];

        @Override
        public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            if (transitionType == LayoutTransition.DISAPPEARING) {
                view.getLocationInWindow(locationSrc);
            } else if (transitionType == LayoutTransition.APPEARING) {
                //点击.由上到下 终点为下面第一个。
                //由下到上，终点为上面最后一个。
                int[] locationDst = new int[2];
                if (!choosedChange) {

                    if (transition == mTransitionerTop) {
                        //由下到上
                        getLocation(locationDst);
                    } else if (transition == mTransitionerBottom) {
                        //由上到下
                        View dstView = gridLayoutUnChoosed.getChildAt(0);
                        dstView.getLocationInWindow(locationDst); //这里获取得到0，0，改用其他方式获取
                    }else {
                        return;
                    }
                }else {
                    View dstView = gridLayoutChoosed.getChildAt(choosedChangeId);
                    dstView.getLocationInWindow(locationDst); //这里获取得到0，0，改用其他方式获取
                    choosedChange = false;
                }


                PropertyValuesHolder pvhTransX =
                        PropertyValuesHolder.ofFloat("translationX", locationSrc[0] - locationDst[0], 0f);
                PropertyValuesHolder pvhTransY =
                        PropertyValuesHolder.ofFloat("translationY", locationSrc[1] - locationDst[1], 0f);
                final ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(
                        this, pvhTransX, pvhTransY).
                        setDuration(transition.getDuration(LayoutTransition.APPEARING));
                transition.setAnimator(LayoutTransition.APPEARING, animIn);
            }
        }

        @Override
        public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

        }

        private void getLocation(int[] location) {
            //1.计算新增控件行列
            int childCount = gridLayoutChoosed.getChildCount();
            int columnCount = gridLayoutChoosed.getColumnCount();
            int row = (int)Math.ceil(( childCount + 1.0 )/columnCount + 0.5);
            int column = (childCount + 1) % columnCount;
            if (column > 1) {
                View columnBefor = gridLayoutChoosed.getChildAt(childCount - 1);
                columnBefor.getLocationInWindow(location);
                location[0] += columnBefor.getWidth();  //这里理论上还需要加上一些margin left,right
            }else {
                //这里认为已选频道大于1
                View columnAbove = gridLayoutChoosed.getChildAt(childCount - columnCount);
                columnAbove.getLocationInWindow(location);
                location[1] += columnAbove.getHeight(); //同样这里也是不准确的
            }

        }
    };

    private void setupCustomAnimations() {
        //把延时去掉，速度瞬间提升
//        mTransitionerTop.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
//        mTransitionerTop.setStartDelay(LayoutTransition.APPEARING, 0);
        mTransitionerTop.addTransitionListener(mTransitionListner);
        mTransitionerTop.setAnimator(LayoutTransition.DISAPPEARING, null);
        mTransitionerTop.setAnimator(LayoutTransition.APPEARING, null);
        mTransitionerTop.setInterpolator(LayoutTransition.APPEARING, new LinearInterpolator());
        mTransitionerTop.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        mTransitionerTop.setStartDelay(LayoutTransition.APPEARING, 0);

//        mTransitionerBottom.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
//        mTransitionerBottom.setStartDelay(LayoutTransition.APPEARING, 0);
        mTransitionerBottom.addTransitionListener(mTransitionListner);
        mTransitionerBottom.setAnimator(LayoutTransition.DISAPPEARING, null);
        mTransitionerBottom.setAnimator(LayoutTransition.APPEARING, null);
        mTransitionerBottom.setInterpolator(LayoutTransition.APPEARING, new LinearInterpolator());
        mTransitionerBottom.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        mTransitionerBottom.setStartDelay(LayoutTransition.APPEARING, 0);

    }

    protected void createBtns(String[] titles, ViewGroup rootView, ArrayList<BtnData> groupList) {
        for (int i = 0; i < titles.length; i++) {
            Button btn = new Button(this);
            btn.setOnLongClickListener(this);
            btn.setOnClickListener(this);
            btn.setOnDragListener(this);
            btn.setWidth(180);
            btn.setText(titles[i]);
            BtnData btnData = new BtnData(i, titles[i], titles == CHANNELS_CHOOSED);

            btn.setTag(btnData);
            groupList.add(btnData);
            rootView.addView(btn);
        }
    }


    @Override
    public boolean onLongClick(View v) {
//        v.startDragAndDrop()  api level 24,好吧算我输
        ClipData dragData = ClipData.newPlainText("clipdata", "clipString");
//        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
        View.DragShadowBuilder dragShadowBuilder = new MyShadow(v);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(dragData, dragShadowBuilder, null, 0);
        }else {
            v.startDrag(dragData, dragShadowBuilder, null, 0);
        }
//        v.setEnabled(false);
        moveView = v;




        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {


        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                //开始拖动,所有监听控件都会收到
                return !(v == moveView);
            case DragEvent.ACTION_DRAG_ENTERED:
                Logger.i(TAG, "onDrag: " + event.getAction() + " " + ((BtnData)v.getTag()).title , "oddshou");
                //进入目标区域，执行变换
                BtnData tag = (BtnData) v.getTag();
                if (tag.choosed) {
                    int k = -1; //终点
                    int j = -1; //起点
                    for (int i = 0; i < gridLayoutChoosed.getChildCount(); i++) {
                        View child = gridLayoutChoosed.getChildAt(i);
                        if (child == v) {
                            k = i;
                        }
                        if (child == moveView) {
                            j = i;
                        }
                        if (k >= 0 && j >= 0) {
                            break;
                        }
                    }
                    choosedChange = true;
                    choosedChangeId =  k > j ? k-1 : k;
                    //如果起点小于终点，计算终点坐标时减1，大于终点不减
                    gridLayoutChoosed.removeView(moveView);
                    moveView.setLayoutParams(new GridLayout.LayoutParams());
                    gridLayoutChoosed.addView(moveView, k);
                }
                return false;
            case DragEvent.ACTION_DRAG_LOCATION:
                Logger.i(TAG, "onDrag: " + event.getAction(), "oddshou");
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                //移除目标区域
                Logger.i(TAG, "onDrag: " + event.getAction() + " " + ((BtnData)v.getTag()).title, "oddshou");
                return true;
            case DragEvent.ACTION_DROP:
                Logger.i(TAG, "onDrag: " + event.getAction() + " " + ((BtnData)v.getTag()).title, "oddshou");
                //目标区域放下
                BtnData tag2 = (BtnData) v.getTag();
                if (tag2.choosed) {
                    //还原到终点位置
                }else {
                    //进入下方第一个位置

                }

                return true;
            case DragEvent.ACTION_DRAG_ENDED:
//                if (moveView == v) {
//                    moveView.setEnabled(true);
//                }
                return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        //已选频道点击移动到未选频道
        //未选频道点击移动到已选频道
        BtnData tag = (BtnData) v.getTag();
        if (tag.choosed) {
            gridLayoutChoosed.removeView(v);
            //这里如果不添加新的btn 会有问题，原因似乎是原btn有一些坐标属性
            //导致添加到新的父控件计算位置有误
            v.setLayoutParams(new GridLayout.LayoutParams());
            gridLayoutUnChoosed.addView(v, 0);

            tag.choosed = false;
            if (groupDataChoosed.contains(tag)) {
                groupDataChoosed.remove(tag);
            }
            if (!groupDataUnChoosed.contains(tag)) {
                groupDataUnChoosed.add(tag);
            }
        } else {
            gridLayoutUnChoosed.removeView(v);
            v.setLayoutParams(new GridLayout.LayoutParams());
            gridLayoutChoosed.addView(v);
            tag.choosed = true;
            if (!groupDataChoosed.contains(tag)) {
                groupDataChoosed.add(tag);
            }
            if (groupDataUnChoosed.contains(tag)) {
                groupDataUnChoosed.remove(tag);
            }
        }
    }

    @NonNull
    private Button getButton(BtnData tag) {
        Button btn = new Button(this);

        btn.setOnLongClickListener(this);
        btn.setOnClickListener(this);
        btn.setWidth(180);
        btn.setText(tag.title);
        btn.setTag(tag);
        return btn;
    }

    public class BtnData {
        int id;
        String title;
        boolean choosed;

        public BtnData(int id, String title, boolean choosed) {
            this.id = id;
            this.title = title;
            this.choosed = choosed;
        }
    }

    /**
     * 要达到完成的效果还需要仔细斟酌shader效果，
     * 目前我遇到的问题就是始终有透明度，
     */
    public class MyShadow extends View.DragShadowBuilder {
        private View viewSrc;

        public MyShadow(View view) {
            super(view);
            this.viewSrc = view;
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.drawColor(Color.RED);
//            super.onDrawShadow(canvas);
            viewSrc.draw(canvas);
        }
    }
}
