package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.animation.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.animation.IMsgAnimation;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.callback.IMsgAnimationCallback;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.IMsgAnimationLifecycle;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.IMsgAnimationView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 消息动画 - 气泡翻转动效
 * Created by xxshhh on 2017/11/15.
 */
public class MsgAnimation_Rollover implements IMsgAnimation {

    // --- 动画相关 ---
    private Subscription mGetChatAnimationSub;
    private Animator mMsgAnimation;
    private IMsgAnimationView mView;

    // --- 裁剪相关 ---
    private Path mRootBorderPath; // 根布局边界路径
    private Path mTextBorderPath; // 文本布局边界路径
    private Path mAnimationPath; // 实际动画路径
    private boolean mIsPlay; // 是否正在播放裁剪动画

    private static final int PER_FRAME_TIME = 40; // 动画每帧时间
    private MessageQueue.IdleHandler mIdleHandler; // 绘制完成的回调

    public MsgAnimation_Rollover() {
        mGetChatAnimationSub = null;
        mMsgAnimation = null;

        mRootBorderPath = new Path();
        mTextBorderPath = new Path();
        mAnimationPath = new Path();
        mIsPlay = false;
    }

    @Override
    public void playAnimation(IMsgAnimationView msgAnimationView) {
        mView = msgAnimationView;

        // 设置动画关联生命周期
        setLifecycle();
        // 设置ItemView不可见
        setItemViewVisible(false);
        // 绘制完成的回调
        mIdleHandler = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                // 获取动画
                mGetChatAnimationSub = Observable
                        .create(new Observable.OnSubscribe<Animator>() {
                            @Override
                            public void call(Subscriber<? super Animator> subscriber) {
                                try {
                                    Animator animator = getAnimation();
                                    subscriber.onNext(animator);
                                    subscriber.onCompleted();
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Animator>() {
                            @Override
                            public void onCompleted() {
                                if (mMsgAnimation == null) {
                                    // 设置ItemView可见
                                    setItemViewVisible(true);
                                } else {
                                    // 开始动画
                                    mMsgAnimation.start();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                // 设置ItemView可见
                                setItemViewVisible(true);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Animator animator) {
                                mMsgAnimation = animator;
                            }
                        });
                return false;
            }
        };
        Looper.myQueue().addIdleHandler(mIdleHandler);
    }

    /**
     * 设置ItemView可见性
     */
    private void setItemViewVisible(boolean visible) {
        if (mView != null && mView.getItemView() != null) {
            mView.getItemView().setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 设置动画关联生命周期
     */
    private void setLifecycle() {
        if (mView == null) {
            return;
        }
        mView.setMsgAnimationLifecycle(new IMsgAnimationLifecycle() {
            @Override
            public void onDraw(Canvas canvas) {
                clipCanvas(canvas);
            }

            @Override
            public void onDestroy() {
                destroy();
            }
        });
    }

    /**
     * 销毁
     */
    private void destroy() {
        if (mIdleHandler != null) {
            Looper.myQueue().removeIdleHandler(mIdleHandler);
            mIdleHandler = null;
        }
        if (mGetChatAnimationSub != null && !mGetChatAnimationSub.isUnsubscribed()) {
            mGetChatAnimationSub.unsubscribe();
        }
        mGetChatAnimationSub = null;
        if (mMsgAnimation != null) {
            mMsgAnimation.end();
            mMsgAnimation = null;
            mIsPlay = false;
        }
    }

    /**
     * 裁剪
     */
    private void clipCanvas(Canvas canvas) {
        if (mMsgAnimation != null && mIsPlay) {
            mIsPlay = false;
            canvas.clipPath(mRootBorderPath);
            canvas.clipPath(mTextBorderPath, Region.Op.DIFFERENCE);
            canvas.clipPath(mAnimationPath, Region.Op.UNION);
        }
    }

    /**
     * 获取动画
     */
    private Animator getAnimation() {
        // 初始化View
        if (mView == null) {
            return null;
        }
        View itemView = mView.getItemView();
        View avatarView = mView.getAvatarView();
        View msgView = mView.getMsgView();
        if (itemView == null || avatarView == null || msgView == null) {
            return null;
        }

        Context context = itemView.getContext();
        // 小球动画的三个点：起点、最高点、终点
        int[] points = getCircleAnimationPoints(context, msgView);
        if (points == null || points.length < 6) {
            return null;
        }
        int startX = points[0];
        int startY = points[1];
        int topX = points[2];
        int topY = points[3];
        int endX = points[4];
        int endY = points[5];

        // 小球动画
        Animator circleAnimator = getCircleAnimation(context, startX, startY, topX, topY);
        if (circleAnimator == null) {
            return null;
        }

        // 消息动画
        Animator msgAnimator = getMsgAnimation(itemView, msgView, avatarView, topX, topY, endX, endY);
        if (msgAnimator == null) {
            return null;
        }
        msgAnimator.setStartDelay(circleAnimator.getDuration() - PER_FRAME_TIME); // 提前1帧

        // 返回动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(circleAnimator).with(msgAnimator);
        return animatorSet;
    }

    /**
     * 获取小球动画的三个点：起点、最高点、终点
     */
    private int[] getCircleAnimationPoints(Context context, View msgView) {
        // 获取小球父容器View
        IMsgAnimationCallback callback = getMsgAnimationCallback(context);
        if (callback == null) {
            return null;
        }
        final ViewGroup parentView = callback.getAnimationParentView();
        if (parentView == null) {
            return null;
        }

        // 计算小球起点、最高点、终点
        // 父容器点
        int[] parentLoc = new int[2];
        parentView.getLocationOnScreen(parentLoc);
        // 终点
        int[] endLoc = new int[2];
        msgView.getLocationOnScreen(endLoc);
        endLoc[0] += msgView.getWidth() / 2 - parentLoc[0];
        endLoc[1] += msgView.getHeight() / 2 - parentLoc[1];
        // 此处计算小球相对于文本中心点的水平距离
        // 相关公式及条件：t=sqrt(2*H/g),X=Vx*t;已知Vx=250dp/s,g=3000dp^2/s
        int vx = context.getResources().getDimensionPixelOffset(R.dimen.animation_param_vx);
        int g = context.getResources().getDimensionPixelOffset(R.dimen.animation_param_g);
        int h = parentView.getHeight() - endLoc[1];
        long t = (long) (Math.sqrt(2f * h / g) * 1000);
        int x = (int) (vx * t / 1000);
        // 起点
        int[] startLoc = new int[2];
        startLoc[0] = endLoc[0] - x;
        startLoc[1] = parentView.getHeight();
        // 最高点位置相对文本中心点有横向和纵向偏移
        int offsetH = (int) getCircleFallHeight(context);
        long offsetT = (long) (Math.sqrt(2f * offsetH / g) * 1000);
        int offsetX = (int) (vx * offsetT / 1000);
        // 最高点
        int[] topLoc = new int[2];
        topLoc[0] = endLoc[0] - offsetX;
        topLoc[1] = endLoc[1] - offsetH;

        // 返回三个点数据
        int[] points = new int[6];
        points[0] = startLoc[0];
        points[1] = startLoc[1];
        points[2] = topLoc[0];
        points[3] = topLoc[1];
        points[4] = endLoc[0];
        points[5] = endLoc[1];
        return points;
    }

    /**
     * 获取界面回调
     */
    private IMsgAnimationCallback getMsgAnimationCallback(Context context) {
        if (!(context instanceof CommonContainerActivity)) {
            return null;
        }
        Fragment fragment = ((CommonContainerActivity) context).getCurrentFragment();
        if (!(fragment instanceof IMsgAnimationCallback)) {
            return null;
        }
        return (IMsgAnimationCallback) fragment;
    }

    /**
     * 获取小球动画
     */
    private Animator getCircleAnimation(Context context, float startX, float startY, float endX, float endY) {
        // 获取小球父容器View
        IMsgAnimationCallback callback = getMsgAnimationCallback(context);
        if (callback == null) {
            return null;
        }
        final ViewGroup parentView = callback.getAnimationParentView();
        if (parentView == null) {
            return null;
        }

        // 创建一个小球
        final ImageView circleView = new ImageView(context);
        Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.animation_msg_bg_selector);
        Drawable circleDrawable = getCircleDrawable(context, defaultDrawable);
        circleView.setImageDrawable(circleDrawable);

        // 计算小球起点、终点、控制点
        float radius = getCircleRadius(context);
        startX -= radius;
        startY -= radius;
        endX -= radius;
        endY -= radius;
        float controlX = startX;
        float controlY = endY;

        // 根据高度确定动画时间，相关公式：y = 0.232*x + 0.202
        float percent = (startY - endY) / getScreenHeight(context);
        long duration = (long) ((0.232 * percent + 0.202) * 1000);

        // 创建贝塞尔曲线动画
        ValueAnimator animator = getBezierCurveAnimation(startX, startY, controlX, controlY, endX, endY,
                duration, circleView);
        animator.setInterpolator(new DecelerateInterpolator()); // 小球减速运动
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setItemViewVisible(false); // 设置ItemView不可见
                parentView.addView(circleView); // 开始时添加小球
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setItemViewVisible(true); // 设置ItemView可见
                parentView.removeView(circleView); // 结束时移除小球
            }
        });
        return animator;
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取小球半径
     */
    private float getCircleRadius(Context context) {
        return context.getResources().getDimension(R.dimen.animation_circle_diameter_size) / 2;
    }

    /**
     * 获取小球落下高度
     */
    private float getCircleFallHeight(Context context) {
        return context.getResources().getDimension(R.dimen.animation_circle_fall_height_size);
    }

    /**
     * 获取小球背景
     */
    private Drawable getCircleDrawable(Context context, Drawable drawable) {
        // Drawable转Bitmap
        Bitmap srcBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas srcCanvas = new Canvas(srcBitmap);
        drawable.setBounds(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight());
        drawable.draw(srcCanvas);

        // 裁剪小球形状Bitmap
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 画圆
        float radius = getCircleRadius(context);
        canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); // 取两层绘制交集。显示上层
        // 画矩形
        Rect rect = new Rect(0, 0, width, height);
        canvas.drawBitmap(srcBitmap, rect, rect, paint);

        // 得到小球Drawable
        return new BitmapDrawable(context.getResources(), outBitmap);
    }

