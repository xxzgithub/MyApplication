package xiexingzhang.bawei.com.myapplication.utils;

import xiexingzhang.bawei.com.myapplication.bean.Bean;

/**
 * autohor:谢兴张(asus)
 * date:2017/4/16
 * effect:
 */

public class GsonUtils {
    public static Bean changeChar(String str){
        Gson gson = new Gson();
        Bean bean1 = gson.fromJson(str, Bean.class);
        return bean1;
    }
}
