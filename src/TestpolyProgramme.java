
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class TestpolyProgramme {
	static private Map<String,Integer> map;
	public static boolean expression(String str){
		String strp="^(((-?([a-zA-Z]+)\\s*(\\^\\s*([0-9]+))?)|(((-?\\d+(\\.\\d+)?)\\s*)"
				+ "(([a-zA-Z]+)(\\^\\s*([0-9]+))?)?\\s*))[\\+\\-\\*]\\s*)"
				+ "*((-?([a-zA-Z]+)"
				+ "\\s*(\\^\\s*([0-9]+))?)|(((-?\\d+(\\.\\d+)?)\\s*)(([a-zA-Z]+)"
				+ "(\\^\\s*([0-9]+))?)?\\s*))$";
		Pattern pat = Pattern.compile(strp);
		Matcher mat = pat.matcher(str);
		boolean rs = mat.find();
		if(rs){
			map=new HashMap<String,Integer>();
			String[] arr=str.split("[\\+\\-\\*\\^]");
			for(int i = 0;i < arr.length;i++){
				if(!map.containsKey(arr[i])){
					map.put(arr[i], 1);
				}
			}
			System.out.println(str);
			return true;
		}
		else{
			System.out.println("ERROR EXPRESSION!");
			return false;
		}
	}
	/*求值.*/
	public static void simplify(String str1,String str2){
		int count=0;
		String[] arr1=str2.split("[ ]+|=");
		str1=changemul(str1);
		for(int i=1;i<arr1.length;i=i+2){
			if(map.containsKey(arr1[i])){
				count++;
			}
			else{
				System.out.println("ERROR, no variable");
				break;
			}
		}
		if(count==arr1.length/2){
			int flag=0;
			String[] arr2=str2.split("[ ]+|=");
			for(int j=1;j<arr2.length;j=j+2){
				str1=str1.replaceAll(arr2[j],arr2[j+1]);
				if(arr2[j+1].charAt(0)=='-'){
					flag++;
				}		
			}
			if(flag==0){
				str1=simplifySingle(str1);
				str1=simplifyNum(str1);
			}System.out.println(str1);
		}
	}
	/*求导.*/
	public static void derivative(String str1,String str2){
		
		String[] strarray=str2.split("[ ]+");
		String[] str1Arr;
		str1=changemul(str1);
		
		String strms=getOp(str1);
		
		if(map.containsKey(strarray[1])){
			str1Arr=str1.split("\\+|\\-");
			for(int i=0;i<str1Arr.length;i++){
				String[] s=str1Arr[i].split("\\*");
				String s1="";
				int n1=0;
				int n2=0;
				int c=0;
				for(int j=0;j<s.length;j++){
					if(s[j].equals(strarray[1])){
						n1++;
						c++;
					}
					else{
						s1=s1+s[j]+"*";
						n2++;
						c++;
					}
				}
				if(c==1 & n1==1 & n2==0){
					s1="1";
				}
				else if(n2==0 & n1==c & c!=1){
					s1=n1+"*"+strarray[1]+"^"+(n1-1);
				}
				else if(n1==0){
					s1="0";
				}
				else{
					s1=s1+n1+"*"+strarray[1]+"^"+(n1-1);
				}
				str1Arr[i]=str1Arr[i].replace(str1Arr[i],s1);
			}
			String str="";
			for(int i=0;i<strms.length();i++){
				str+=str1Arr[i]+strms.substring(i,i+1);
			}
			str+=str1Arr[strms.length()];
			str=changemul(str);
			str=simplifySingle(str);
			str=simplifyNum(str);
			System.out.println(str);
		}
		else{
			System.out.println("ERROR, no variable");
		}
	}
	
	public static String changeadd(String str){
		String strp="(((-?\\d+(\\.\\d+)?)\\s*)(([a-zA-Z]+)((\\^\\s*([0-9]+))?))\\s*)";
		Pattern pat=Pattern.compile(strp);
		Matcher mat=pat.matcher(str);
		boolean rs=mat.find();
		if(rs){
			str=str.replaceAll(strp,"$2\\*$5");
		}
		return str;
	}
	
	public static String changemul(String str){
		String strp="[a-zA-Z]+(\\^\\s*([0-9]+))";
		Pattern pat=Pattern.compile(strp);
		Matcher mat=pat.matcher(str);
		while(mat.find()){
			String[] a=mat.group(0).split("\\^");
			if(a[1].equals("0"))
				str=str.replace(mat.group(0), "1");
			else{
				String str2="";
				String[] arr=mat.group(0).split("\\^");
				for(int i=0;i<Integer.parseInt(arr[1])-1;i++){
					str2+=(arr[0]+"*");
				}
				str=str.replace(mat.group(0), str2+arr[0]);
			}
		}
		return str;
	}
	
	public static boolean existMul( final String exp){
		if(exp.indexOf("*")==-1)
			return false;
		return true;
	}
	
	public static String getOp(String exp){
		String strms="";
		String strp1="(\\+|\\-)";
		Pattern pat1=Pattern.compile(strp1);
		Matcher mat1=pat1.matcher(exp);
		while(mat1.find()){
			strms+=mat1.group(0);
		}
		return strms;
	}
	
	public static String simplifySingle(String str){
		int border=3;
		String strms=getOp(str);
		String exp="";
		str=changemul(str);
		String[] arr=str.split("[\\+\\-]");
		for(int i=0;i<arr.length;i++){
			float sum=1;
			String strp2="\\d+(\\.\\d+)?";
			Pattern pat2=Pattern.compile(strp2);
			Matcher mat2=pat2.matcher(arr[i]);
			while(mat2.find()){
				float n=Float.parseFloat(mat2.group(0));
				sum*=n;
			}
			String str2="";
			String strp3="[a-zA-Z]+";
			if(sum!=0){
				Pattern pat3=Pattern.compile(strp3);
				Matcher mat3=pat3.matcher(arr[i]);
				while(mat3.find()){
					str2+=mat3.group(0)+"*";
				}
				str2+=sum+"";
			}
			else{
				str2+="0.0";
			}
			if(i<strms.length()){
				exp+=str2+strms.substring(i,i+1);
			}
			if(i==strms.length())
				exp+=str2;
		}
		if(str.charAt(0)=='-')
			return exp.substring(border);
		return exp;
	}
	
	public static String simplifyNum(String exp){
		String exptemp = new String(exp);
		float sum=0;
		if(exptemp.charAt(0)=='-')
			exptemp="0"+exptemp;
		else
			exptemp="0+"+exptemp;
		String[] arr=exptemp.split("[\\+\\-]");
		for(int i=0;i<arr.length;i++){
			if(!existMul(arr[i])){
				int p=exptemp.indexOf(arr[i]);
				String tmp="";
				if(p>0){
					if(exptemp.charAt(p-1)=='+'){
						sum+=Float.parseFloat(arr[i]);
						tmp=exptemp.substring(p, p+arr[i].length());
						tmp="\\+"+tmp;
						exptemp=exptemp.replaceFirst(tmp, "");
					}
					else if(exptemp.charAt(p-1)=='-'){
						sum-=Float.parseFloat(arr[i]);
						tmp=exptemp.substring(p, p+arr[i].length());
						tmp="\\-"+tmp;
						exptemp=exptemp.replaceFirst(tmp, "");
					}
				}
				else if(p==0){
					sum+=Float.parseFloat(arr[i]);
					exptemp=exptemp.replaceFirst(arr[i], "");
				}
			}
		}
		if(exptemp.equals(""))
			exptemp=sum+"";
		else{
			if(exptemp.charAt(0)=='-' || exptemp.charAt(0)=='+'){
				if(sum!=0){
					exptemp=sum+""+exptemp;
				}
				else{
					if(exptemp.charAt(0)=='+'){
						exptemp=exptemp.substring(1);
					}
				}
			}	
			else{
				if(sum!=0){
					exptemp=sum+""+"+"+exptemp;
				}
			}
		}
		return exptemp;
	}

	
	public static void main(String[] args){
		try{
			Scanner input=new Scanner(System.in);
			String str;
			boolean rs;
			long startTime = 0;
			long endTime = 0;
			do{
				System.out.println("请输入多项式：");
				str=input.nextLine();
				rs=expression(str);
			}while(rs==false);
			
			String str1=changeadd(str);	
			System.out.println(str1);
			String str2="";
			String con="";
			System.out.print("是否输入命令（y or n）？：");
			con=input.next();
			while(con.equals("y")||con.equals("Y")){
				input.nextLine();
				System.out.print("请输入命令：");
				str2=input.nextLine();
				
				String strp1="^!simplify\\s*(\\s+([a-zA-Z]+)=(-?\\d+(\\.\\d+)?)[ ]*)*$";
				Pattern pat1=Pattern.compile(strp1);
				Matcher mat1=pat1.matcher(str2);
				boolean rs1=mat1.find();

				String strp2="^!d/d\\s+[a-zA-Z]+$";
				Pattern pat2=Pattern.compile(strp2);
				Matcher mat2=pat2.matcher(str2);
				boolean rs2=mat2.find();
				startTime=System.currentTimeMillis();
				if(rs1){
					simplify(str1,str2);
				}
				else if(rs2){
					derivative(str1,str2);
				}
				else{
					System.out.println("Error Command!");
				}
				endTime=System.currentTimeMillis();
				System.out.println("执行开始时间： "+startTime+"ms");
				System.out.println("执行结束时间： "+endTime+"ms");
				System.out.println("执行总的时间： "+(endTime-startTime)+"ms");
				System.out.print("是否继续输入命令（y or n）？：");
				con=input.next();
			}
			input.close();
		}catch(Exception e){
			System.out.println("Error!");
		}
	}
}
