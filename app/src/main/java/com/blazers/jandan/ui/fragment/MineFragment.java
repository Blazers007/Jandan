package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blazers.jandan.R;
import com.blazers.jandan.model.database.local.LocalFavImages;
import com.blazers.jandan.model.pojo.favorite.Favorite;
import com.blazers.jandan.api.BlazersAPI;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.model.event.NightModeEvent;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.SdcardHelper;
import com.blazers.jandan.widgets.nightwatch.WatchTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Blazers on 2015/10/14.
 * <p>
 * http://stackoverflow.com/questions/9783368/alternatives-to-preferencefragment-with-android-support-v4
 * <p>
 * 后期修改为自定义 Preference控件使用
 */
public class MineFragment extends BaseFragment {

    public static final String TAG = MineFragment.class.getSimpleName();
    public static MineFragment INSTANCE;

    @BindView(R.id.set_night_mode)
    RelativeLayout setNightMode;
    @BindView(R.id.set_auto_gif)
    RelativeLayout setAutoGif;
    @BindView(R.id.set_meizi)
    RelativeLayout setMeizi;
    @BindView(R.id.set_filter)
    RelativeLayout setFilter;
    @BindView(R.id.set_filter_number)
    RelativeLayout setFilterNumber;
    @BindView(R.id.set_clean_cache)
    RelativeLayout setCleanCache;

    //Holders
    private SwitchHolder nightModeHolder, autoGifHolder, meiziHolder, filterHolder;
    private TextHolder filterNumberHolder, cleanCacheHolder;

    public static MineFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MineFragment();
            INSTANCE.setTAG(TAG);
        }
        return INSTANCE;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, root);
        initSettingSegments();
        return root;
    }

    /**
     * 初始化并读取保存的值
     */
    void initSettingSegments() {

        nightModeHolder = new SwitchHolder(setNightMode,
                R.string.setting_night_mode, R.string.setting_night_mode_describe, SPHelper.NIGHT_MODE_ON,
                (view, isChecked) -> Rxbus.getInstance().send(new NightModeEvent(isChecked))
        );

        autoGifHolder = new SwitchHolder(setAutoGif,
                R.string.setting_auto_gif, R.string.setting_auto_gif_describe, SPHelper.AUTO_GIF_MODE_ON, null);

        meiziHolder = new SwitchHolder(setMeizi,
                R.string.setting_meizi, R.string.setting_meizi_describe, SPHelper.MEIZI_MODE_ON, null);

        filterHolder = new SwitchHolder(setFilter,
                R.string.setting_auto_filter, R.string.setting_auto_filter_describe, SPHelper.AUTO_FILTER_MODE_ON,
                (view, isChecked) -> {
                    filterNumberHolder.setEnabled(isChecked);
                    if (!isChecked)
                        SPHelper.putIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, 10000);
                }
        );

        /* 输入框 */
        filterNumberHolder = new TextHolder(setFilterNumber,
                R.string.setting_filter_number, R.string.setting_filter_number_describe,
                SPHelper.getIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, 0) + "",
                SPHelper.getBooleanSP(getActivity(), SPHelper.AUTO_FILTER_MODE_ON, false),
                (v) -> {
                 /* 首先获取当前 */
                    int selection = SPHelper.getIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, -1);
