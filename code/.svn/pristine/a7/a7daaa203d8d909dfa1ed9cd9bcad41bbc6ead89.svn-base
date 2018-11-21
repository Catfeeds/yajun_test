package com.wis.mes.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supoin.rfidtest.R;
import com.uhf.uhf.Common.Comm;
import com.uhf.uhf.UHF1.UHF001;
import com.uhf.uhf.UHF1Function.AndroidWakeLock;
import com.uhf.uhf.UHF1Function.SPconfig;
import com.uhf.uhf.UHF5Base.StringTool;
import com.uhf.uhf.UHF6.UHF006;
import com.wis.mes.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.ToastUtils;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;
import static com.uhf.uhf.Common.Comm.rfidOperate;


public class MainActivity extends Activity {

    //大于2时停止RFID
    private int pressCount = 0;
    //RFID是否开启
    private boolean isRfidStart = false;
    private long firstTime = 0;
    private WebView webView;
    private Button button;
    private EditText edit;
    List<String> listData = new ArrayList<>();
    private ScanOperate scanOperate;
    private Boolean is_canScan = true;

    private String epcData;
    private String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_one);

        webView = (WebView) findViewById(R.id.webView);
        button = (Button) findViewById(R.id.button);
        edit = (EditText) findViewById(R.id.edit);
        initData();
        InitDevice();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        String str = edit.getText().toString();
                        webView.loadUrl("javascript:AndroidToWeb('" + str + "')");
                        Log.e("Logs", "-----------------------wave--------------------");
                    }
                });
            }
        });
        scanOperate = new ScanOperate();
        scanOperate.onCreate(MainActivity.this, R.raw.scanok);
        scanOperate.openScannerPower(is_canScan);
        scanOperate.mHandler = scanHandler;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Comm.powerDown();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getScanCode() == 249 || event.getScanCode() == 261) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime <= 3000) {
                firstTime = secondTime;
            } else {
                pressCount = pressCount + 1;
                if (!isRfidStart) {
                    isRfidStart = true;
                    listData.clear();
                    Comm.Awl.WakeLock();
                    Comm.clean();
                    Comm.startScan();
                } else
                    isRfidStart = false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==4){ //返回键

        }
        if (event.getScanCode() == 249 || event.getScanCode() == 261) {
            if (pressCount == 2 && !isRfidStart) {
                Comm.Awl.ReleaseWakeLock();
                Comm.stopScan();
                pressCount = 0;
            } else if (pressCount > 2) {
                Comm.Awl.ReleaseWakeLock();
                Comm.stopScan();
                pressCount = 0;
            } else {
                new Thread() {
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);//设置支持js
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.requestFocus();
        webView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        webView.addJavascriptInterface(new JavascriptInterface(), "demo");
        webView.loadUrl("file:///android_asset/index.html");//设置加载网页
    }

    private void webViewPost(final String data, final String callback) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + callback + "('" + data + "')");
            }
        });
    }

    private void readTag(int ant, int tagBank, String opCount, String startAdd, int datatype) {
        Comm.Awl.WakeLock();
        Comm.clean();
        Comm.readTag(ant, tagBank, opCount, startAdd, datatype);
    }

    private void writeTag(int ant, int tagBank, String opCount, String startAdd, int datatype, String strWriteData) {
        try {
            boolean isWrite = true;
            Comm.opeT = Comm.operateType.writeOpe;
            if (tagBank == 1 && (startAdd.equals("0") || startAdd.equals("1"))) {
                Toast.makeText(MainActivity.this, "写操作不能操作EPC区第0块和第1块",
                        Toast.LENGTH_SHORT).show();
            }
            int len = 0;
            if (datatype == 0 && strWriteData.length() % 4 == 0) {
                String[] result = StringTool.stringToStringArray(strWriteData.toUpperCase(), 2);
                len = (byte) ((result.length / 2 + result.length % 2) & 0xFF);
            } else if (datatype == 1 && strWriteData.length() % 2 == 0) {
                len = strWriteData.length() / 2;
            } else if (datatype == 2) {
                len = strWriteData.length();
            } else {
                Toast.makeText(MainActivity.this, "输入的数据长度不对",
                        Toast.LENGTH_SHORT).show();
                isWrite = false;
            }
            if (isWrite) {
                Comm.writeTag(ant, tagBank, opCount, startAdd, datatype, strWriteData);
            } else
                Toast.makeText(MainActivity.this, "write fail",
                        Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Sub3Exception:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            Log.d("UHF", e.getMessage());
        }
    }

    private void setAntPower(String power) {
        if (Integer.parseInt(power) > 30) {
            ToastUtils.show(MainActivity.this, "功率不能大于30!");
            return;
        }
        Comm.setAntPower(Integer.parseInt(power) - 5, 1, 1, 1);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ScanOperate.MESSAGE_TEXT:
                    String code = (String) msg.obj;
                    scanOperate.setVibratortime(50);
                    scanOperate.mediaPlayer();

                    Log.e("barcode", code);
                    webView.loadUrl("javascript:AndroidToWeb('" + code + "')");
                    break;
                case 2:
                    break;
            }

        }
    };

    private void InitDevice() {
        Comm.checkDevice();
        Comm.app = getApplication();
        Comm.spConfig = new SPconfig(this);
        Comm.context = this;
        Comm.soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        Comm.soundPool.load(this, R.raw.beep51, 1);
        Comm.Awl = new AndroidWakeLock((PowerManager) this.getSystemService(Context.POWER_SERVICE));
        if (!Comm.powerUp()) {
            Comm.powerDown();
        }
        Comm.connecthandler = connectH;
        Comm.mRWLHandler = mRWLHandler;
        Comm.Connect();
    }

    @SuppressLint("HandlerLeak")
    public Handler connectH = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                UHF001.mhandler = mRWLHandler;
                if (null != rfidOperate)
                    rfidOperate.mHandler = mRWLHandler;
                if (null != Comm.uhf6)
                    UHF006.UHF6handler = mRWLHandler;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler mRWLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Bundle b = msg.getData();
                switch (Comm.opeT) {
                    case readOpe:
                        String epcErr = b.getString("Err");
                        String epcVal = b.getString("readData");
                        if (epcVal != "") {
                            epcData = epcVal;
                            //Toast.makeText(MainActivity.this, "Read Fail" + epcData, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("epcErr", epcErr);
                            Toast.makeText(MainActivity.this, "未获取标签,请重试", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case getQ:
                        String userErr = b.getString("Err");
                        String userVal = b.getString("readData");
                        if (userVal != "") {
                            userData = StringUtils.hexStr2Str(userVal);
                            //Toast.makeText(MainActivity.this, "Read Fail" + userData, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("userErr", userErr);
                            Toast.makeText(MainActivity.this, "未获取标签,请重试", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case writeOpe:
                        boolean isWriteSucceed = b.getBoolean("isWriteSucceed");
                        if (isWriteSucceed) {
                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            Log.d("UHF", "Write Succeed");
                        } else {
                            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case writeepcOpe:
                        boolean isWriteEPCSucceed = b.getBoolean("isWriteSucceed");
                        if (isWriteEPCSucceed) {
                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case lockOpe:
                        boolean isLockSucceed = b.getBoolean("isLockSucceed");
                        if (isLockSucceed)
                            Toast.makeText(MainActivity.this, "Lock Succeed", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(MainActivity.this, "Lock Fail", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 扫描枪，扫描回掉
     */
    @SuppressLint("HandlerLeak")
    public Handler scanHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ScanOperate.MESSAGE_TEXT:
                    String code = (String) msg.obj;
                    scanOperate.setVibratortime(50);
                    scanOperate.mediaPlayer();
                    Log.i("barcode", code);
                    webViewPost(code,"scanSN");
                    //webView.loadUrl("javascript:AndroidToWeb('" + code + "')");
                    break;
                case 2:
                    break;
            }
        }
    };

    class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void toRead(final String orderType, final String callback) {
            if (orderType.equalsIgnoreCase("readEpc")) {
                epcData = "";
                Comm.opeT = Comm.operateType.readOpe;
                readTag(0, 1, "6", "2", 0);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                }
                webViewPost(epcData, callback);
            } else if (orderType.equalsIgnoreCase("readUser")) {
                userData = "";
                Comm.opeT = Comm.operateType.getQ;
                readTag(0, 3, "11", "0", 0);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                }
                webViewPost(userData, callback);
            }
        }

        @android.webkit.JavascriptInterface
        public void toWrite(final String orderType, final String strWriteData) {
            if (orderType.equalsIgnoreCase("writeEpc")) {
                Comm.opeT = Comm.operateType.writeepcOpe;
                writeTag(0, 1, "6", "2", 0, strWriteData);
            } else if (orderType.equalsIgnoreCase("writeUser")) {
                Comm.opeT = Comm.operateType.writeOpe;
                writeTag(0, 3, "11", "0", 0, StringUtils.str2HexStr(strWriteData));
            }
        }
    }
}
