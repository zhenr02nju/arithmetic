/**
 * 
 */
package rongzhen.arithmetic;

import java.util.Collection;
import java.util.Vector;

/**
 * 2018-11-30 13:48:18
 * 算术公用方法类
 * @author zhenr
 *
 */
public class Arithmetic {
	/**
	 * 转为Double
	 * */
	protected Double toDouble(Object number) {
		if(number instanceof Integer) {
			return (Double)((Integer)number*1.0);
		}else if(number instanceof Long) {
			return (Double)((Long)number*1.0);
		}else if(number instanceof Double) {
			return (Double)number;
		}
		return 1.0;
	}
	
	/**
	 * 转换算式的数字为double
	 * */
	protected Vector<Object> standardizeFormuleNum(Collection <Object> formule) {
		Vector<Object> result=new Vector<Object>();
		for(Object ob : formule) {
			if(ob instanceof Integer||ob instanceof Long) {
				result.add(toDouble(ob));
			}else {
				result.add(ob);
			}
		}
		return result;
	}
	
	/**
	 * 是否为运算符
	 * */
	protected boolean isOperator(Object c) {
		if(c instanceof Character) {
			Character o=(Character)c;
			return o == '+' || o == '-' || o == '*' || o == '/' || o == 'x' || o == '÷';
		}
		else
			return false;
	}
	
	/**
	 * 判断一级运算符
	 * */
	protected boolean islevel1_Operator(Character c) {
		return c == '*' || c == '/';
	}
	
	/**
	 * 判断二级运算符
	 * */
	protected boolean islevel2_Operator(Character c) {
		return c == '+' || c == '-';
	}
	
	/**
	 * 判断左括号
	 * */
	protected boolean isLeftParenthes(Object c) {
		if(c instanceof Character) {
			Character o=(Character)c;
			return o == '(';
		}else
			return false;
		
	}
	
	/**
	 * 判断右括号
	 * */
	protected boolean isRightParenthes(Object c) {
		if(c instanceof Character) {
			Character o=(Character)c;
			return o == ')';
		}else
			return false;
	}
	
	/**
	 * 判断算式是否带括号
	 * */
	protected boolean isContainParentheses(Vector<Object> formule) {
		for(int i=0;i<formule.size();i++) {
			if(formule.get(i) instanceof Character) {
				if(isLeftParenthes((Character)formule.get(i))||isRightParenthes((Character)formule.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 加
	 * */
	protected double add(double a,double b) {
		return a+b;
	}
	
	/**
	 * 减
	 * */
	protected double sub(double a,double b) {
		return a-b;
	}
	
	/**
	 * 乘
	 * */
	protected double mul(double a,double b) {
		return a*b;
	}
	
	/**
	 * 除
	 * */
	protected double div(double a,double b) {
		return a/b;
	}
}