//                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                            .setTitle(R.string.filter_dialog_title)
//                            .setItems(R.array.filter_selections, (dialogInterface, i) -> {
//
//                            })
//                            .setPositiveButton(R.string.choose, (dialogInterface, i) -> {
//
//                            })
//                            .setPositiveButton(R.string.negetive, (dialogInterface, i) -> {
//
//                            })
//                            .create();
                    MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                            .title(R.string.filter_dialog_title)
                            .items(R.array.filter_selections)
                            .itemsCallbackSingleChoice(selection, (dia, view, which, str) -> {
                                if (which == -1) {
                                    SPHelper.putIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, which);
                                    return true;
                                }
                                filterNumberHolder.setText(str.toString());
                                SPHelper.putIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, which);
                                return true;
                            })
                            .positiveText(R.string.choose)
                            .positiveColor(Color.rgb(240, 114, 175))
                            .negativeText(R.string.negetive)
                            .negativeColor(Color.rgb(109, 109, 109))
                            .build();
                    dialog.show();
                }
        );

        // 点击清空
        cleanCacheHolder = new TextHolder(setCleanCache,
                R.string.setting_clean_cache, R.string.setting_clean_cache_describe,
                "计算中", true,
                (v) -> SdcardHelper.cleanSDCardOffline()
                        .compose(RxHelper.applySchedulers())
                        .subscribe(
                                state -> {
                                    Toast.makeText(getActivity(), "清理完毕", Toast.LENGTH_SHORT).show();
                                    SdcardHelper.calculateOfflineSize().compose(RxHelper.applySchedulers()).subscribe(cleanCacheHolder::setText);
                                }
                                , throwable -> Log.e("Error", "清空失败")
                        )
        );
        SdcardHelper.calculateOfflineSize().compose(RxHelper.applySchedulers()).subscribe(cleanCacheHolder::setText);
    }

    /**
     * Create Or Update Fav data on server
     */
    void createOrUpdateData() {
        try {
            BlazersAPI service = Favorite.getRetrofitServiceInstance();
            Favorite.getLocalFavorite(getActivity())
                    .flatMap(json -> service.postUserFavorite("bqvSgbP6G", json))
                    .compose(RxHelper.applySchedulers())
                    .subscribe(state -> {
                        Log.e("State", state);
                    }, throwable -> Log.e("POST", throwable.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sync Data from Server
     *
     * @param fullCopy true 同样也备份用户UUID
     */
    void syncDataFromServer(boolean fullCopy) {
        BlazersAPI service = null;
        try {
            service = Favorite.getRetrofitServiceInstance();
            service.getUserFavorite("bqvSgbP6G")
                    .compose(RxHelper.applySchedulers())
                    .subscribe(favorite -> {
                        for (LocalFavImages image : favorite.images) {
                            Log.i("Url", image.getUrl());
                        }
                    }, throwable -> Log.e("Error", throwable.toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch Holder 终究还要采用Preference
     */
    class SwitchHolder {

        @BindView(R.id.setting_main)
        WatchTextView title;
        @BindView(R.id.setting_sub)
        WatchTextView sub;
        @BindView(R.id.setting_switch)
        SwitchCompat switchCompat;
        private View root;
        private String key;
        //

        public SwitchHolder(View root, int title, int sub, String key, CompoundButton.OnCheckedChangeListener listener) {
            ButterKnife.bind(this, root);
            this.root = root;
            //
            this.title.setText(title);
            this.sub.setText(sub);

            this.key = key;
            switchCompat.setChecked(SPHelper.getBooleanSP(getActivity(), key, false));

            root.setClickable(true);
            root.setOnClickListener(v -> switchCompat.toggle());

            switchCompat.setOnCheckedChangeListener((view, isChecked) -> {
                SPHelper.putBooleanSP(getActivity(), key, isChecked);
                if (listener != null)
                    listener.onCheckedChanged(view, isChecked);
            });
        }

        public void hide() {
            root.setVisibility(View.GONE);
        }
    }

    class TextHolder {

        @BindView(R.id.setting_main)
        TextView title;
        @BindView(R.id.setting_sub)
        TextView sub;
        @BindView(R.id.setting_text)
        TextView text;
        @BindView(R.id.disable_mask)
        View disableMask;
        private View root;
        private boolean enabled;

        public TextHolder(View root, int title, int sub, String value, boolean enabled, View.OnClickListener listener) {
            ButterKnife.bind(this, root);
            this.root = root;
            this.enabled = enabled;
            this.title.setText(title);
            this.sub.setText(sub);
            setEnabled(enabled);
            text.setText(value);
            root.setOnClickListener(listener);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            root.setClickable(enabled);
            if (enabled) {
                disableMask.setVisibility(View.GONE);
            } else {
                disableMask.setVisibility(View.VISIBLE);
            }
        }
    }
}
