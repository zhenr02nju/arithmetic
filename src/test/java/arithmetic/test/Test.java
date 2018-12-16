/**
 * 
 */
package arithmetic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import rongzhen.arithmetic.ArithmeticFactory;
import rongzhen.arithmetic.ArithmeticFormule;
import rongzhen.arithmetic.FormuleCalculator;

/**
 * 2018-11-26 15:40:37
 * @author zhenr
 *
 */
public class Test {
	@org.junit.Test
	public void test() throws ParseException {
		/*
		Vector<Object> formule=new Vector<Object>();		
		formule.add(5);
		formule.add('-');
		formule.add(6);		
		formule.add('/');
		formule.add(2);		
		formule.add('-');
		formule.add(2);
		formule.add('x');
		formule.add(2);
		formule.add('-');
		formule.add(6);
		formule.add('+');
		formule.add(7);
		formule.add('/');
		formule.add(6);
		formule.add('x');
		formule.add(7);		
		ArithmeticFormule arithmeticFormule=new ArithmeticFormule(formule);
		FormuleCalculator formuleCalculator=new FormuleCalculator();		
		System.out.println(arithmeticFormule.print());
		arithmeticFormule.addParentheses(5);
		System.out.println(arithmeticFormule.print());
		System.out.println(arithmeticFormule.calculate());
		*/
		/*
		 		Vector<Object> operators=new Vector<Object>();
				Vector<Object> operator=new Vector<Object>();
				operators.add('-');
				operators.add('+');
		 */
		new ArithmeticFactory() {
			protected Vector<Object> buildNumbers(){
				Random random=new Random();
				Vector<Object> numbers=new Vector<Object>();
				for(int i=0;i<2;i++) {
					numbers.add(random.nextInt(20)+1);
				}
				return numbers;					
			}
			
			protected Vector<Object> buildOperator(){
				Random random=new Random();
				Character[] operators= {'+','-'};
				Vector<Object> operator=new Vector<Object>();
				operator.add(operators[random.nextInt(2)]);
				return operator;
			}
		}.buildFormule(10, 0);
;	}
}
