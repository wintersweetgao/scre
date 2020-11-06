package edu.hfu.scre.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class FormatUtil {
	public static long DAY = 24 * 60 * 60 * 1000L; //一天的毫秒数
	public static String TIME_FORMAT_Millis = "yyyyMMddHHmmssSSS" ;
	/**
	     * 时间型字符串转换成date型
	  * @param dateStr
	  * @param dateFormat
	  * @return
	  */
    public static Date strToDate(String dateStr, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);  
		Date date = new Date();  
		try {
			date = simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	 }
	/**
	 * 将Date转化为指定格式并返回为字符串类型
	 * @param date
	 * @param dataFormat
	 * @return
	 */
	public static String formatDateToStr(Date date, String dataFormat) {
		SimpleDateFormat todaySDF = new SimpleDateFormat(dataFormat);
		return todaySDF.format(date);
	}
	/**
	 * 将Date转化为指定格式
	 * @param date
	 * @param dataFormat
	 * @return
	 */
	public static Date formatDate(Date date, String dataFormat){
		try {
			SimpleDateFormat todaySDF = new SimpleDateFormat(dataFormat);
			String strDate=todaySDF.format(date);
			return todaySDF.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 以一定格式返回当前时间的毫秒数
	 * @return
	 */
	public static String cunrrentDateMillis(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_Millis, Locale.SIMPLIFIED_CHINESE);
			return sdf.format(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 为Date设置指定的时分秒 HH:mm:ss
	 * @param date
	 * @param timestr
	 * @return
	 */
	public static Date dateSetHHmmssToDate(Date date, String timestr){
		String datestr=formatDateToStr(date,"yyyy-MM-dd");
		datestr=datestr+" "+timestr;
		return strToDate(datestr,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	   * Date转化为XMLGregorianCalendar
	   * @param date
	   * @return
	   */
	public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {  
	    GregorianCalendar cal = new GregorianCalendar();  
	    cal.setTime(date);  
	    XMLGregorianCalendar gc = null;  
	    try {  
	    gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);  
	    } catch (Exception e) {  
	         e.printStackTrace();  
	    }  
	    return gc;  
	}
	/**
	 * 获取指定天数后的日期
	 * 
	 * **/
	public static Date getDateAdd(Date d, int days) throws Exception {
		Date r = new Date();
		r.setTime(d.getTime() + days * DAY);
		return r;
	}
	/**根据金额和税率计算贷方值和税金（从SQL语句直接算了，此方法保留备用）
	 * */
	public Map<String,Double> calculateAmount(Double sumAmount,Double tax_rate) throws Exception {
		Map<String,Double> map=new HashMap<String, Double>();
		BigDecimal amountDB = new BigDecimal(sumAmount);
		BigDecimal taxDB = new BigDecimal(tax_rate);
		BigDecimal temp1=new BigDecimal(1.00);
		BigDecimal temp2=new BigDecimal(-1.00);
		System.out.println("------"+((sumAmount/(1+tax_rate))*(1-tax_rate)-sumAmount));
		// 税=(借方合计/(1+税率))*(1-税率）-借方合计
		BigDecimal cal_taxDB=(amountDB.divide(temp1.add(taxDB),2,RoundingMode.HALF_UP).multiply(temp1.subtract(taxDB))).subtract(amountDB);
		//贷方合计 (税+借方合计)*(-1)
		BigDecimal cal_amountDB=(cal_taxDB.add(amountDB)).multiply(temp2);
		map.put("credit_amount", cal_amountDB.doubleValue());
		map.put("tax_amount", cal_taxDB.doubleValue());
		return map;
	}
	
	public static List<Integer> getYearLs(){
		List<Integer> ls=new ArrayList<>();
		int begin=1999;
		int end=Integer.parseInt(formatDateToStr(new Date(),"yyyy"));
		for(int i=end;i>=begin;i--) {
			ls.add(i);
		}
		return ls;
		
	}
	public static String getEntityName(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf('.')+1);
	}
	/**
	 * 获取两个日期相差的月数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getMonthDiff(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		int year1 = c1.get(Calendar.YEAR);        
		int year2 = c2.get(Calendar.YEAR);        
		int month1 = c1.get(Calendar.MONTH);        
		int month2 = c2.get(Calendar.MONTH);        
		int day1 = c1.get(Calendar.DAY_OF_MONTH);        
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		// 获取年的差值 
		int yearInterval = year1 - year2;
		// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
		if (month1 < month2 || month1 == month2 && day1 < day2) {
			yearInterval--;
		}
		// 获取月数差值
		int monthInterval = (month1 + 12) - month2;
		if (day1 < day2) {
			monthInterval--;
		}
		monthInterval %= 12;
		int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
		return monthsDiff;
	}
	
	
	public static String percentByNum(int num1,int num2) {
		if (num2==0) {
			return "0.00%";
		}else {
			NumberFormat numberFormat = NumberFormat.getInstance();  
			// 设置精确到小数点后2位  
			numberFormat.setMaximumFractionDigits(2);  
			String result = numberFormat.format((float) num1 / (float) num2 * 100);  
			return result+"%";
		}
	}
	public static void main(String[] args) {
		Date d1=FormatUtil.strToDate("2018-12-21", "yyyy-HH-dd");
		Date d2=FormatUtil.strToDate("2019-12-31", "yyyy-HH-dd");
		String s1 = FormatUtil.formatDateToStr(new Date(),"yyyy-MM-dd");
		
		System.out.println(FormatUtil.getMonthDiff(d1,d2));
		System.out.println(s1);
	}
}
