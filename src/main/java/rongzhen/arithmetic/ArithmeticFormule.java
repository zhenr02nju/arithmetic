/**
 * 
 */
package rongzhen.arithmetic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 2018-11-28 16:23:32
 * 算术算式
 * @author zhenr
 *
 */
public class ArithmeticFormule extends Arithmetic{
	
	private Vector<Object> formule=new Vector<Object>();
	private FormuleCalculator calculator=new FormuleCalculator();;
	
	public ArithmeticFormule(Collection <Object> formule) {
		setFormule(formule);
	}
	
	public ArithmeticFormule(Collection <Object> numbers,Collection <Object> operators) {
		setFormule(generateFormulaNoParentheses(numbers,operators));
	}
	
	public Vector<Object> getFormule(){
		return formule;
	}
	
	public void setFormule(Collection <Object> formule) {
		this.formule.clear();
		this.formule.addAll(standardizeFormuleNum(formule));
	}
	
	/**
	 * 根据数字和符号组生成标准算式向量,不含括号，数字全部为double
	 * */
	public Vector<Object> generateFormulaNoParentheses(Collection <Object> numbers,Collection <Object> operators) {
		Vector<Object> formule=new Vector<Object>();
		Iterator<Object> itNum=numbers.iterator();
		Iterator<Object> itOpt=operators.iterator();
		while(itNum.hasNext()) {
			Object number=itNum.next();
			if(number instanceof Double) {
				formule.add(number);
			}else if(number instanceof Integer) {
				formule.add(toDouble(number));
			}else if(number instanceof Long) {
				formule.add(toDouble(number));
			}
			if(itOpt.hasNext()) {
				Object operator=itOpt.next();
				if(operator instanceof Character&&isOperator((Character)operator)) {
					if((Character)operator=='x') {
						formule.add('*');
					}else if((Character)operator=='÷') {
						formule.add('/');
					}else {
						formule.add(operator);
					}
				}				
			}
		}
		return formule;
	}
	
