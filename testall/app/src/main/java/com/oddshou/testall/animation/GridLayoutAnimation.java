package com.oddshou.testall.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddshou.testall.R;

import java.util.ArrayList;

/**
 * Created by oddshou on 2017/6/19.
 */

public class GridLayoutAnimation extends Activity implements
        View.OnClickListener{
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
    private DragGridLayout gridLayoutUnChoosed;
    private DragGridLayout gridLayoutChoosed;
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
        gridLayoutChoosed = new DragGridLayout(this);
        gridLayoutChoosed.setChangeItemCallback(callback);
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
        gridLayoutUnChoosed = new DragGridLayout(this);
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

                }

                PropertyValuesHolder pvhTransX =
                        PropertyValuesHolder.ofFloat("translationX", locationSrc[0] - locationDst[0], 0f);
                PropertyValuesHolder pvhTransY =
                        PropertyValuesHolder.ofFloat("translationY", locationSrc[1] - locationDst[1], 0f);
                final ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(
                        this, pvhTransX, pvhTransY).
                        setDuration(transition.getDuration(LayoutTransition.APPEARING));
                transition.setAnimator(LayoutTransition.APPEARING, animIn);
                animIn.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        choosedChange = false;
                    }
                });
            }
        }

        @Override
        public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            container.postInvalidate(); //这主要解决转换过程中的一些异常
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
//            btn.setOnLongClickListener(this);
            btn.setOnClickListener(this);
//            btn.setOnDragListener(this);
            btn.setWidth(160);
            btn.setText(titles[i]);
            btn.setBackgroundResource(R.drawable.button_selector);
            btn.setTextColor(Color.BLACK);
            BtnData btnData = new BtnData(i, titles[i], titles == CHANNELS_CHOOSED);

            btn.setTag(btnData);
            groupList.add(btnData);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(10, 10, 10, 10);
            btn.setLayoutParams(lp);
            rootView.addView(btn);
        }
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

    private DragGridLayout.ChangeItemCallback callback = new DragGridLayout.ChangeItemCallback() {

        @Override
        public void changeItem(int indexEnd, View childView) {
            if (choosedChange)
                return;
            int indexStart = gridLayoutChoosed.indexOfChild(childView);
            choosedChange = true;
            choosedChangeId =  indexEnd > indexStart ? indexEnd-1 : indexEnd;
            //如果起点小于终点，计算终点坐标时减1，大于终点不减
//            View view = gridLayoutChoosed.getChildAt(indexStart);
            gridLayoutChoosed.removeView(childView);
            gridLayoutChoosed.addView(childView, indexEnd);
//            Logger.i(TAG, "changeItem: " + indexStart + " : " + indexEnd, "oddshou");
        }
    };


}
