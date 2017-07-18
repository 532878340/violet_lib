package com.violet.library.utils;

import android.text.TextUtils;

import com.violet.library.manager.NetManager;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description：String工具类
 * author：JimG on 16/10/12 15:16
 * e-mail：info@zijinqianbao.com
 */

public class StringUtils {

    /**
     * 根据uri,返回图片的绝对路径
     * @param imgUrl
     * @return
     */
    public static String parseImageUrl(String imgUrl){
        if(!TextUtils.isEmpty(imgUrl)){
            if(!imgUrl.startsWith("http")){
                imgUrl = NetManager.getServerRoot() + imgUrl;
            }
        }
        return imgUrl;
    }

    /**
     * 字节转16进制
     * @param b
     * @return
     */
    public static  String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    /**
     * 格式化字符串
     */
    private static DecimalFormat df;

    public static String getDecimalFormat(String value){
        return getDecimalFormat(Double.valueOf(value));
    }

    public static String getDecimalFormat(double value)
    {
        if(df == null){
            df = new DecimalFormat("###########0.00");
        }
        return df.format(value);
    }

    /**
     * 返回千分位格式金额
     * @param value
     * @return
     */
    public static String getDecimalFormatFen(double value){
        return new DecimalFormat("###,###,##0.00").format(value);
    }

    private static final String REG_CHINESE = "^[\u4E00-\u9FA5]+$";

    /**
     * 保留前4位和后4位，其他的以*号替代
     * @param text
     * @return
     */
    public static String parse4Hint(String text) {
        if (text == null || text.length() < 8) {
            return text;
        }
        final int length = text.length();
        StringBuffer regular = new StringBuffer("(\\d{4})\\d{");
        regular.append(length - 8);
        regular.append("}(\\d{4})");
        return text.replaceAll(regular.toString(), "$1****$2");
    }

    /**
     * 保留前3位和后4位，其他的以*号替代
     * @param text
     * @return
     */
    public static String parse3_4Hint(String text) {
        if (text == null || text.length() < 7) {
            return text;
        }
        final int length = text.length();
        StringBuffer sb = new StringBuffer();
        sb.append(text.substring(0, 3));
        sb.append("****");
        sb.append(text.substring(length - 4, length));
        return sb.toString();
    }

    /**
     * 保留前4位和后3位，其他的以*号替代
     * @param text
     * @return
     */
    public static String parse4_3Hint(String text) {
        if (text == null || text.length() < 7) {
            return text;
        }
        final int length = text.length();
        StringBuffer sb = new StringBuffer();
        sb.append(text.substring(0, 4));
        sb.append("****");
        sb.append(text.substring(length - 3, length));
        return sb.toString();
    }

    /**
     * 格式化邮箱
     * @param email
     * @return
     */
    public static String parseEmail(String email){
        if(!isEmail(email)){
            return email;
        }

        int split = email.indexOf("@");
        StringBuffer sb = new StringBuffer();
        String prefex = email.substring(0,split);
        if(prefex.length() > 2){
            prefex = prefex.substring(0,2) + "**";
        }
        sb.append(prefex);
        sb.append(email.substring(split));
        return sb.toString();
    }

    /**
     * 将金额以千分位的格式显示，并保留两位小数
     * @return
     */
    public static String dividerAmount(double amount) {
        return String.format("%,.2f", amount);
    }

    /**
     * 将金额以千分位的格式显示，并保留两位小数
     * @return
     */
    public static String dividerAmount(Double amount) {
        if (amount == null) {
            return dividerAmount(0d);
        }
        return dividerAmount(amount.doubleValue());
    }

    /**
     * 将金额以千分位的格式显示，并保留两位小数
     * @return
     */
    public static String dividerAmount(float amount) {
        return String.format("%,.2f", amount);
    }

    /**
     * 将金额以千分位的格式显示
     * @return
     */
    public static String dividerAmount(long amount) {
        return String.format("%,d", amount);
    }

    /**
     * 将金额以千分位的格式显示
     * @return
     */
    public static String dividerAmount(Long amount) {
        if (amount == null) {
            return dividerAmount(0L);
        }
        return dividerAmount(amount.longValue());
    }

    /**
     * 格式化为1位小数
     * @param value
     * @return
     */
    public static String formatDecimal1(float value) {
        return String.format("%.1f", value);
    }

    /**
     * 格式化为2位小数
     * @param value
     * @return
     */
    public static String formatDecimal2(double value) {
        return String.format("%.2f", value);
    }

    /**
     * 判断字符是否为汉字
     * @param text
     * @return
     */
    public static boolean isChinese(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return text.toString().matches(REG_CHINESE);
    }

    /**
     * 判断字符是否为汉字
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        return String.valueOf(c).matches(REG_CHINESE);
    }

    /**
     * 使用空格格式化手机号(3_4_4)
     * @return
     */
    public static String formatPhoneNo(String phoneNo) {
        if (phoneNo == null || phoneNo.length() < 11) {
            return phoneNo;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(phoneNo.substring(0, 3));
        sb.append(" ");
        sb.append(phoneNo.substring(3, 7));
        sb.append(" ");
        sb.append(phoneNo.substring(7, phoneNo.length()));
        return sb.toString();
    }

    /**
     * 判断是否是Email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^(1[0-9][0-9])\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String htmlRemoveTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_space;
        java.util.regex.Matcher m_space;
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
            //定义空格回车换行符
            String REGEX_SPACE = "\\s*|\t|\r|\n";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
            m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll("");// 过滤空格回车标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 替换Html< >标签
     * @param input
     * @return
     */
    public static String replaceHtml(String input){
        return input.replaceAll("&lt;","<").replaceAll("&gt;",">");
    }

    /**
     * 去除Html空格
     * @param input
     * @return
     */
    public static String delSpace(String input){
        // 定义HTML标签的正则表达式
        String REGEX_HTML = "<[^>]+>";
        //定义空格回车换行符
        String REGEX_SPACE = "\\s*|\t|\r|\n";

        Pattern pattern = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        input = matcher.replaceAll("");
        pattern = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    /**
     * 将map 转为 string
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) { return ""; }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,String> entry : map.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.lastIndexOf("&"));
        }
        return s;
    }
}