	/**
	 * 当前位置能否向后插入括号,默认算式无括号
	 * 1.-（+|-）
	 * 2.x（+|-）
	 * 3.÷（+|-|x|÷）
	 * 4.左括号后至少有三位
	 * 5.左括号后面是否紧跟数字
	 * @param formule 无括号算式  index 奇数
	 * */
	private boolean isParenthesBack(Vector<Object> formule,int index) {
		int count=formule.size();
		if(count>3) {
			Object object=formule.get(index);
			List<Object> sublist=formule.subList(index+1, count);
			if(sublist.size()>2&&isOperator(object)) {//该位置是运算符，而且之后有合法算式>=3位
				Character operator=(Character)object;
				if(operator=='-') {
					if(sublist.contains('+')||sublist.contains('-')) {
						return true;
					}
				}else if(operator=='x'||operator=='*') {
					if(sublist.contains('+')||sublist.contains('-')) {
						return true;
					}
				}else if(operator=='÷'||operator=='/') {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 随机向前插入一个括号，括号需有意义,默认算式无括号
	 * 1.（+|-）x
	 * 2.（+|-|÷）÷
	 * 3.右括号前至少有三位
	 * 4.右括号是否紧跟数字后面
	 * @param formule 无括号算式  index 奇数
	 * */
	private boolean isParenthesFront(Vector<Object> formule,int index) {
		int count=formule.size();
		if(count>3) {
			Object object=formule.get(index);
			List<Object> sublist=formule.subList(0, index);
			if(sublist.size()>2&&isOperator(object)) {//该位置是运算符，而且之前有合法算式>=3位
				Character operator=(Character)object;
				if(operator=='x'||operator=='*') {
					if(sublist.contains('+')||sublist.contains('-')) {
						return true;
					}
				}else if(operator=='÷'||operator=='/') {
					if(sublist.contains('+')||sublist.contains('-')||sublist.contains('÷')||sublist.contains('/')) {
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	/**
	 * 判断一个算式，能否插入有意义的括号,默认算式无括号
	 * 1.-（+|-）
	 * 2.x（+|-）
	 * 3.÷（+|-|x|÷）
	 * 4.（+|-）x
	 * 5.（+|-|÷）÷
	 * 6.左括号后至少有三位
	 * 7.右括号前至少有三位
	 * 8.左括号后面是否紧跟数字
	 * 9.右括号是否紧跟数字后面
	 * */
	private boolean isParenthesNoP(Vector<Object> formule) {
		int count=formule.size();
		if(count>3) {
			for(int i=0;i<count;i++) {
				if(isParenthesBack(formule,i)||isParenthesFront(formule,i)) {//对每位运算符，进行前后能否加括号的判断
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断一个算式能否打括号，无论是否有括号
	 * */
	public boolean isParentheses() {
		return isParentheses(formule);
	}
	
	/**
	 * 判断一个算式能否打括号，无论是否有括号
	 * */
	@SuppressWarnings("unchecked")
	public boolean isParentheses(Vector<Object> formule) {
		Vector<Object> formuleNoP=replaceParentheses(formule);
		if(isParenthesNoP(formuleNoP)) {
			return true;
		}else {
			int count=formuleNoP.size();
			for(int i=0;i<count;i++) {
				Object object=formuleNoP.get(i);
				if(object instanceof Vector) {
					if(isParentheses((Vector<Object>) object)) {
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	/**
	 * 括号连同括号内算式替换，全部替换为Vector
	 * */
	private Vector<Object> replaceParentheses(Vector<Object> formule) {
		int count=formule.size();
		Vector<Object> level2formula=new Vector<Object>();//括号计算后的算式
		Vector<Integer> LeftParenthesIndex=new Vector<Integer>();//l2算式中左括号的索引号
		for(int i=0;i<count;i++) {
			//System.out.println(i);
			level2formula.add(formule.get(i));//把原算式内容加入下一步l2算式
			if(formule.get(i) instanceof Character) {
				if(isLeftParenthes((Character)formule.get(i))) {//如果是左括号，就记录左括号在l2算式中的index，用于删除括号中的内容，替换为计算结果
					LeftParenthesIndex.add(level2formula.size()-1);
				}
				if(isRightParenthes((Character)formule.get(i))) {//如果遇到右括号，就反向查询最后一个左括号，用于配对
					int start=LeftParenthesIndex.lastElement()+1;//最后一个左括号索引位的后一位，从这一位开始，到l2算式的最后一位，就是括号内的算式，先进行计算
					int end=level2formula.size()-1;//l2算式的最后一位
					LeftParenthesIndex.remove(LeftParenthesIndex.lastElement());//删除最后一个左括号的索引号
					Vector<Object> subFormula=new Vector<Object>();
					subFormula.addAll(level2formula.subList(start, end));//取出括号内的算式
					//把括号内算式和左括号删除，然后替代为计算结果
					for(int j=end;j>=start-1;j--) {
						level2formula.remove(j);
					}
					level2formula.add(subFormula);//替代为子算式
				}
			}				
		}
		return level2formula;
	}
	
	/**
	 * 还原所有括号，返回含括号的算式
	 * */
	@SuppressWarnings("unchecked")
	private Vector<Object> restoreParentheses(Vector<Object> formule) {
		int count=formule.size();
		Vector<Object> level2formula=new Vector<Object>();//括号添加后的算式
		for(int i=0;i<count;i++) {
			Object object=formule.get(i);
			if(object instanceof Vector) {
				level2formula.add('(');
				level2formula.addAll(restoreParentheses((Vector<Object>) object));//递归子算式
				level2formula.add(')');
			}else {
				level2formula.add(object);
			}
		}
		return level2formula;
	}
		
	/**
	 * 随机向后插入一个括号，括号需有意义,默认算式无括号,且可以向后加括号
	 * 1.-（+|-）
	 * 2.x（+|-）
	 * 3.÷（+|-|x|÷）
	 * @param formule 无括号算式  index 奇数
	 * @return 返回一个加好括号的算式
	 * */
	private Vector<Object> addParenthesBack(Vector<Object> formule,int index) {
		int count=formule.size();
		Character operator=(Character)formule.get(index);
		List<Object> sublist=formule.subList(index+1, count);
		Vector<Integer> opIndexList=new Vector<Integer>();
		int subCount=sublist.size();
		for(int i=0;i<subCount;i++) {
			Object subObject=sublist.get(i);
			if(isOperator(subObject)) {
				if(operator=='-') {
					if((Character)subObject=='+'||(Character)subObject=='-') {
						opIndexList.add(index+i+1);
					}
				}else if(operator=='x'||operator=='*') {
					if((Character)subObject=='+'||(Character)subObject=='-') {
						opIndexList.add(index+i+1);
					}
				}else if(operator=='÷'||operator=='/') {
					opIndexList.add(index+i+1);
				}
			}
		}
		Random random =new Random();
		int ra_i=random.nextInt(opIndexList.size());
		int opIndex=opIndexList.get(ra_i);
		Vector<Object> inParentheses=new Vector<Object>();
		inParentheses.addAll(formule.subList(index+1, opIndex+2));
		Vector<Object> result=new Vector<Object>();
		for(int j=0;j<count;j++) {
			if(j>index&&j<opIndex+1) {
				//do nonthing
			}else if(j==opIndex+1) {
				result.add(inParentheses);
			}else {
				result.add(formule.get(j));
			}
		}
		return result;
	}
	
	/**
	 * 随机向前插入一个括号，括号需有意义,默认算式无括号,且可以向前加括号
	 * 1.（+|-）x
	 * 2.（+|-|÷）÷
	 * */
	private Vector<Object> addParenthesFront(Vector<Object> formule,int index) {		
		int count=formule.size();
		Character operator=(Character)formule.get(index);
		List<Object> sublist=formule.subList(0, index);
		Vector<Integer> opIndexList=new Vector<Integer>();
		int subCount=sublist.size();
		for(int i=0;i<subCount;i++) {
			Object subObject=sublist.get(i);
			if(isOperator(subObject)) {
				if(operator=='x'||operator=='*') {
					if((Character)subObject=='+'||(Character)subObject=='-') {
						opIndexList.add(i);
					}
				}else if(operator=='÷'||operator=='/') {
					if((Character)subObject=='+'||(Character)subObject=='-'||(Character)subObject=='÷'||(Character)subObject=='/') {
						opIndexList.add(i);
					}
				}
			}
		}
		Random random =new Random();
		int ra_i=random.nextInt(opIndexList.size());
		int opIndex=opIndexList.get(ra_i);
		Vector<Object> inParentheses=new Vector<Object>();
		inParentheses.addAll(formule.subList(opIndex-1, index));
		Vector<Object> result=new Vector<Object>();
		for(int j=0;j<count;j++) {
			if(j>=opIndex-1&&j<index-1) {
				//do nonthing
			}else if(j==index-1) {
				result.add(inParentheses);
			}else {
				result.add(formule.get(j));
			}
		}
		return result;
	}
	
	/**
	 * 随机插入1个括号，默认算式无括号，默认可打括号
	 * */
	private Vector<Object> addParenthesNoP(Vector<Object> formule) {
		Vector<Object> result=new Vector<Object>();
		int count=formule.size();
		Random random =new Random();
		boolean done=false;
		Vector<Integer> opIndexList=new Vector<Integer>();
		for(int i=0;i<count;i++) {
			if(isOperator(formule.get(i))) {
				opIndexList.add(i);
			}
		}
		int opCount=opIndexList.size();
		
		do {
			int randomIndex = opIndexList.get(random.nextInt(opCount));
			int direction=0;//为0就向后，为1就向前
			if(random.nextInt(10)%2==1) {
				direction=1;
			}
			if(direction==0) {
				if(isParenthesBack(formule,randomIndex)) {
					result.addAll(addParenthesBack(formule,randomIndex));
					done=true;
				}else if(isParenthesFront(formule,randomIndex)) {
					result.addAll(addParenthesFront(formule,randomIndex));
					done=true;
				}
			}else {
				if(isParenthesFront(formule,randomIndex)) {
					result.addAll(addParenthesFront(formule,randomIndex));
					done=true;
				}else if(isParenthesBack(formule,randomIndex)) {
					result.addAll(addParenthesBack(formule,randomIndex));
					done=true;
				}
			}
		}while(!done);
		return result;
	}
	
	/**
	 * 随机打1个括号，无论有没有括号，返回一个打完括号的完整算式
	 * */
	private Vector<Object> addParenthes(Vector<Object> formule){
		if(!isParentheses(formule)) {
			return restoreParentheses(formule);
		}else {
			if(!isContainParentheses(formule)) {
				return restoreParentheses(addParenthesNoP(formule));
			}else {
				int count=formule.size();
				Vector<Integer> lParenthesIndex=new Vector<Integer>();
				Vector<int[]> parentheses=new Vector<int[]>();
				for(int i=0;i<count;i++) {
					Object object=formule.get(i);
					if(isLeftParenthes(object)) {
						lParenthesIndex.add(i);
					}
					if(isRightParenthes(object)) {
						int[] temp={lParenthesIndex.lastElement(),i};
						parentheses.add(temp);
						int last=lParenthesIndex.size();
						lParenthesIndex.remove(last-1);
					}
				}
				Random random=new Random();
				int parenthesSize=parentheses.size();
				while(true) {
					int index=random.nextInt(parenthesSize+1);
					if(index<parenthesSize) {
						int[] parenthes=parentheses.get(index);
						Vector<Object> subTemp=new Vector<Object>();
						subTemp.addAll(formule.subList(parenthes[0]+1, parenthes[1]));
						Vector<Object> sub= replaceParentheses(subTemp);
						if(isParenthesNoP(sub)) {
							Vector<Object> result=new Vector<Object>();
							result.addAll(formule.subList(0,parenthes[0]+1));
							result.addAll(addParenthesNoP(sub));
							result.addAll(formule.subList(parenthes[1],count));
							Vector<Object> check=restoreParentheses(result);
							if(Double.isFinite(calculator.calculateFormula(check))) {
								return check;
							}
							
						}
					}else {
						Vector<Object> sub= replaceParentheses(formule);
						if(isParenthesNoP(sub)) {
							Vector<Object> check=restoreParentheses(addParenthesNoP(sub));
							if(Double.isFinite(calculator.calculateFormula(check))) {
								return check;
							}
						}
					}
				}				
			}
		}
	}
	
	/**
	 * 插入多个括号，默认算式可加括号self
	 * */
	public void addParentheses(int no) {
		setFormule(addParentheses(formule,no));
	}
			
	/**
	 * 插入多个括号，默认算式可加括号
	 * */
	public Vector<Object> addParentheses(Vector<Object> formule,int no) {
		Vector<Object> result=new Vector<Object>();
		result.addAll(formule);
		for(int i=0;i<no;i++) {			
			Vector<Object> temp=new Vector<Object>();
			temp.addAll(result);
			if(isParentheses(result)) {
				result.clear();
				result.addAll(addParenthes(temp));
			}else {
				return result;
			}
		}
		return result;
	}
	
	
	
	/**
	 * Double类型的显示,如果有小数部分， 就显示小数部分，没有就只显示整数部分
	 * */
	private String printDouble(Double d) {
		double decimal=d-Math.floor(d);
		if(decimal>0){
			/*//四舍五入保留2位
			DecimalFormat df = new DecimalFormat("#.00");	        
	        return String.valueOf(df.format(d));
	        */
			return String.valueOf(d);//显示完整小数
		}else {
			return String.format("%.0f", d);
		}
	}
	
	
	
	/**
	 * 判断算式是否合法 含括号判断
	 * */
	public boolean isFormuleCorrect() {
		if(calculator.isFormuleCorrect(formule)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 打印算式-打印参数里的算式
	 * */
	public String print(Collection <Object> formule) {
		StringBuffer sf=new StringBuffer();
		for(Object ob : formule) {
			if(ob instanceof Double) {
				sf.append(printDouble((Double) ob));
			}else if(ob instanceof Character) {
				if((Character)ob=='*') {
					sf.append('x');
				}else if((Character)ob=='/') {
					sf.append('÷');
				}else {
					sf.append(ob);
				}
			}else {
				sf.append(ob);
			}
		}
		sf.append("=");
		return sf.toString();
	}
	
	/**
	 * 打印算式-self
	 * */
	public String print() {
		return print(formule);
	}
	
	/**
	 * 计算算式
	 * */
	public double calculate() {
		return calculator.calculateFormula(formule);
	}
}
