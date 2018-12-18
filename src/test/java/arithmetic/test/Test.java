/**
 * 
 */
package arithmetic.test;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import rongzhen.arithmetic.ArithmeticFactory;
import rongzhen.arithmetic.ArithmeticFormule;
//import rongzhen.arithmetic.FormuleCalculator;

/**
 * 2018-11-26 15:40:37
 * @author zhenr
 *
 */
public class Test {
	@org.junit.Test
	public void test() throws ParseException {
		HashSet<ArithmeticFormule> list=new HashSet<ArithmeticFormule>();
		int count=72;//算式数量
		while(list.size()<count) {
			ArithmeticFormule formule=
			new ArithmeticFactory() {
				//数字
				protected Vector<Object> buildNumbers(){
					Random random=new Random();
					Vector<Object> numbers=new Vector<Object>();
					for(int i=0;i<2;i++) {
						numbers.add(random.nextInt(20)+1);
					}
					return numbers;					
				}
				//符号
				protected Vector<Object> buildOperator(){
					Random random=new Random();
					Character[] operators= {'+','-'};
					Vector<Object> operator=new Vector<Object>();
					operator.add(operators[random.nextInt(2)]);
					return operator;
				}
			}.buildFormule(0);
			//这里可以添加对算式的结果要求
			if(formule.calculate()>0) {
				list.add(formule);
			}
		}
		//打印
		ArithmeticFactory.print(list, 15);		
	}
}
