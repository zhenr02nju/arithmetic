/**
 * 
 */
package rongzhen.arithmetic;

import java.util.HashSet;
import java.util.Vector;

/**
 * 2018-12-16 18:28:09
 * @author zhenr
 * 算式工厂，用于准备数据与生成算式
 */
public abstract  class ArithmeticFactory {
	/**
	 * 设置算式数字
	 * */
	protected abstract Vector<Object> buildNumbers();
	
	/**
	 * 设置算式运算符
	 * */
	protected abstract Vector<Object> buildOperator();
	
	/**
	 * 生成多个算式
	 * @param no 生成多少个算式
	 * @param parentheses 括号数
	 * */
	public HashSet<String> buildFormule(int no,int parentheses) {
		HashSet<String> list=new HashSet<String>();
		while(list.size()<no) {
			ArithmeticFormule formule=new ArithmeticFormule(buildNumbers(),buildOperator());
			if(parentheses>0) {
				formule.addParentheses(parentheses);
			}
			if(formule.calculate()>0) {
				System.out.println(formule.print());
				list.add(formule.print());
			}
		}
		return list;
	}
}