    /**
     * 获取贝塞尔曲线动画
     */
    private ValueAnimator getBezierCurveAnimation(float startX, float startY,
                                                  float controlX, float controlY,
                                                  float endX, float endY,
                                                  long duration, final View targetView) {
        // 运动轨迹
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, endX, endY);
        // 创建动画
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator animator = ValueAnimator.ofFloat(pathMeasure.getLength());
        animator.setDuration(duration);
        // 监听变化
        final float[] currentPos = new float[2];
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, currentPos, null);
                targetView.setTranslationX(currentPos[0]);
                targetView.setTranslationY(currentPos[1]);
            }
        });
        return animator;
    }

    /**
     * 获取消息动画
     */
    private Animator getMsgAnimation(View itemView, View msgView, View avatarView, float startX, float startY, float endX, float endY) {
        // 文本动画
        Animator textAnimator = getTextAnimation(itemView, msgView, startX, startY, endX, endY);
        if (textAnimator == null) {
            return null;
        }

        // 头像动画
        Animator avatarAnimator = getAvatarAnimation(avatarView);
        if (avatarAnimator == null) {
            return null;
        }

        // 返回动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(textAnimator).with(avatarAnimator);
        return animatorSet;
    }

    /**
     * 获取文本动画
     */
    private Animator getTextAnimation(View itemView, View msgView, float startX, float startY, float endX, float endY) {
        // 揭露动画
        Animator textRevealAnimation = getTextRevealAnimation(itemView, msgView, startX, startY, endX, endY);
        if (textRevealAnimation == null) {
            return null;
        }

        // 抖动动画
        Animator textShakeAnimator = getTextShakeAnimation(msgView);
        if (textShakeAnimator == null) {
            return null;
        }
        textShakeAnimator.setStartDelay(textRevealAnimation.getDuration() - 4 * PER_FRAME_TIME); // 提前4帧

        // 返回动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(textRevealAnimation).with(textShakeAnimator);
        return animatorSet;
    }

    /**
     * 获取文本揭露动画
     */
    private Animator getTextRevealAnimation(final View itemView, final View msgView, float startX, float startY, float endX, float endY) {
        // 裁剪相关参数
        // 获取根布局边界路径（不变）
        mRootBorderPath.reset();
        mRootBorderPath.addRect(0, 0, itemView.getWidth(), itemView.getHeight(), Path.Direction.CW);
        // 根布局位置
        final int[] rootLoc = new int[2];
        // 文本位置
        final int[] textLoc = new int[2];
        // 文本圆角
        final int round = itemView.getResources().getDimensionPixelOffset(R.dimen.animation_rect_round_size);
        // 小球半径
        final float radius = getCircleRadius(itemView.getContext());
        // 临时Path
        final Path path = new Path();
        // 临时RectF
        final RectF rectF = new RectF();
        // 临时Matrix
        final Matrix matrix = new Matrix();

        // 创建贝塞尔曲线动画
        ValueAnimator animator = getBezierCurveAnimation(startX - endX, startY - endY, 0, startY - endY, 0, 0,
                8 * PER_FRAME_TIME, msgView);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                // 旋转角度
                int degree = (int) (-10 + 10 * fraction);
                float textWidth = msgView.getWidth();
                float textHeight = msgView.getHeight();
                msgView.setPivotX(textWidth / 2);
                msgView.setPivotY(textHeight / 2);
                msgView.setRotation(degree);

                // 裁剪文本
                // 获取文本布局边界路径（变化）
                itemView.getLocationOnScreen(rootLoc);
                msgView.getLocationOnScreen(textLoc);
                float textLeft = textLoc[0] - rootLoc[0];
                float textTop = textLoc[1] - rootLoc[1];
                float textRight = textLeft + textWidth;
                float textBottom = textTop + textHeight;
                rectF.set(textLeft, textTop, textRight, textBottom);
                path.reset();
                path.addRoundRect(rectF, round, round, Path.Direction.CW);
                // 矩阵旋转变换
                matrix.reset();
                matrix.setRotate(degree, textLeft, textTop);
                mTextBorderPath.reset();
                mTextBorderPath.addPath(path, matrix);

                // 获取实际动画路径（变化）
                float centerX = textLeft + textWidth / 2f;
                float centerY = textTop + textHeight / 2f;
                float maxRadiusX = textWidth / 2f;
                float maxRadiusY = textHeight / 2f;
                float marginX = radius + (maxRadiusX - radius) * fraction;
                float marginY = radius + (maxRadiusY - radius) * fraction;
                float left = centerX - marginX;
                float top = centerY - marginY;
                float right = centerX + marginX;
                float bottom = centerY + marginY;
                rectF.set(left, top, right, bottom);
                path.reset();
                path.addRoundRect(rectF, round, round, Path.Direction.CW);
                // 矩阵旋转变换
                mAnimationPath.reset();
                mAnimationPath.addPath(path, matrix);

                // 调用onDraw进行裁剪
                mIsPlay = true;
                itemView.invalidate();
            }
        });
        return animator;
    }

    /**
     * 获取文本抖动动画
     */
    private Animator getTextShakeAnimation(View msgView) {
        long totalTime = 28 * PER_FRAME_TIME;
        float[] k = new float[28];
        for (int i = 0; i < 28; i++) {
            k[i] = i / 27f;
        }

        float[] sx = new float[]{
                1f, 1.004f, 1.016f, 1.032f, 1.05f,
                1.068f, 1.084f, 1.096f, 1.10f, 1.064f,
                0.996f, 0.96f, 0.963f, 0.972f, 0.984f,
                0.996f, 1.008f, 1.017f, 1.02f, 1.019f,
                1.017f, 1.015f, 1.012f, 1.008f, 1.005f,
                1.003f, 1.001f, 1f
        };

        float[] sy = new float[]{
                1f, 0.996f, 0.984f, 0.968f, 0.95f,
                0.932f, 0.916f, 0.904f, 0.90f, 0.939f,
                1.011f, 1.05f, 1.046f, 1.036f, 1.022f,
                1.008f, 0.994f, 0.984f, 0.98f, 0.981f,
                0.983f, 0.985f, 0.988f, 0.992f, 0.995f,
                0.997f, 0.999f, 1f
        };

        Keyframe[] keyframesScaleX = new Keyframe[28];
        Keyframe[] keyframesScaleY = new Keyframe[28];
        for (int i = 0; i < 28; i++) {
            keyframesScaleX[i] = Keyframe.ofFloat(k[i], sx[i]);
            keyframesScaleY[i] = Keyframe.ofFloat(k[i], sy[i]);
        }

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X, keyframesScaleX);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y, keyframesScaleY);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(msgView,
                pvhScaleX, pvhScaleY);
        animator.setDuration(totalTime);
        animator.setInterpolator(new LinearInterpolator()); // 抖动匀速变化
        return animator;
    }

    /**
     * 获取头像动画
     */
    private Animator getAvatarAnimation(View avatarView) {
        // 设置中心点
        avatarView.setPivotX(avatarView.getWidth() / 2f);
        avatarView.setPivotY(avatarView.getHeight());

        long totalTime = 12 * PER_FRAME_TIME;
        float k1 = 0f;
        float k2 = 2f / 12f;
        float k3 = 10f / 12f;
        float k4 = 1f;

        float alpha1 = 0f;
        float alpha2 = 0f;
        float alpha3 = 1f;
        float alpha4 = 1f;

        float scale1 = 0f;
        float scale2 = 0f;
        float scale3 = 1.06f;
        float scale4 = 1f;

        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofKeyframe(View.ALPHA,
                Keyframe.ofFloat(k1, alpha1),
                Keyframe.ofFloat(k2, alpha2),
                Keyframe.ofFloat(k3, alpha3),
                Keyframe.ofFloat(k4, alpha4));

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(k1, scale1),
                Keyframe.ofFloat(k2, scale2),
                Keyframe.ofFloat(k3, scale3),
                Keyframe.ofFloat(k4, scale4));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(k1, scale1),
                Keyframe.ofFloat(k2, scale2),
                Keyframe.ofFloat(k3, scale3),
                Keyframe.ofFloat(k4, scale4));

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(avatarView,
                pvhAlpha, pvhScaleX, pvhScaleY);
        animator.setDuration(totalTime);
        return animator;
    }

}
