/**
 * 
 */
package rongzhen.arithmetic;

import java.util.Iterator;
import java.util.Set;
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
	 * 生成一条算式
	 * @param parentheses 括号数
	 * */
	public ArithmeticFormule buildFormule(int parentheses) {
		ArithmeticFormule formule=new ArithmeticFormule(buildNumbers(),buildOperator());
		if(parentheses>0) {
			formule.addParentheses(parentheses);
		}
		return formule;
	}
	
	/**
	 * 打印算式组，排版
	 * @param width算式加空格的宽度
	 * */
	public static void print(Set<ArithmeticFormule> list,int width) {
		Iterator<ArithmeticFormule> iterator=list.iterator();
		int i=1;
		while(iterator.hasNext()) {
			ArithmeticFormule formule=iterator.next();			
			String temp=formule.show();
			//添加间距空格
			char[] temparry=temp.toCharArray();
			for(int j=0;j<width;j++) {
				if(j<temparry.length) {
					char self=temparry[j];			
					if(j==0) {//头直接打印
						System.out.print(self);
					}
					if(j>0) {
						char before=temparry[j-1];
						//运算符前空格，运算符后空格
						if(self == '+' || self == '-' || self == '*' || self == '/' || self == 'x' || self == '÷' || self == '=') {
							System.out.print(" ");
							System.out.print(self);
						}else if(before == '+' || before == '-' || before == '*' || before == '/' || before == 'x' || before == '÷' || before == '=') {
							System.out.print(" ");
							System.out.print(self);
						}else {
							System.out.print(self);
						}
					}
				}
				if(j>=temparry.length) {//算式打印后就打印空格
					System.out.print(" ");
				}
			}
			if(i%3==0) {//一行几个算式
				System.out.print('\n');
				System.out.print('\n');
				System.out.print('\n');
				System.out.print('\n');
			}
			i++;
		}
	}
}
