package com.yyfly.android.common.util

import android.net.ParseException
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * 校验工具
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object ValidateUtils {

    /**
     * 是否数字
     */
    private val REGEX_NUMERIC = "[0-9]*"
    /**
     * 正则表达式：验证汉字
     */
    val REGEX_CHINESE = "^[\\u4e00-\\u9fa5]+"
    /**
     * 验证电话
     */
    private val REGEX_PHONE = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$"
    /**
     * 验证密码，允许特殊字符。不允许^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$
     */
    private val REGEX_PASSWORD = "^(?![^a-zA-Z]+$)(?![^0-9]+$).{6,16}$"
    /**
     * 验证邮箱
     */
    private val REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    /**
     * 验证用户昵称特殊字符
     */
    private val REGEX_NICKNAME = "^[\u4E00-\u9FA5A-Za-z0-9_]{2,12}$"
    /**
     * 日期
     */
    private val REGEX_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$"
    /**
     * 正则表达式：验证URL
     */
    private val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"

    /**
     * 正则表达式：验证IP地址
     */
    private val REGEX_IP_ADDRESS = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"

    /**
     * 验证是否为正确的浮点数
     *
     * @param str the str
     * @return the boolean
     *
     */
    fun isFloatNumeric(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        if (str.substring(0, 1) == ".") {
            return false
        }
        return true
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str the str
     * @return the boolean
     */
    private fun isNumeric(str: String): Boolean {
        return isValidate(str, REGEX_NUMERIC)
    }


    /**
     * 判断字符串是否为日期格式
     *
     * @param strDate the str date
     * @return the boolean
     */
    fun isDate(strDate: String): Boolean {
        return isValidate(strDate, REGEX_DATE)
    }

    /**
     * 验证中文字符

     * @param str the str
     * @return the boolean
     */
    fun isChinese(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        return str.matches((REGEX_CHINESE).toRegex())
    }

    /**
     * 验证邮箱
     *
     * @param email the email
     * @return the boolean
     */
    fun isEmial(email: String): Boolean {
        return isValidate(email, REGEX_EMAIL)
    }


    /**
     * 验证手机号码是否合法
     *
     * @param phone the phone
     * @return the boolean
     */
    fun isPhone(phone: String): Boolean {
        return isValidate(phone, REGEX_PHONE)
    }

    /**
     * 验证密码
     *
     * @param pwd the pwd
     * @return the boolean
     */
    fun isPassword(pwd: String): Boolean {
        return isValidate(pwd, REGEX_PASSWORD)
    }

    /**
     * 验证用户昵称特殊字符
     *
     * @param nickname the nickname
     * @return the boolean
     */
    fun isNickName(nickname: String): Boolean {
        return isValidate(nickname, REGEX_NICKNAME)
    }

    /**
     * 验证URL是否合法
     *
     * @param url the url
     * @return the boolean
     */
    fun isUrl(url: String): Boolean {
        return isValidate(url, REGEX_URL)
    }

    /**
     * 验证IP地址是否合法
     *
     * @param ip the ip
     * @return the boolean
     */
    fun isIpAddress(ip: String): Boolean {
        return isValidate(ip, REGEX_IP_ADDRESS)
    }

    /**
     * 判断银行卡是否合法
     *
     * @param cardId the card id
     * @return the boolean
     */
    fun isBankCard(cardId: String): Boolean {
        val bit = getBankCardCheckCode(cardId.substring(0, cardId.length - 1))
        if (bit == 'N') {
            return false
        }
        return cardId.get(cardId.length - 1) == bit
    }

    /**
     * 根据正则表达式验证是否合法
     *
     * @param value      the value
     * @param patternStr the pattern str
     * @return the boolean
     */
    private fun isValidate(value: String, patternStr: String): Boolean {
        if (!TextUtils.isEmpty(value)) {
            val pattern = Pattern.compile(patternStr)
            val matcher = pattern.matcher(value)
            if (matcher.matches()) {
                return true
            }
        }
        return false
    }

    /**
     * 判断字符串长度是否合法
     *
     * @param text     the text
     * @param minLen   the min len
     * @param maxLen   the max len
     * @return the boolean
     */
    fun validateTextLength(text: String, minLen: Int, maxLen: Int): Boolean {
        if (TextUtils.isEmpty(text)) {
            return false
        }
        if (text.length > maxLen) {
            return false
        }
        if (text.length < minLen) {
            return false
        }
        return true
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId the non check code card id
     * @return the char
     */
    private fun getBankCardCheckCode(nonCheckCodeCardId: String?): Char {
        if (StringUtils.isBlank(nonCheckCodeCardId) || !nonCheckCodeCardId!!.matches(("\\d+").toRegex())) {
            // 如果传的不是数据返回N
            return 'N'
        }

        val chs = nonCheckCodeCardId.trim().toCharArray()
        var luhmSum = 0
        var i = chs.size - 1
        var j = 0
        while (i >= 0) {
            var k = chs[i] - '0'
            if (j % 2 == 0) {
                k *= 2
                k = k / 10 + k % 10
            }
            luhmSum += k
            i--
            j++
        }
        return if ((luhmSum % 10 == 0)) '0' else ((10 - luhmSum % 10).toChar() + '0'.toInt()).toChar()
    }

    /*********************************** 身份证验证开始***********************************/
    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
     * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
     * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
     * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
     * X 9 8 7 6 5 4 3 2
     *
     * 功能：身份证的有效验证
     * @param IDStr 身份证号
     * @return 有效 ：返回"" 无效：返回String信息
     * @throws ParseException the parse exception
     */
    @Throws(ParseException::class)
    fun validateIDCard(IDStr: String): String {
        var errorInfo = ""// 记录错误信息
        val ValCodeArr = arrayOf<String>("1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2")
        val Wi = arrayOf<String>("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2")
        var Ai = ""
// ================ 号码的长度 15位或18位 ================
        if (IDStr.length != 18) {
            errorInfo = "目前只支持18位身份证号码"
            return errorInfo
        }
// =======================(end)========================

// ================ 数字 除最后以为都为数字 ================
        if (IDStr.length == 18) {
            Ai = IDStr.substring(0, 17)
        } else if (IDStr.length == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15)
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。"
            return errorInfo
        }
// =======================(end)========================

// ================ 出生年月是否有效 ================
        val strYear = Ai.substring(6, 10)// 年份
        val strMonth = Ai.substring(10, 12)// 月份
        val strDay = Ai.substring(12, 14)// 日份
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。"
            return errorInfo
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。"
                return errorInfo
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效"
            return errorInfo
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效"
            return errorInfo
        }
// =====================(end)=====================

// ================ 地区码时候有效 ================
        val h = GetAreaCode()
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。"
            return errorInfo
        }
// ==============================================

// ================ 判断最后一位的值 ================
        var TotalmulAiWi = 0
        for (i in 0..16) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt((Ai.get(i)).toString()) * Integer.parseInt(Wi[i])
        }
        val modValue = TotalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai += strVerifyCode

        if (IDStr.length == 18) {
            if (Ai != IDStr) {
                errorInfo = "身份证无效，不是合法的身份证号码"
                return errorInfo
            }
        } else {
            return ""
        }
