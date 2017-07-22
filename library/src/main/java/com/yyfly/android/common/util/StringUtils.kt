package com.yyfly.android.common.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * 字符串工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object StringUtils {

    /**
     * Checks if a CharSequence is empty ("") or null.
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().
     *
     * @param cs  the CharSequence to check, may be null
     * @return `true` if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    fun isEmpty(cs: CharSequence?): Boolean {
        return cs == null || cs.length == 0
    }

    /**
     * Checks if a CharSequence is not empty ("") and not null.
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return `true` if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
     */
    fun isNotEmpty(cs: CharSequence): Boolean {
        return !isEmpty(cs)
    }

    /**
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return `true` if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    fun isBlank(cs: CharSequence?): Boolean {
        if (cs == null || cs.length == 0) {
            return true
        }
        val strLen: Int = cs.length
        for (i in 0..strLen - 1) {
            if (!Character.isWhitespace(cs[i])) {
                return false
            }
        }
        return true
    }

    /**
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return `true` if the CharSequence is not empty and not null and not whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
     */
    fun isNotBlank(cs: String?): Boolean {
        return !isBlank(cs)
    }

    /**
     * Removes control characters (char &lt;= 32) from both
     * ends of this String, handling `null` by returning `null`.
     * The String is trimmed using [String.trim].
     * Trim removes start and end characters &lt;= 32.
     * To trim your choice of characters, use the
     * <pre>
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed string, `null` if null String input
     */
    fun trim(str: String?): String? {
        return str?.trim { it <= ' ' }
    }

    /**
     * Removes control characters (char &lt;= 32) from both
     * ends of this String returning `null` if the String is
     * empty ("") after the trim or if it is `null`.
     * The String is trimmed using [String.trim].
     * Trim removes start and end characters &lt;= 32.
     * <pre>
     * StringUtils.trimToNull(null)          = null
     * StringUtils.trimToNull("")            = null
     * StringUtils.trimToNull("     ")       = null
     * StringUtils.trimToNull("abc")         = "abc"
     * StringUtils.trimToNull("    abc    ") = "abc"
     * </pre>
     * @param str  the String to be trimmed, may be null
     * @return the trimmed String,`null` if only chars &lt;= 32, empty or null String input
     * @since 2.0
     */
    fun trimToNull(str: String): String? {
        val ts = trim(str)
        return if (isEmpty(ts)) null else ts
    }


    /**
     * 处理字符为null ，如果null 返回空白字符串
     *
     * @param  str
     * @return
     */
    fun trimNull(str: String): String {
        if (isNotEmpty(str)) {
            return str
        }
        return ""
    }

    /**
     * 不为null，也不是全是空白字符
     *
     * @param string
     * @return
     */
    fun isNotNullAndNotEmpty(string: String?): Boolean {
        return string != null && string.trim { it <= ' ' }.length > 0
    }

    /**
     * 只含有汉字、数字、字母、下划线不能以下划线开头和结尾
     *
     * @param str
     * @return
     */
    fun StringFilter(str: String): Boolean {
        val regEx = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 过滤特殊字符
     *
     * @param str
     * @return
     */
    fun StringReplace(str: String): String {
        val regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").replace("[\\[\\]]".toRegex(), "").trim { it <= ' ' }
    }

    /**
     * get length of CharSequence
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     * @param str
     * @return if str is null or empty, return 0, else return [CharSequence.length].
     */
    fun length(str: CharSequence?): Int {
        return str?.length ?: 0
    }

    /**
     * null Object to empty string
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * @param str
     * @return
     */
    fun nullStrToEmpty(str: Any?): String {
        return if (str == null) "" else str as? String ?: str.toString()
    }

    /**
     * capitalize first letter
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * @param str
     * @return
     */
    fun capitalizeFirstLetter(str: String): String {
        if (isEmpty(str)) {
            return str
        }

        val c = str[0]
        return if (!Character.isLetter(c) || Character.isUpperCase(c))
            str
        else
            StringBuilder(str.length)
                    .append(Character.toUpperCase(c)).append(str.substring(1)).toString()
    }

    /**
     * encoded in utf-8
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    fun utf8Encode(str: String): String {
        if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                return URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException("UnsupportedEncodingException occurred. ", e)
            }

        }
        return str
    }

    /**
     * encoded in utf-8, if exception, return defaultReturn
     *
     * @param str
     * @param defaultReturn
     * @return
     */
    fun utf8Encode(str: String, defaultReturn: String): String {
        if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                return URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                return defaultReturn
            }
        }
        return str
    }

    /**
     * get innerHtml from href
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return
     * *          * if href is null, return ""
     * *          * if not match regx, return source
     * *          * return the last string that match regx
     */
    fun getHrefInnerHtml(href: String): String {
        if (isEmpty(href)) {
            return ""
        }
        val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
        val hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE)
        val hrefMatcher = hrefPattern.matcher(href)
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1)
        }
        return href
    }

    /**
     * process special char in html
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    fun htmlEscapeCharsToString(source: String): String {
        return if (isEmpty(source))
            source
        else
            source.replace("&lt;".toRegex(), "<").replace("&gt;".toRegex(), ">").replace("&amp;".toRegex(), "&").replace("&quot;".toRegex(), "\"")
    }

    /**
     * transform half width char to full width char
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * @param s
     * @return
     */
    fun fullWidthToHalfWidth(s: String): String {
        if (isEmpty(s)) {
            return s
        }
        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i].toInt() == 12288) {
                source[i] = ' '
            } else if (source[i].toInt() in 65281..65374) {
                source[i] = (source[i].toInt() - 65248).toChar()
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    /**
     * transform full width char to half width char
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     *</pre>
     * @param s
     * @return
     */
    fun halfWidthToFullWidth(s: String): String {
        if (isEmpty(s)) {
            return s
        }
        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i] == ' ') {
                source[i] = 12288.toChar()
            } else if (source[i].toInt() in 33..126) {
                source[i] = (source[i].toInt() + 65248).toChar()
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    /**
     * 格式化整数字符串
     *
     * @param intStr
     * @return
     */
    fun stringToInt(intStr: String): String {
        var str = "0"
        if (StringUtils.isNotNullAndNotEmpty(intStr)) {
            if (intStr.contains(".")) {
                str = intStr.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else {
                str = intStr
            }
        }
        return str
    }


    /**
     * 格式化url路径
     *
     * @param urlPath the url path
     * @return the string
     */
    fun formatUrlPath(urlPath: String): String {
        if (urlPath.indexOf("//") != -1) {
            return formatUrlPath(urlPath.replace("//", "/"))
        }
        return urlPath
    }

}
