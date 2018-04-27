package com.wanlong.iptv.app;

import android.app.Application;
import android.content.Intent;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.squareup.leakcanary.LeakCanary;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.CrashHandler;
import com.wanlong.iptv.utils.DeviceUuidFactory;
import com.wanlong.iptv.utils.Utils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by lingchen on 2018/1/24. 13:19
 * mail:lingchen52@foxmail.com
 */
public class App extends Application {

    private static App application;
    public static UUID sUUID;
    public static final boolean RELEASE_VERSION = true;
    public static final boolean PRISON = true;
    public static long newtime;
    public static String mac = "";
    public static String adText = "";
    public static boolean ADserver = false;
    public static boolean look_permission = false;

    public static App getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mac = Utils.getMac(this);
        getUUID();
        initLeakcanary();
        initLogger();
        initOkGo();
        initPlayer();
        AutoLayoutConifg.getInstance().useDeviceSize();
        CrashHandler.getInstance().init(this);//全局异常捕获
    }

    //得到UUID
    public void getUUID() {
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(this);
        sUUID = deviceUuidFactory.getDeviceUuid();
    }

    //设定播放器全局参数
    private void initPlayer() {
        GSYVideoType.enableMediaCodec();//开启硬解码
        GSYVideoType.enableMediaCodecTexture();//使能硬解码渲染优化
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        Debuger.enable();
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    //初始化OkGo
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("Hotel-OkGo");
        //log打印级别，决定了log显示的详细程度
        if (RELEASE_VERSION) {
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        }
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //全局的读取超时时间
        builder.readTimeout(2000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(2000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(2000, TimeUnit.MILLISECONDS);

        //使用内存保持cookie，app退出后，cookie消失
//        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(2);                          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        //.addCommonHeaders(headers)                      //全局公共头
        //.addCommonParams(params);
    }

    //初始化Logger
    public void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default curtain2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("Hotel-Log")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            //项目上线前，可以实现以下方法，以保证上线后不输出日志。
            @Override
            public boolean isLoggable(int priority, String tag) {
                if (RELEASE_VERSION) {
                    return false;//release版本不输出日志
                } else {
                    return true;
                }
            }
        });

        //Save logs to the file
        //Add custom tag to Csv format strategy
        FormatStrategy diskformatStrategy = CsvFormatStrategy.newBuilder()
                .tag("Hotel-Log")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(diskformatStrategy));
    }

    //初始化Leakcanary
    public void initLeakcanary() {
        if (!RELEASE_VERSION) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            if (PRISON) {

            } else {
                LeakCanary.install(this);
            }
        }
    }

    //退出应用
    public void exit() {
        stopService(new Intent(this, AdService.class));
        ADserver = false;
        ActivityCollector.finishAll();
        System.exit(0);
    }
}