// =====================(end)=====================
        return ""
    }


    /**
     * 设置地区编码
     *
     * @return the hashtable
     */
    private fun GetAreaCode(): Hashtable<String, String> {
        val hashTable = Hashtable<String, String>()
        hashTable.put("11", "北京")
        hashTable.put("12", "天津")
        hashTable.put("13", "河北")
        hashTable.put("14", "山西")
        hashTable.put("15", "内蒙古")
        hashTable.put("21", "辽宁")
        hashTable.put("22", "吉林")
        hashTable.put("23", "黑龙江")
        hashTable.put("31", "上海")
        hashTable.put("32", "江苏")
        hashTable.put("33", "浙江")
        hashTable.put("34", "安徽")
        hashTable.put("35", "福建")
        hashTable.put("36", "江西")
        hashTable.put("37", "山东")
        hashTable.put("41", "河南")
        hashTable.put("42", "湖北")
        hashTable.put("43", "湖南")
        hashTable.put("44", "广东")
        hashTable.put("45", "广西")
        hashTable.put("46", "海南")
        hashTable.put("50", "重庆")
        hashTable.put("51", "四川")
        hashTable.put("52", "贵州")
        hashTable.put("53", "云南")
        hashTable.put("54", "西藏")
        hashTable.put("61", "陕西")
        hashTable.put("62", "甘肃")
        hashTable.put("63", "青海")
        hashTable.put("64", "宁夏")
        hashTable.put("65", "新疆")
        hashTable.put("71", "台湾")
        hashTable.put("81", "香港")
        hashTable.put("82", "澳门")
        hashTable.put("91", "国外")
        return hashTable
    }


}
