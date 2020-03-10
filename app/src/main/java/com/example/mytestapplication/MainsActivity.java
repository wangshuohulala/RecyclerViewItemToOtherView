package com.example.mytestapplication;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainsActivity extends Activity {

    // ListView2
    private ListView mLv2 = null;
    // list2的adapter
    private LsAdapter2 mAdapter2 = null;
    // 支持的刷卡头
    String[] arrSupportShua = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};

    List<String> mList1 = new ArrayList<String>();
    List<String> mList2 = new ArrayList<String>();

    ImageView img1;
    ImageView img2;
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mLv2 = findViewById(R.id.list2);
        img1 = findViewById(R.id.image_view_you);
    }

    private void initData() {
        for (String shua : arrSupportShua) {
            mList2.add(shua);
        }
        mAdapter2 = new LsAdapter2(MainsActivity.this, mList2);

        mLv2.setAdapter(mAdapter2);
    }

    private void initListener() {
        mLv2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, final int location, long arg3) {
                //如果点击的时候，之前动画还没结束，那么就让点击事件无效
                if (isMove) {
                    return;
                }
                ImageView mtv = view.findViewById(R.id.item_tv);
                final int[] startLocation = new int[2];
                mtv.getLocationInWindow(startLocation);

                //相当于创建一个点击位置view的副本用于动画
                final ImageView img = getView(mtv);


                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            int[] endLocation = new int[2];
                            // 获取终点的坐标
                            img1.getLocationInWindow(endLocation);
                            MoveAnim(img, startLocation, endLocation);
                            img1.setImageResource(R.drawable.ic_launcher_background);
                            ObjectAnimator.ofFloat(img1, "alpha", 0, 1)
                                    .setDuration(2000)
                                    .start();
                        } catch (Exception localException) {
                        }
                    }
                }, 50L);

            }
        });
    }

    private void MoveAnim(ImageView moveView, int[] startLocation, int[] endLocation) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);

        ObjectAnimator mAnimator1 = ObjectAnimator.ofFloat(mMoveView, "translationY", startLocation[1], endLocation[1]);
        ObjectAnimator mAnimator2 = ObjectAnimator.ofFloat(mMoveView, "translationX", startLocation[0], endLocation[0]);
        ObjectAnimator mAnimator3 = ObjectAnimator.ofFloat(mMoveView, "alpha", 1, 0);
        final AnimatorSet translationAnimatorSet = new AnimatorSet();
        translationAnimatorSet.playTogether(mAnimator1, mAnimator2, mAnimator3);
        translationAnimatorSet.setDuration(2000);
        translationAnimatorSet.start();
        isMove = true;
        mAnimator1.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveViewGroup.removeView(mMoveView);
                isMove = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout
                .setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * 可以将任意View显示内容以及背景生成位图展示在ImageView中
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        //  ViewGroup里所有的子view开启cache
        //  setChildrenDrawingCacheEnabled(boolean enabled)

        // 使在绘制子view时，若该子view开启了cache,则使用它的cache进行绘制，从而节省绘制时间
        // (如何验证？)
        // setC hildrenDrawnWithCacheEnabled(boolean enabled)


        // 若想更新cache,必须要调用destroyDrawingCache方法把旧的cache销毁，才能建立新的
        view.destroyDrawingCache();
        // 要获取cache首先要通过setDrawingCacheEnable方法开启cache
        view.setDrawingCacheEnabled(true);
        // 调用getDrawingCache方法就可以获得view的cache图片,复制bp
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        // 当调用setDrawingCacheEnabled方法设置为false,系统也会自动把原来的cache销毁
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器 //方便控制对象？
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = initLocation[0];
        mLayoutParams.topMargin = initLocation[1];
        view.setLayoutParams(mLayoutParams);
        return view;
    }
}