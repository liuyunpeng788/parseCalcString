import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class parseCalcString {
   private static String strCalc = "6 * ( 5 + ( 2 + 3) * 8 + 3)";
   
   /**
    * 比较字符ch1 和ch2的优先级。如果ch1的优先级高，则返回true,直接入栈。否则，返回false
    * 因为对于四则运算，相同优先级是从左到右先运算
    * @param ch1 字符串中的元素
    * @param ch2 栈顶元素
    * @return
    */
   private static Boolean chkGreaterPriority(final char ch1, final  char ch2){
	   if(ch2 == '(' || ch2 == ')'){  //如果栈顶元素是这些元素，直接入栈
		   return true;  
	   }else if(ch1 == '*' || ch1 == '/'){
		   if(ch2 == '+' || ch2 == '-' ){
			   return true;
		   }else{
			   return false;
		   }
	   } 
		   return false;
	
   }
   
   /**
    * 中缀表达式转后缀表达式
    * @param strCalc
    * @return
    */
   @SuppressWarnings("unchecked")
   private final static List<String> midfixToSurfix(final String strCalc){

	   List<String> list = new ArrayList<String>();
	   @SuppressWarnings("rawtypes")
	   Stack st = new Stack();
	   StringBuffer sb = new StringBuffer();
	   
	   for(int i = 0 ; i < strCalc.length(); i++){
		  
		   char ch = strCalc.charAt(i);
		   
		   //如果当前的字符串包含字母，则表示该字符串无法解析
		   if((ch >='A' && ch <= 'Z')|| (ch >='a' && ch <= 'z')){
			   return null;
		   }
		   //判断ch 是否是数字或小数点
		   if(ch == '.' || (ch >='A' && ch <= 'Z')|| (ch >='a' && ch <= 'z')|| (ch >='0' && ch <= '9')){  //是否是字母、数字或小数点
//		   if(ch == '.' || (ch >='0' && ch <= '9')){
			    sb.append(ch);
			    continue;
		   }else if(ch == ' '){
			   continue;
		   }else{  //如果它不为数字、字母、及空格，则将stringbuffer当中的值放入list
			   
			   //将Stringbuffer 中的内容转换为字符串，存放到list中
			   if(sb.length() > 0){
				   list.add(sb.toString());
				   sb.setLength(0);
			   }
			   
			   if(st.isEmpty() || ch == '('){
				   st.push(ch);
			   }else if(ch == ')'){
				   // 输出栈中'(' 以上的值
				  while(!st.isEmpty() && (char)st.peek() != '('){
					  String str = String.valueOf(st.pop());
					  list.add(str);
				  }
				  st.pop(); //弹出“(”
				   
			   }else if(!st.isEmpty()  && !chkGreaterPriority(ch,(char)st.peek())){
				  
				   //需要将当前符号与栈顶元素的符号进行比较，根据优先级判断：如果ch的优先级比栈顶元素的优先级低或相等，则弹出栈顶元素，追加到list后，否则，ch入栈。
				   while(!st.isEmpty() && !chkGreaterPriority(ch,(char)st.peek())){
					   //如果当前符号的优先级比栈顶元素的优先级要高，则栈顶元素出栈，添加到list中
					   list.add(String.valueOf(st.pop()));
				   }
				   //将当前符号放入栈中
				   st.push(ch);
			   }else{
				   st.push(ch);
			   }
			   
		   }
			   
			  
		
	   }
	   
	   //将栈中的元素弹出，追加到list后，形成一个逆波兰表达式
	   while(!st.isEmpty()){
		   list.add(String.valueOf(st.pop()));
	   }
	    
      return list;
   }
   
   private final static BigDecimal getCalcResult(final List<String> resList ,Integer precision){
	   Stack<Double> stack = new Stack<Double>();
	   for(String str : resList){
		/*   //如果当前的字符串包含字母，则表示需要查询该指标的值
		   if(str.matches("[a-z|A-Z]+")){
			   //到数据库中查询该指标的值
			   continue;
		   }*/
		   BigDecimal sum = new BigDecimal(0);
		    if(str.equals("+")){
		    	sum = sum.add(new BigDecimal(stack.pop()));
		    	sum = sum.add(new BigDecimal(stack.pop()));
		    	stack.push(sum.doubleValue());
		    }else if(str.equals("-")){
		    	sum = sum.subtract(new BigDecimal(stack.pop()));
		    	sum = sum.subtract(new BigDecimal(stack.pop()));
		    	stack.push(sum.doubleValue());
		    }else if(str.equals("*")){
		    	
		    	sum = new BigDecimal(stack.pop());
		    	sum = sum.multiply(new BigDecimal(stack.pop()));
		    	stack.push(sum.doubleValue());
		    }else if(str.equals("/")){
		    	sum = new BigDecimal(stack.pop());
		    	sum = sum.divide(new BigDecimal(stack.pop()));
		    	stack.push(sum.doubleValue());
		    }else{
		    	stack.push(new BigDecimal(str).doubleValue());
		    }
		    
	   }
	   Integer scale  =  precision == null?0:precision;
	   
	   BigDecimal res = new BigDecimal(stack.pop());
	   return res.setScale(scale,BigDecimal.ROUND_HALF_UP);
   }
 
   /**
    * 计算表达式的值
    * @param strCalc 待计算的表达式
    * @param scale 精确到小数点后多少位
    * @return
    */
   public static final BigDecimal calcExpStr(final String strCalc , final Integer precision){
	   List<String> resList =  midfixToSurfix(strCalc);
	   return getCalcResult(resList ,precision);
   }
   
public  static void main(String[] args){

	   List<String> resList =  midfixToSurfix(strCalc);
	   BigDecimal res = getCalcResult(resList,3);
	  
	   
	   //输出结果
	   System.out.println(res.toString());
   }
}
