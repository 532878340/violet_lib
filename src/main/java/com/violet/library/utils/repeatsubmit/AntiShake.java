package com.violet.library.utils.repeatsubmit;

/**
 * description：
 * author：JimG on 2017/6/27 13:45
 * e-mail：info@zijinqianbao@qq.com
 */


public class AntiShake {
    private static LimitQueue<OneClick> queue = new LimitQueue<>(20);

    public static boolean check(Object o) {
        String flag;
        if(o == null) {
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        } else {
            flag = o.toString();
        }
        for (OneClick util : queue.getArrayList()) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClick clickUtil = new OneClick(flag);
        queue.offer(clickUtil);
        return clickUtil.check();
    }
}
