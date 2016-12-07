import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class parseCalcString {
   private static String strCalc = "6 * ( 5 + ( 2 + 3) * 8 + 3)";
   
   /**
    * �Ƚ��ַ�ch1 ��ch2�����ȼ������ch1�����ȼ��ߣ��򷵻�true,ֱ����ջ�����򣬷���false
    * ��Ϊ�����������㣬��ͬ���ȼ��Ǵ�����������
    * @param ch1 �ַ����е�Ԫ��
    * @param ch2 ջ��Ԫ��
    * @return
    */
   private static Boolean chkGreaterPriority(final char ch1, final  char ch2){
	   if(ch2 == '(' || ch2 == ')'){  //���ջ��Ԫ������ЩԪ�أ�ֱ����ջ
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
    * ��׺���ʽת��׺���ʽ
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
		   
		   //�����ǰ���ַ���������ĸ�����ʾ���ַ����޷�����
		   if((ch >='A' && ch <= 'Z')|| (ch >='a' && ch <= 'z')){
			   return null;
		   }
		   //�ж�ch �Ƿ������ֻ�С����
		   if(ch == '.' || (ch >='A' && ch <= 'Z')|| (ch >='a' && ch <= 'z')|| (ch >='0' && ch <= '9')){  //�Ƿ�����ĸ�����ֻ�С����
//		   if(ch == '.' || (ch >='0' && ch <= '9')){
			    sb.append(ch);
			    continue;
		   }else if(ch == ' '){
			   continue;
		   }else{  //�������Ϊ���֡���ĸ�����ո���stringbuffer���е�ֵ����list
			   
			   //��Stringbuffer �е�����ת��Ϊ�ַ�������ŵ�list��
			   if(sb.length() > 0){
				   list.add(sb.toString());
				   sb.setLength(0);
			   }
			   
			   if(st.isEmpty() || ch == '('){
				   st.push(ch);
			   }else if(ch == ')'){
				   // ���ջ��'(' ���ϵ�ֵ
				  while(!st.isEmpty() && (char)st.peek() != '('){
					  String str = String.valueOf(st.pop());
					  list.add(str);
				  }
				  st.pop(); //������(��
				   
			   }else if(!st.isEmpty()  && !chkGreaterPriority(ch,(char)st.peek())){
				  
				   //��Ҫ����ǰ������ջ��Ԫ�صķ��Ž��бȽϣ��������ȼ��жϣ����ch�����ȼ���ջ��Ԫ�ص����ȼ��ͻ���ȣ��򵯳�ջ��Ԫ�أ�׷�ӵ�list�󣬷���ch��ջ��
				   while(!st.isEmpty() && !chkGreaterPriority(ch,(char)st.peek())){
					   //�����ǰ���ŵ����ȼ���ջ��Ԫ�ص����ȼ�Ҫ�ߣ���ջ��Ԫ�س�ջ����ӵ�list��
					   list.add(String.valueOf(st.pop()));
				   }
				   //����ǰ���ŷ���ջ��
				   st.push(ch);
			   }else{
				   st.push(ch);
			   }
			   
		   }
			   
			  
		
	   }
	   
	   //��ջ�е�Ԫ�ص�����׷�ӵ�list���γ�һ���沨�����ʽ
	   while(!st.isEmpty()){
		   list.add(String.valueOf(st.pop()));
	   }
	    
      return list;
   }
   
   private final static BigDecimal getCalcResult(final List<String> resList ,Integer precision){
	   Stack<Double> stack = new Stack<Double>();
	   for(String str : resList){
		/*   //�����ǰ���ַ���������ĸ�����ʾ��Ҫ��ѯ��ָ���ֵ
		   if(str.matches("[a-z|A-Z]+")){
			   //�����ݿ��в�ѯ��ָ���ֵ
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
    * ������ʽ��ֵ
    * @param strCalc ������ı��ʽ
    * @param scale ��ȷ��С��������λ
    * @return
    */
   public static final BigDecimal calcExpStr(final String strCalc , final Integer precision){
	   List<String> resList =  midfixToSurfix(strCalc);
	   return getCalcResult(resList ,precision);
   }
   
public  static void main(String[] args){

	   List<String> resList =  midfixToSurfix(strCalc);
	   BigDecimal res = getCalcResult(resList,3);
	  
	   
	   //������
	   System.out.println(res.toString());
   }
}
