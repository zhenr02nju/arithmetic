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
		HashSet<String> non_coincidence=new HashSet<String>();
		int count=10000;//算式数量
		while(count>0) {
			ArithmeticFormule formule=
			new ArithmeticFactory() {
				//数字
				protected Vector<Object> buildNumbers(){
					Random random=new Random();
					Vector<Object> numbers=new Vector<Object>();
					
					for(int i=0;i<2;i++) {
						numbers.add(random.nextInt(100)+1);
					}
					
					return numbers;
				}
				//符号
				protected Vector<Object> buildOperator(){
					Random random=new Random();
					Character[] operators= {'+','-'};
					Vector<Object> operator=new Vector<Object>();
					operator.add(operators[random.nextInt(operators.length)]);
					return operator;
				}
			}.buildFormule(0);
			//这里可以添加对算式的结果要求
			if(formule.calculate()>0&&formule.calculate()<=100) {
				if(!non_coincidence.contains(formule.show())) {
					list.add(formule);
					non_coincidence.add(formule.show());
				}
			}
			count--;
		}
		//打印
		ArithmeticFactory.print(list, 10, 5,false);
	}
}
