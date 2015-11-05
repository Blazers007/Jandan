package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blazers.jandan.R;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.NightModeEvent;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.views.nightwatch.NightWatcher;
import com.blazers.jandan.views.nightwatch.WatchTextView;


/**
 * Created by Blazers on 2015/10/14.
 *
 * http://stackoverflow.com/questions/9783368/alternatives-to-preferencefragment-with-android-support-v4
 *
 * 后期修改为自定义 Preference控件使用
 *
 */
public class SettingFragment extends BaseFragment {

    public static final String TAG = SettingFragment.class.getSimpleName();
    public static SettingFragment INSTANCE;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.set_night_mode) RelativeLayout setNightMode;
    @Bind(R.id.set_auto_gif) RelativeLayout setAutoGif;
    @Bind(R.id.set_meizi) RelativeLayout setMeizi;
    @Bind(R.id.set_filter) RelativeLayout setFilter;
    @Bind(R.id.set_filter_number) RelativeLayout setFilterNumber;

    //Holders
    private SwitchHolder nightModeHolder, autoGifHolder, meiziHolder, filterHolder;
    private EditIntHolder filterNumberHolder;

    public static SettingFragment getInstance() {
        if (INSTANCE == null){
            INSTANCE = new SettingFragment();
            INSTANCE.setTAG(TAG);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, root);
        initToolbarAndLeftDrawer(toolbar, "设置");
        initSettingSegments();
        return root;
    }

    /**
     * 初始化并读取保存的值
     * */
    void initSettingSegments() {

        nightModeHolder = new SwitchHolder(setNightMode,
            R.string.setting_night_mode, R.string.setting_night_mode_describe, SPHelper.NIGHT_MODE_ON,
            (view, isChecked)->Rxbus.getInstance().send(new NightModeEvent(isChecked))
        );
        // Todo 目前暂时隐藏
//        nightModeHolder.hide();

        autoGifHolder = new SwitchHolder(setAutoGif,
            R.string.setting_auto_gif, R.string.setting_auto_gif_describe, SPHelper.AUTO_GIF_MODE_ON, null);

        meiziHolder = new SwitchHolder(setMeizi,
            R.string.setting_meizi, R.string.setting_meizi_describe, SPHelper.MEIZI_MODE_ON, null);

        filterHolder = new SwitchHolder(setFilter,
            R.string.setting_auto_filter, R.string.setting_auto_filter_describe, SPHelper.AUTO_FILTER_MODE_ON,
            (view, isChecked)->filterNumberHolder.setEnabled(isChecked)
        );

        /* 输入框 */
        filterNumberHolder = new EditIntHolder(setFilterNumber,
            R.string.setting_filter_number, R.string.setting_filter_number_describe, SPHelper.AUTO_FILTER_NUMBER,
            SPHelper.getBooleanSP(getActivity(), SPHelper.AUTO_FILTER_MODE_ON, false)
        );
    }

    /**
     * Switch Holder 终究还要采用Preference
     * */
    class SwitchHolder {

        private View root;
        private String key;

        @Bind(R.id.setting_main) WatchTextView title;
        @Bind(R.id.setting_sub) WatchTextView sub;
        @Bind(R.id.setting_switch) SwitchCompat switchCompat;
        //

        public SwitchHolder(View root, int title, int sub, String key, CompoundButton.OnCheckedChangeListener listener){
            ButterKnife.bind(this, root);
            this.root = root;
            //
            this.title.setText(title);
            this.sub.setText(sub);

            this.key = key;
            switchCompat.setChecked(SPHelper.getBooleanSP(getActivity(), key, false));

            root.setClickable(true);
            root.setOnClickListener(v -> switchCompat.toggle());

            switchCompat.setOnCheckedChangeListener((view, isChecked)->{
                SPHelper.putBooleanSP(getActivity(), key, isChecked);
                if (listener != null)
                    listener.onCheckedChanged(view, isChecked);
            });
        }

        public void hide(){
            root.setVisibility(View.GONE);
        }
    }

    class EditIntHolder {

        private View root;
        private boolean enabled;

        @Bind(R.id.setting_main) TextView title;
        @Bind(R.id.setting_sub) TextView sub;
        @Bind(R.id.setting_text) TextView text;
        @Bind(R.id.disable_mask) View disableMask;

        public EditIntHolder (View root, int title, int sub, String key, boolean enabled) {
            ButterKnife.bind(this, root);
            this.root = root;
            this.enabled = enabled;

            this.title.setText(title);
            this.sub.setText(sub);

            setEnabled(enabled);

            int value = SPHelper.getIntSP(getActivity(), key, 0);
            text.setText(String.format("%d", value));

            root.setOnClickListener(v->{
                /* 首先获取当前 */
                int selection = SPHelper.getIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, -1);
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title(R.string.filter_dialog_title)
                        .items(R.array.filter_selections)
                        .itemsCallbackSingleChoice(selection, (dia, view, which, str)->{
                            if (which == -1) {
                                SPHelper.putIntSP(getActivity(), key, which);
                                return true;
                            }
                            text.setText(str);
                            SPHelper.putIntSP(getActivity(), key, which);
                            return true;
                        })
                        .positiveText(R.string.choose)
                        .positiveColor(Color.rgb(240, 114 ,175))
                        .negativeText(R.string.negetive)
                        .negativeColor(Color.rgb(109, 109, 109))
                        .build();
                dialog.show();
            });
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            root.setClickable(enabled);
            if (enabled) {
                disableMask.setVisibility(View.GONE);
            }else{
                disableMask.setVisibility(View.VISIBLE);
            }
        }
    }
}
