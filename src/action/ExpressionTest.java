package action;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
	private final Expression expression = Expression.instance();

	@Before
	public void setUp() throws Exception {
		expression.originalExpression.clear();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
public void testInput() throws Exception {

		System.out.println("期望输出："+"Error! String index out of range: 0");
		System.out.print("实际输出：");
		expression.input("");
		expression.printExpression(expression.originalExpression);	
	}










}
