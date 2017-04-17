package xiexingzhang.bawei.com.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

import xiexingzhang.bawei.com.myapplication.bean.Bean;
import xiexingzhang.bawei.com.myapplication.bean.Beans;
import xiexingzhang.bawei.com.myapplication.utils.GsonUtils;
import xiexingzhang.bawei.com.myapplication.utils.NetworkUtils;
import xiexingzhang.bawei.com.myapplication.utils.UrlUtils;


public class MainActivity extends AppCompatActivity {
    private SlidingMenu slidingMenu;
    private ImageView main_click;
    private ListView main_list;
    private TextView main_texts;
    private ListView left_list;
    private boolean flag = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    Bean bean1 = GsonUtils.changeChar(str);
                    Log.i("11111", bean1.toString());
                    List<Bean.DataBean> data = bean1.getData();
                    MyAdapter myAdapter = new MyAdapter(data, MainActivity.this);
                    main_list.setAdapter(myAdapter);
                    main_texts.setText("全部药品");
                    break;
                case 2:
                    String str1 = (String) msg.obj;
                    Bean bean2 = GsonUtils.changeChar(str1);
                    Log.i("22222", bean2.toString());
                    List<Bean.DataBean> data1 = bean2.getData();
                    MyAdapter myAdapter1 = new MyAdapter(data1, MainActivity.this);
                    main_list.setAdapter(myAdapter1);
                    main_texts.setText("西药");
                    break;
                case 3:
                    String str2 = (String) msg.obj;
                    Bean bean3 = GsonUtils.changeChar(str2);
                    Log.i("33333", bean3.toString());
                    List<Bean.DataBean> data2 = bean3.getData();
                    MyAdapter myAdapter2 = new MyAdapter(data2, MainActivity.this);
                    main_list.setAdapter(myAdapter2);
                    main_texts.setText("中药");
                    break;
                case 4:
                    String strs = (String) msg.obj;
                    Gson gson = new Gson();
                    Beans beans = gson.fromJson(strs, Beans.class);
                    List<Beans.DataBean> data3 = beans.getData();
                    left_list.setAdapter(new ArrayAdapter<Beans.DataBean>(MainActivity.this, android.R.layout.simple_list_item_1, data3));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initView() {
        leftView();
        main_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                if (slidingMenu.isSecondaryMenuShowing()) {
                    slidingMenu.showContent();
                } else {
                    slidingMenu.showSecondaryMenu();
                }
            }
        });
        new NetworkUtils(handler, UrlUtils.URLPATH1, 1).execNet();
        new NetworkUtils(handler, UrlUtils.URLPATH, 4).execNet();

    }

    private void initData() {
        main_click = (ImageView) findViewById(R.id.main_click);
        main_list = (ListView) findViewById(R.id.main_list);
        main_texts = (TextView) findViewById(R.id.main_texts);
    }

    private void leftView() {
        slidingMenu = new SlidingMenu(this);
        //从左边滑出
        slidingMenu.setMode(SlidingMenu.LEFT);
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        slidingMenu.setBehindOffset(widthPixels / 3 * 1);
        slidingMenu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.leftmenu);
        left_list = (ListView) slidingMenu.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (!slidingMenu.isSecondaryMenuShowing()) {
                        slidingMenu.showContent();
                    }
                    new NetworkUtils(handler, UrlUtils.URLPATH1, 1).execNet();
                } else if (position == 1) {
                    if (!slidingMenu.isSecondaryMenuShowing()) {
                        slidingMenu.showContent();
                    }
                    new NetworkUtils(handler, UrlUtils.URLPATH2, 2).execNet();
                } else if (position == 2) {
                    if (!slidingMenu.isSecondaryMenuShowing()) {
                        slidingMenu.showContent();
                    }
                    new NetworkUtils(handler, UrlUtils.URLPATH3, 3).execNet();
                }
            }
        });
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!slidingMenu.isSecondaryMenuShowing()) {
                slidingMenu.showContent();
            }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if (flag) {
            if (slidingMenu.isMenuShowing()) {
                slidingMenu.showContent();
                flag = false;
            }
        } else {
            finish();
            System.exit(0);
            flag = true;
        }
    }
}

