package com.robam.roki.utils;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class StringUtil {


    public static String join(String join, String[] strAry) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }

    public static boolean isEmpty(String value) {

        return value == null || value.length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为null
     *
     * @param input 给定的字符串
     * @return 如果字符串为null，返回true，否则返回false
     */
    public static boolean isNull(String input) {

        return input == null;
    }


    /**
     * 判断字符串是否为空或null
     *
     * @param input 给定的字符串
     * @return 如果字符串为空或null，返回true，否则返回false
     */
    public static boolean isNullOrEmpty(String input) {

        return StringUtil.isNull(input) || input.trim().equals("");
    }


    /**
     * 清除 null 对象
     *
     * @param obj
     * @return String
     */
    public static String clearNullString(Object obj) {

        if (obj == null || "null".equals(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }


    public static boolean isUpperLetter(char c) {

        return c >= 'A' && c <= 'Z';
    }


    public static boolean isLowerLetter(char c) {

        return c >= 'a' && c <= 'z';
    }


    public static boolean isLetter(char c) {

        return isUpperLetter(c) || isLowerLetter(c);
    }


    public static boolean isDigitalChar(char c) {

        return c >= '0' && c <= '9';
    }


    public static boolean isLatinChar(char c) {

        return isLetter(c) || isDigitalChar(c);
    }


    public static boolean isNormalPlateNo(String plateNo) {

        if (plateNo == null) {
            return false;
        }
        String reg =
                "^[\u4e00-\u9fa5][A-Za-z](-)?[A-HJ-NP-Za-hj-np-z0-9]{4}[A-HJ-NP-Za-hj-np-z0-9\u4e00-\u9fa5][A-HJ-NP-Za-hj-np-z0-9]?$";
        return Pattern.compile(reg).matcher(plateNo).matches();
    }


    public static int countNotEmptyString(String[] strs) {

        if (strs == null)
            return 0;
        int count = 0;
        for (String str : strs) {
            if (str != null && str.length() > 0) {
                ++count;
            }
        }
        return count;
    }


    /**
     * @param length   the string's length
     * @param position defined as Python
     * @return
     */
    private static int handleStringPosition(int length, int position) {

        if (position < 0) {
            position += length;
        }
        if (position < 0) {
            position = 0;
        } else if (position > length) {
            position = length;
        }
        return position;
    }


    public static String repeatString(String rep, int count) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(rep);
        }
        return sb.toString();
    }


    public static String subString(String source, int start, int end) {

        int length = source.length();
        start = handleStringPosition(length, start);
        end = handleStringPosition(length, end);
        if (start >= end) {
            return "";
        }
        return source.substring(start, end);
    }


    /**
     * @param str
     * @param start
     * @param end
     * @param maskChar
     * @return
     */
    public static String maskString(String str, int start, int end,
                                    char maskChar) {

        int length = str.length();
        start = handleStringPosition(length, start);
        end = handleStringPosition(length, end);

        if (start >= end) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.substring(0, start));
        for (int i = start; i < end; ++i) {
            sb.append(maskChar);
        }
        sb.append(str.substring(end, length));
        return sb.toString();
    }


    public static String maskString(String str, int start, int end,
                                    String maskChar) {

        int length = str.length();
        start = handleStringPosition(length, start);
        end = handleStringPosition(length, end);

        if (start >= end) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.substring(0, start));
        sb.append(maskChar);
        sb.append(str.substring(end, length));
        return sb.toString();
    }


    public static String toMoneyString(Integer fee) {

        if (fee == null)
            return "0.00";

        String format = fee >= 0 ? "%d.%02d" : "-%d.%02d";
        fee = Math.abs(fee);
        return String.format(format, fee / 100, fee % 100);
    }


    public static String toMoneyString(Long fee) {
        fee = Math.abs(fee);
        if (fee == null)
            return "0.00";

        String format = fee >= 0 ? "%d.%02d" : "-%d.%02d";
        fee = Math.abs(fee);
        return String.format(format, fee / 100, fee % 100);
    }

    /**
     * 折扣
     *
     * @param fee
     * @return
     */
    public static String toDiscountString(Double fee) {
        if (fee == null || 100 <= fee)
            return "全额";
        fee = Math.abs(fee);
        int value = (int) (fee * 10);
        if (value % 100 == 0) {
            return value / 100 + "折";
        } else if (value % 10 == 0) {
            return String.format("%d.%d折", value / 100, (value % 100) / 10);
        } else {
            return String.format("%.2f折", fee / 10);
        }

    }

    public static String toDiscountString(Integer fee) {

        if (fee == null)
            return "0.0";

        return String.format("%d.%d", fee / 10, fee % 10);
    }


    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();


    public static String byteArray2hexString(byte[] data) {

        if (data == null)
            return null;
        if (data.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(HEX_CHARS[(b & 0xf0) >>> 4]);
            sb.append(HEX_CHARS[b & 0x0f]);
        }
        return sb.toString();
    }


    public static byte[] hexString2byteArray(String hexString) {

        if (hexString.length() % 2 != 0)
            throw new IllegalArgumentException("hexString must be even length");
        byte[] data = new byte[hexString.length() / 2];
        for (int i = 0; i < data.length; ++i) {
            int base = 0, shift = 0;
            while (shift <= 4) {
                char ch = hexString.charAt(i * 2 + 1 - shift / 4);
                if (ch >= '0' && ch <= '9')
                    base |= (ch - '0') << shift;
                else if (ch >= 'A' && ch <= 'F')
                    base |= (ch - 'A' + 10) << shift;
                else if (ch >= 'a' && ch <= 'f')
                    base |= (ch - 'a' + 10) << shift;
                else
                    throw new IllegalArgumentException("invalid char: " + ch);
                shift += 4;
            }
            data[i] = (byte) base;
        }
        return data;
    }

    //单位换算
    public static String toShortMoneyString(long fee) {

        return toMoneyString(fee);
//		fee = Math.abs(fee);
//		String yuan = Long.toString(fee / 100);
//		fee %= 100;
//		if (fee >= 10) {
//			return yuan + "." + (fee % 10 == 0 ? fee / 10 : fee);
//		} else if (fee > 0) {
//			return yuan + ".0" + fee;
//		} else {
//			return yuan;	
//		}
    }

    public static String toShortMoneyStringForPrepay(String fee) {
        long prepay = 0;
        try {
            prepay = Long.valueOf(fee);
        } catch (Exception e) {
            // TODO: handle exception
            return "0";
        }
        prepay = Math.abs(prepay);
        String yuan = Long.toString(prepay / 100);
        prepay %= 100;
        if (prepay >= 10) {
            return yuan + "." + (prepay % 10 == 0 ? prepay / 10 : prepay);
        } else if (prepay > 0) {
            return yuan + ".0" + prepay;
        } else {
            return yuan;
        }
    }

    /**
     * 排除List中的重复元素
     *
     * @param li
     * @return
     */
    public static List<String> getNewList(List<String> li) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < li.size(); i++) {
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if (!list.contains(str)) {   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }

    /* 哈希化字符串的Horner方法： a4*n^4 + a3*n^3 + a2*n^2 + a1*n^1 + a0*n^0
       可写成 (((a4*n+a3)*n + a2)*n + a1)*n + a0
       我们从括号最内端开始计算，渐渐向外扩展。这里a4、a3、a2、a1、a0均代表字符串中的字符码，
       通常取a=1,b=2····依次类推，为了保证所有字符串能被唯一编码表示
       n一般取比26大的数，这里我们取32，乘32可以左移实现，提高效率。即代码中的 <<5。
     */
    public static int hash(String key) {
        int arraySize = 11113;            //数组大小一般取质数
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) {        //从字符串的左边开始计算
            int letterValue = key.charAt(i) - 96;//将获取到的字符串转换成数字，比如a的码值是97，则97-96=1 就代表a的值，同理b=2；
            hashCode = ((hashCode << 5) + letterValue) % arraySize;//防止编码溢出，对每步结果都进行取模运算
        }
        return hashCode;
    }

    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("####.00");

        double Byte = 1024F;
        double KB = Byte * 1024F;
        double MB = KB * 1024F;
        double GB = MB * 1024F;
        double TB = GB * 1024F;
        double PB = TB * 1024F;
        double EB = PB * 1024F;
        double ZB = EB * 1024F;
        double YB = ZB * 1024F;

        if (size < 0) {
            throw new IllegalArgumentException("file size error:" + size);
        }

        if (size < Byte) // 小于1KB
        {
            return size + " Byte";
        } else if (size < KB) // 小于1MB
        {
            double kSize = size / Byte;
            return df.format(kSize) + " KB";
        } else if (size < MB) // 小于1GB
        {
            double mSize = size / KB;
            return df.format(mSize) + " MB";
        } else if (size < GB) // 小于1TB
        {
            double gSize = size / MB;
            return df.format(gSize) + " GB";
        } else if (size < TB) // 小于1PB
        {
            double tSize = size / GB;
            return df.format(tSize) + " TB";
        } else if (size < PB) // 小于1EB
        {
            double pSize = size / TB;
            return df.format(pSize) + " PB";
        } else // xxEB表示
        {
            double eSize = size / PB;
            return df.format(eSize) + " EB";
        }
    }


    /**
     * 16进制转换为10进制
     *
     * @param o
     * @return
     */
    public static int toParseInt(Object o) {
        String s = getnewLengthStr(o, 6);
        return Integer.parseInt(s, 16);
    }
    /**
     * 变换字符长度 前加0
     */
    public static String getnewLengthStr(Object str, int i) {
        String ss = processNull(str);
        if (ss.length() > i) {
            ss = ss.substring(ss.length() - i, ss.length());
        }
        while (ss.length() < i) {
            ss = "0" + ss;
        }
        return ss;
    }
    /**
     * 判断是否为null返回字符串
     */
    public static String processNull(Object value) {
        return ((value == null) ? "" : value.toString());
    }

}
