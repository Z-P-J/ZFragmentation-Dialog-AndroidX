package com.zpj.fragmentation.dialog.impl;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.DialogAnimator;
import com.zpj.fragmentation.dialog.base.AttachDialogFragment;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttachListDialogFragment<T> extends AttachDialogFragment<AttachListDialogFragment<T>> {

    protected RecyclerView recyclerView;
    protected int bindLayoutId;
    protected int bindItemLayoutId;
    protected int tintColor = Color.TRANSPARENT;
    protected int textColor = Color.TRANSPARENT;

    private float cornerRadius;

    private IDialog.ViewBinder<ImageView, T> iconCallback;
    private IDialog.ViewBinder<TextView, T> titleCallback;

//    private int minWidth;


    private final List<T> items = new ArrayList<>();
    private final List<Integer> iconIds = new ArrayList<>();

    public AttachListDialogFragment() {
        cornerRadius = ScreenUtils.dp2px(8);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout._dialog_layout_attach_impl_list;
    }

    @Override
    protected DialogAnimator onCreateShadowAnimator(FrameLayout flContainer) {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (attachView == null && touchPoint == null) {
            touchPoint = new PointF(0, 0);
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        if (maxWidth <= 0) {
//            minWidth = ScreenUtils.getScreenWidth(context) / 2;
            maxWidth = ScreenUtils.dp2pxInt(180);
        }
        int color = DialogThemeUtils.getDialogBackgroundColor(context);
        ShadowLayout shadowLayout = findViewById(R.id.shadow_layout);
        shadowLayout.setmShadowColor(Color.DKGRAY);
        shadowLayout.setmCornerRadius((int) cornerRadius);
        CardView cardView = findViewById(R.id.cv_container);
        cardView.setRadius(cornerRadius);
        try {
            Field mBackGroundColor = ShadowLayout.class.getDeclaredField("mBackGroundColor");
            mBackGroundColor.setAccessible(true);
            mBackGroundColor.set(shadowLayout, color);
            shadowLayout.setSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (textColor == 0) {
            textColor = DialogThemeUtils.getMajorTextColor(context);
        }

        recyclerView = findViewById(R.id._dialog_recycler_View);
        recyclerView.setMinimumWidth(maxWidth);
        EasyRecyclerView<T> easyRecyclerView = new EasyRecyclerView<>(recyclerView);
        easyRecyclerView.setData(items)
                .setItemRes(bindItemLayoutId == 0 ? R.layout._dialog_item_text : bindItemLayoutId)
                .onBindViewHolder((holder, list, position, payloads) -> {
//                    holder.getItemView().setMinimumWidth(maxWidth);
                    TextView tvText = holder.getView(R.id.tv_text);
                    tvText.setTextColor(textColor);

                    ImageView ivImage = holder.getView(R.id.iv_image);
                    if (iconCallback == null) {
                        if (iconIds.size() > position) {
                            ivImage.setVisibility(View.VISIBLE);
                            ivImage.setImageResource(iconIds.get(position));
                            if (tintColor == Color.TRANSPARENT) {
                                ivImage.clearColorFilter();
                            } else {
                                ivImage.setColorFilter(tintColor);
                            }
                        } else {
                            ivImage.setVisibility(View.GONE);
                        }
                    } else {
                        ivImage.setVisibility(View.VISIBLE);
                        if (tintColor == Color.TRANSPARENT) {
                            ivImage.clearColorFilter();
                        } else {
                            ivImage.setColorFilter(tintColor);
                        }
                        iconCallback.onBindView(ivImage, list.get(position), position);
                    }
                    if (titleCallback == null) {
                        tvText.setText(list.get(position).toString());
                    } else {
                        titleCallback.onBindView(tvText, list.get(position), position);
                    }

                })
                .onItemClick((holder, view1, data) -> {
                    if (selectListener != null) {
                        selectListener.onSelect(AttachListDialogFragment.this, holder.getAdapterPosition(), data);
                    }
                })
                .build();
    }

//    public AttachListDialogFragment<T> setMinWidth(int minWidth) {
//        this.minWidth = minWidth;
//        return this;
//    }

    /**
     * ??????????????????????????????????????????id?????????
     *
     * @param layoutId ??????layoutId??????????????????id???recyclerView???RecyclerView???????????????????????????????????????????????????id???tv_title???TextView
     * @return
     */
    public AttachListDialogFragment<T> bindLayout(int layoutId) {
        this.bindLayoutId = layoutId;
        return this;
    }

    /**
     * ?????????????????? item??????
     *
     * @param itemLayoutId ???????????????id???????????????????????????id???iv_image???ImageView??????id???tv_text???TextView
     * @return
     */
    public AttachListDialogFragment<T> bindItemLayout(int itemLayoutId) {
        this.bindItemLayoutId = itemLayoutId;
        return this;
    }

//    public void show(View atView) {
//        popupInfo.atView = atView;
//        show();
//    }
//
//    public void show(float x, float y) {
//        popupInfo.touchPoint = new PointF(x, y);
//        show();
//    }

    public AttachListDialogFragment<T> setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public AttachListDialogFragment<T> setCornerRadiusDp(float cornerRadiusDp) {
        return setCornerRadius(ScreenUtils.dp2px(cornerRadiusDp));
    }

    public AttachListDialogFragment<T> setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public AttachListDialogFragment<T> setIconTintColor(int tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    public AttachListDialogFragment<T> setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        return this;
    }

    public AttachListDialogFragment<T> addItems(List<T> items) {
        this.items.addAll(items);
        return this;
    }

    public AttachListDialogFragment<T> addItems(T...items) {
        this.items.addAll(Arrays.asList(items));
        return this;
    }

    public AttachListDialogFragment<T> addItemsIf(boolean flag, T...items) {
        if (flag) {
            this.items.addAll(Arrays.asList(items));
        }
        return this;
    }

    public AttachListDialogFragment<T> addItem(T item) {
        this.items.add(item);
        return this;
    }

    public AttachListDialogFragment<T> addItemIf(boolean flag, T item) {
        if (flag) {
            this.items.add(item);
        }
        return this;
    }

    public AttachListDialogFragment<T> setIconIds(List<Integer> iconIds) {
        this.iconIds.clear();
        this.iconIds.addAll(iconIds);
        return this;
    }

    public AttachListDialogFragment<T> addIconId(int iconId) {
        this.iconIds.add(iconId);
        return this;
    }

    public AttachListDialogFragment<T> addIconIds(Integer...ids) {
        this.iconIds.addAll(Arrays.asList(ids));
        return this;
    }

    public AttachListDialogFragment<T> setOffsetXAndY(int offsetX, int offsetY) {
        this.defaultOffsetX += offsetX;
        this.defaultOffsetY += offsetY;
        return this;
    }

    private OnSelectListener<T> selectListener;

    public AttachListDialogFragment<T> setOnSelectListener(OnSelectListener<T> selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    public AttachListDialogFragment<T> onBindIcon(IDialog.ViewBinder<ImageView, T> iconCallback) {
        this.iconCallback = iconCallback;
        return this;
    }

    public AttachListDialogFragment<T> onBindTitle(IDialog.ViewBinder<TextView, T> titleCallback) {
        this.titleCallback = titleCallback;
        return this;
    }

    public interface OnSelectListener<T> {
        void onSelect(AttachListDialogFragment<T> fragment, int position, T text);
    }

}
