/**
 * 
 */
package rongzhen.arithmetic;

import java.util.Vector;

/**
 * 2018-11-27 12:06:14
 * 四则混合运算计算类，可计算带括号的
 * 对外接口 calculateFormula（Vector）
 * @author zhenr
 *
 */
public class FormuleCalculator extends Arithmetic{
	
	/**
	 * 括号合法化判断
	 * 1.左括号与右括号是否配对
	 * 2.左括号后面是否紧跟数字
	 * 3.右括号是否紧跟数字后面
	 * 4.如果先出现右括号直接返回false
	 * 5.如果左括号出现在最后一位，返回false
	 * */
	private boolean isParenthesesCorrect(Vector<Object> formule) {
		if(!isContainParentheses(formule)) {
			return true;
		}
		int count=0;
		for(int i=0;i<formule.size();i++) {
			Object parenthes=formule.get(i);
			if(parenthes instanceof Character) {
				if((Character)parenthes=='(') {
					count++;
					if(i+1>=formule.size()) {//如果左括号出现在最后一位，返回false
						return false;
					}
					Object number=formule.get(i+1);//左括号后面是否紧跟数字
					if(!(number instanceof Double||number instanceof Integer||number instanceof Long)) {
						return false;
					}
				}else if((Character)parenthes==')') {
					count--;
					if(count<0) {//如果先出现右括号直接返回false
						return false;
					}
					Object number=formule.get(i-1);//右括号是否紧跟数字后面
					if(!(number instanceof Double||number instanceof Integer||number instanceof Long)) {
						return false;
					}
				}
			}
		}
		if(count==0) {//左括号与右括号是否配对
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 算式合法化判断,默认括号已合法
	 * 1.运算符在头尾的，false
	 * 2.运算符连续出现的，false
	 * 3.数字连续出现，false
	 * 4.除数为0，false
	 * */
	public boolean isFormuleCorrect(Vector<Object> formule) {
		if(isContainParentheses(formule)) {
			if(!isParenthesesCorrect(formule)) {
				return false;
			}
		}
		Object head=formule.firstElement();//运算符在头的，false
		if(head instanceof Character && isOperator((Character)head)) {
			return false;
		}
		Object tail=formule.lastElement();//运算符在尾的，false
		if(tail instanceof Character && isOperator((Character)tail)) {
			return false;
		}
		for(int i=0;i<formule.size();i++) {
			Object ob=formule.get(i);
			if(ob instanceof Double||ob instanceof Integer||ob instanceof Long) {//数字连续出现
				if(i<formule.size()-1) {
					Object next=formule.get(i+1);
					if(next instanceof Double||next instanceof Integer||next instanceof Long) {
						return false;
					}
				}
			}
			if(ob instanceof Character && isOperator((Character)ob)) {//运算符连续出现的
				Object next=formule.get(i+1);
				if(next instanceof Character && isOperator((Character)next)) {
					return false;
				}
			}
		}
		if(Double.isInfinite(calculateFormula(standardizeFormuleNum(formule)))) {
			return false;
		}
		if(Double.isNaN(calculateFormula(standardizeFormuleNum(formule)))) {
			return false;
		}
		return true;
	}
		
	/**
	 * 计算算式 不带括号
	 * */
	private double calculateNoParentheses(Vector<Object> formule) {
		Vector<Object> level2formula=new Vector<Object>();//乘法除法计算后的算式
		double result=0;
		//计算乘法/除法
		for(int i=0;i<formule.size();i++) {
			if(formule.get(i) instanceof Character&&i>0) {//判断是否为运算符
				if(islevel1_Operator((Character)formule.get(i))) {//判断是否为1级运算符
					if((Character)formule.get(i)=='*') {//乘法
						double temp=mul((Double)level2formula.lastElement(),(Double)formule.get(++i));
						level2formula.set(level2formula.size()-1, temp);
					}else {//除法
						double temp=div((Double)level2formula.lastElement(),(Double)formule.get(++i));
						level2formula.set(level2formula.size()-1, temp);
					}
				}else {//不是1级运算符就放入下一步算式
					level2formula.add(formule.get(i));
				}
			}else {//不是运算符就放入下一步算式
				level2formula.add(formule.get(i));
			}
		}
		//计算加减法
		for(int j=0;j<level2formula.size();j++) {
			if(level2formula.get(j) instanceof Character&&j>0) {//判断是否为运算符
				if((Character)level2formula.get(j)=='+') {//加法
					double temp=add(result,(Double)level2formula.get(++j));
					result=temp;
				}else {//减法
					double temp=sub(result,(Double)level2formula.get(++j));
					result=temp;
				}
			}else 
				result=(Double) level2formula.get(j);
		}
		return result;
	}
	
	/**
	 * 计算算式 带括号
	 * */
	public double calculateFormula(Vector<Object> formule) {
		Vector<Object> doubleFormula=standardizeFormuleNum(formule);//算式数字进行转换
		Vector<Object> level2formula=new Vector<Object>();//括号计算后的算式		
		if(isContainParentheses(doubleFormula)) {
			Vector<Integer> LeftParenthesIndex=new Vector<Integer>();//l2算式中左括号的索引号
			for(int i=0;i<doubleFormula.size();i++) {
				//System.out.println(i);
				level2formula.add(doubleFormula.get(i));//把原算式内容加入下一步l2算式
				if(doubleFormula.get(i) instanceof Character) {
					if(isLeftParenthes((Character)doubleFormula.get(i))) {//如果是左括号，就记录左括号在l2算式中的index，用于删除括号中的内容，替换为计算结果
						LeftParenthesIndex.add(level2formula.size()-1);
					}
					if(isRightParenthes((Character)doubleFormula.get(i))) {//如果遇到右括号，就反向查询最后一个左括号，用于配对
						int start=LeftParenthesIndex.lastElement()+1;//最后一个左括号索引位的后一位，从这一位开始，到l2算式的最后一位，就是括号内的算式，先进行计算
						int end=level2formula.size()-1;//l2算式的最后一位
						LeftParenthesIndex.remove(LeftParenthesIndex.lastElement());//删除最后一个左括号的索引号
						Vector<Object> subFormula=new Vector<Object>();
						subFormula.addAll(level2formula.subList(start, end));//取出括号内的算式，用于计算
						//把括号内算式和左括号删除，然后替代为计算结果
						for(int j=end;j>=start-1;j--) {
							level2formula.remove(j);
						}
						level2formula.add(calculateNoParentheses(subFormula));//替代为计算结果
					}
				}				
			}
		}else {
			level2formula.addAll(doubleFormula);//如果算式无括号，就直接复制给l2算式
		}
		return calculateNoParentheses(level2formula);//计算l算式
	}
}
