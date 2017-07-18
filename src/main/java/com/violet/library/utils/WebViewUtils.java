package com.violet.library.utils;

import android.os.Build;
import android.webkit.WebView;

import com.violet.library.manager.ConfigsManager;
import com.violet.library.manager.NetManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by perley on 15/11/30.
 * WebView相关工具类
 *
 */
public class WebViewUtils {

    /**
     * 加载html适配界面
     * @param webView
     * @param html
     */
    public static void loadHtml(WebView webView, String html) {
        StringBuilder fitContent = new StringBuilder();
        if (Build.VERSION.SDK_INT < 14)
            fitContent.append("<meta name='viewport' content = 'user-scalable=no, width=device-width, initial-scale=1' />");
        else
            fitContent.append("<meta name='viewport' content = 'user-scalable=no, " +
                    "width=device-width, initial-scale=1'/><style type=text/css>img{max-width: 100%;}</style>");
        fitContent.append(html);
        webView.loadDataWithBaseURL(NetManager.getServerRoot(), fitContent.toString(), "text/html", "utf-8", null);
    }

    /**
     * @param content
     * @param replaceHttp
     * @return
     */
    public String replaceImgSrc(String content, String replaceHttp) {
        if (content != null) {
            Pattern pattern = Pattern.compile("<img[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            // 如果匹配到了img
            while (matcher.find()) {
                if (matcher.group(1).indexOf("http") < 0)
                    content = content.replaceAll(matcher.group(1),
                            (replaceHttp + matcher.group(1)));
            }
        }
        return content;
    }

    /**
     * 替换gt lt 符号
     * @param content
     * @return
     */
    public static String replaceHtmlTag(String content){
        if(content != null){
            content = content.replaceAll("&lt;","<").replaceAll("&gt;",">");
        }
        return content;
    }
}
