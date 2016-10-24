
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static boolean expression(String str){
		String str_p="^(((-?([a-zA-Z]+)\\s*(\\^\\s*([0-9]+))?)|(((-?\\d+(\\.\\d+)?)\\s*)(([a-zA-Z]+)(\\^\\s*([0-9]+))?)?\\s*))[\\+\\-\\*]\\s*)*((-?([a-zA-Z]+)\\s*(\\^\\s*([0-9]+))?)|(((-?\\d+(\\.\\d+)?)\\s*)(([a-zA-Z]+)(\\^\\s*([0-9]+))?)?\\s*))$";
		Pattern pat=Pattern.compile(str_p);
		Matcher mat=pat.matcher(str);
		boolean rs=mat.find();
		if(rs){
			System.out.println(str);
			return true;
		}
		else{
			System.out.println("ERROR EXPRESSION!");
			return false;
		}
	}
	
	public static void simplify(String str1,String str2){
		int count=0;
		String[] arr1=str2.split("[ ]+|=");
		str1=change_mul(str1);
		for(int i=1;i<arr1.length;i=i+2){
			if(existVar(str1,arr1[i])){
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
				if(arr2[j+1].charAt(0)=='-')
					flag++;
			}
			if(flag==0){
				str1=simplifySingle(str1);
				str1=simplifyNum(str1);
			}
			System.out.println(str1);
		}
	}
	
	public static void derivative(String str1,String str2){
		
		String[] str_array=str2.split("[ ]+");
		String[] str1_arr;
		str1=change_mul(str1);
		
		String str_ms=getOp(str1);
		
		if(existVar(str1,str_array[1])){
			str1_arr=str1.split("\\+|\\-");
			for(int i=0;i<str1_arr.length;i++){
				String[] s=str1_arr[i].split("\\*");
				String s1="";
				int n1=0;
				int n2=0;
				int c=0;
				for(int j=0;j<s.length;j++){
					if(s[j].equals(str_array[1])){
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
					s1=n1+"*"+str_array[1]+"^"+(n1-1);
				}
				else if(n1==0){
					s1="0";
				}
				else{
					s1=s1+n1+"*"+str_array[1]+"^"+(n1-1);
				}
				str1_arr[i]=str1_arr[i].replace(str1_arr[i],s1);
			}
			String str="";
			for(int i=0;i<str_ms.length();i++){
				str+=str1_arr[i]+str_ms.substring(i,i+1);
			}
			str+=str1_arr[str_ms.length()];
			str=change_mul(str);
			str=simplifySingle(str);
			str=simplifyNum(str);
			System.out.println(str);
		}
		else{
			System.out.println("ERROR, no variable");
		}
	}
	
	public static String change_add(String str){
		String str_p="(((-?\\d+(\\.\\d+)?)\\s*)(([a-zA-Z]+)((\\^\\s*([0-9]+))?))\\s*)";
		Pattern pat=Pattern.compile(str_p);
		Matcher mat=pat.matcher(str);
		boolean rs=mat.find();
		if(rs){
			str=str.replaceAll(str_p,"$2\\*$5");
		}
		return str;
	}
	
	public static String change_mul(String str){
		String str_p="[a-zA-Z]+(\\^\\s*([0-9]+))";
		Pattern pat=Pattern.compile(str_p);
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
	
	public static boolean existVar(String exp,String var){
		String[] arr=exp.split("[\\+\\-\\*\\^]");
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals(var))
				return true;
		}
		return false;
	}
	
	public static boolean existMul(String exp){
		if(exp.indexOf("*")==-1)
			return false;
		return true;
	}
	
	public static String getOp(String exp){
		String str_ms="";
		String str_p1="(\\+|\\-)";
		Pattern pat1=Pattern.compile(str_p1);
		Matcher mat1=pat1.matcher(exp);
		while(mat1.find()){
			str_ms+=mat1.group(0);
		}
		return str_ms;
	}
	
	public static String simplifySingle(String str){
		String str_ms=getOp(str);
		String exp="";
		str=change_mul(str);
		String[] arr=str.split("[\\+\\-]");
		for(int i=0;i<arr.length;i++){
			float sum=1;
			String str_p2="\\d+(\\.\\d+)?";
			Pattern pat2=Pattern.compile(str_p2);
			Matcher mat2=pat2.matcher(arr[i]);
			while(mat2.find()){
				float n=Float.parseFloat(mat2.group(0));
				sum*=n;
			}
			String str2="";
			String str_p3="[a-zA-Z]+";
			if(sum!=0){
				Pattern pat3=Pattern.compile(str_p3);
				Matcher mat3=pat3.matcher(arr[i]);
				while(mat3.find()){
					str2+=mat3.group(0)+"*";
				}
				str2+=sum+"";
			}
			else{
				str2+="0.0";
			}
			if(i<str_ms.length()){
				exp+=str2+str_ms.substring(i,i+1);
			}
			if(i==str_ms.length())
				exp+=str2;
		}
		if(str.charAt(0)=='-')
			return exp.substring(3);
		return exp;
	}
	
	public static String simplifyNum(String exp){
		float sum=0;
		if(exp.charAt(0)=='-')
			exp="0"+exp;
		else
			exp="0+"+exp;
		String[] arr=exp.split("[\\+\\-]");
		for(int i=0;i<arr.length;i++){
			if(!existMul(arr[i])){
				int p=exp.indexOf(arr[i]);
				String tmp="";
				if(p>0){
					if(exp.charAt(p-1)=='+'){
						sum+=Float.parseFloat(arr[i]);
						tmp=exp.substring(p, p+arr[i].length());
						tmp="\\+"+tmp;
						exp=exp.replaceFirst(tmp, "");
					}
					else if(exp.charAt(p-1)=='-'){
						sum-=Float.parseFloat(arr[i]);
						tmp=exp.substring(p, p+arr[i].length());
						tmp="\\-"+tmp;
						exp=exp.replaceFirst(tmp, "");
					}
				}
				else if(p==0){
					sum+=Float.parseFloat(arr[i]);
					exp=exp.replaceFirst(arr[i], "");
				}
			}
		}
		if(exp.equals(""))
			exp=sum+"";
		else{
			if(exp.charAt(0)=='-' | exp.charAt(0)=='+'){
				if(sum!=0){
					exp=sum+""+exp;
				}
				else{
					if(exp.charAt(0)=='+'){
						exp=exp.substring(1);
					}
				}
			}	
			else{
				if(sum!=0){
					exp=sum+""+"+"+exp;
				}
			}
		}
		return exp;
	}

	
	public static void main(String[] args){
		Scanner input=new Scanner(System.in);
		String str;
		boolean rs;
		long startTime=0,endTime=0;
		do{
			System.out.println("请输入多项式：");
			str=input.nextLine();
			rs=expression(str);
		}while(rs==false);
		
		String str1=change_add(str);	
		
		String str2="";
		String Con="";
		System.out.print("是否输入命令（y or n）？：");
		Con=input.next();
		while(Con.equals("y")){
			input.nextLine();
			System.out.print("请输入命令：");
			str2=input.nextLine();
			
			String str_p1="^!simplify\\s*(\\s+([a-zA-Z]+)=(-?\\d+(\\.\\d+)?)[ ]*)*$";
			Pattern pat1=Pattern.compile(str_p1);
			Matcher mat1=pat1.matcher(str2);
			boolean rs1=mat1.find();

			String str_p2="^!d/d\\s+[a-zA-Z]+$";
			Pattern pat2=Pattern.compile(str_p2);
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
			Con=input.next();
		}
		input.close();
	}
}
