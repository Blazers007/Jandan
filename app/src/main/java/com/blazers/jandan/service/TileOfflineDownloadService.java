package com.blazers.jandan.service;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

import com.blazers.jandan.R;
import com.blazers.jandan.util.log.Log;

/**
 * Created by blazers on 2016/11/17.
 *
 * 通知栏下拉菜单的快捷开关
 *
 * ==========================================================================================
 *
 * 编辑界面 把按钮拖进快捷设置栏
 *
 *          -: onCreate()
 *          -: onBind()
 *          -: onTileAdded()
 *          -: onDestroy()
 *
 *==========================================================================================
 *
 * 快捷设置栏中按钮可见
 *
 *          -: onCreate()
 *          -: onBind()
 *          -: onStartListening()
 *
 * ==========================================================================================
 *
 * 快捷设置栏按钮不可见
 *
 *          -: onStopListening()
 *          -: onDestroy()
 *
 * ==========================================================================================
 *
 * 点击按钮
 *
 *          -: onClick()
 *
 * ==========================================================================================
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class TileOfflineDownloadService extends TileService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("mID", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("mID", "onBind");
        return super.onBind(intent);
    }

    @Override
    public void onTileAdded() {
        Log.i("mID", "onTileAdded");
        super.onTileAdded();
    }

    @Override
    public void onStartListening() {
        Log.i("mID", "onStartListening");
        super.onStartListening();
        // TODO: 是否能够通过 Update Icon 实现动画效果?
    }

    @Override
    public void onStopListening() {
        Log.i("mID", "onStopListening");
        super.onStopListening();
    }

    @Override
    public void onTileRemoved() {
        Log.i("mID", "onTileRemoved");
        super.onTileRemoved();
    }

    @Override
    public void onDestroy() {
        Log.i("mID", "onDestory");
        super.onDestroy();
    }

    @Override
    public void onClick() {
        Log.i("mID", "onClick");
        super.onClick();
        Tile tile = getQsTile();
        if (tile != null) {
            if (tile.getState() == Tile.STATE_ACTIVE) {
                tile.setLabel(getString(R.string.tile_icon_offline));
                tile.setState(Tile.STATE_INACTIVE);
            } else {
                tile.setLabel(getString(R.string.tile_icon_offline_ing));
                tile.setState(Tile.STATE_ACTIVE);
            }
            tile.updateTile();
        }
    }

    private void markAsInitial() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setLabel(getString(R.string.tile_icon_offline));
            tile.setState(Tile.STATE_INACTIVE);
        }
    }

    // Util.CheckPermission

    // Start activity for requesting permission and let the user set whether to quit or not
    private void startActivityForRequesintPermission() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // ? 启动到首页 clear top 还是 跳转到指定的权限申请界面?
//        startActivityAndCollapse(intent);
    }
}
