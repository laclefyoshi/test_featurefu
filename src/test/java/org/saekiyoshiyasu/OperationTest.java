/**
 * File
 */

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Map;
import java.util.HashMap;

import com.linkedin.featurefu.expr.VariableRegistry;
import com.linkedin.featurefu.expr.Expr;
import com.linkedin.featurefu.expr.Expression;
import com.linkedin.featurefu.expr.Variable;

/**
 * @author laclefyoshi
 */
public class OperationTest {

    /**
     * FeatureFuのオペレータリスト.
     */
    @Test
    public final void operators() {
        // assertEquals(expected, actual, delta)
        assertEquals(evaluator("(== a b)", 1.2, 1.3), 0.0, 0); // false == 0.0
        assertEquals(evaluator("(== a b)", 2.5, 2.5), 1.0, 0); // true  == 1.0
        assertEquals(evaluator("(!= a b)", 1.2, 1.3), 1.0, 0);
        assertEquals(evaluator("(> a b)", 1.3, 1.3), 0.0, 0);
        assertEquals(evaluator("(< a b)", 2.3, 2.5), 1.0, 0);
        assertEquals(evaluator("(>= a b)", 1.3, 1.3), 1.0, 0);
        assertEquals(evaluator("(<= a b)", 2.3, 2.5), 1.0, 0);
        assertEquals(evaluator("(max a b)", 1.2, 1.3), 1.3, 0);
        assertEquals(evaluator("(min a b)", 1.2, 1.3), 1.2, 0);
        assertEquals(evaluator("(|| a b)", 1.0, 0.0), 1.0, 0);
        assertEquals(evaluator("(&& a b)", 1.0, 0.0), 0.0, 0);
        assertEquals(evaluator("(! a)", 1.0, 0.0), 0.0, 0);
        assertEquals(evaluator("(! a)", 0.0, 0.0), 1.0, 0);
        assertEquals(evaluator("(sign a)", -1.3, 0.0), -1.0, 0);
        assertEquals(evaluator("(sign a)", 2.4, 0.0), 1.0, 0);
        assertEquals(evaluator("(abs a)", -2.4, 0.0), 2.4, 0);
        assertEquals(evaluator("(- a)", 2.4, 0.0), -2.4, 0);
        assertEquals(evaluator("(unaryMinus a)", 2.4, 0.0), -2.4, 0);
        assertEquals(evaluator("(/ (- a (* 2 (+ a b))) 2)", 2.4, 3.0), -4.2, 0);
        // == ((a - (2.0 * (a + b))) / 2.0)
        // ==
        //  └── /
        //      ├── -
        //      |   ├── a
        //      |   └── *
        //      |       ├── 2.0
        //      |       └── +
        //      |           ├── a
        //      |           └── b
        //      └── 2.0
        assertEquals(evaluator("(** a b)", 4.0, 3.0), 64.0, 0);
        assertEquals(evaluator("(% a b)", 7.1, 3.0), 1.1, 0.01);
        assertEquals(evaluator("(ln a)", 4.2, 0.0), 1.435, 0.0001);
        assertEquals(evaluator("(ln1plus a)", 4.2, 0.0), 1.6486, 0.0001);
        assertEquals(evaluator("(log2 a)", 4.2, 0.0), 2.0703, 0.0001);
        assertEquals(evaluator("(exp a)", 1.0, 0.0), 2.71828, 0.00001);
        assertEquals(evaluator("(sigmoid a)", 0.6, 0.0), 0.64565, 0.00001);
        assertEquals(evaluator("(sin a)", 1.57, 0.0), 1.0, 0.0001);
        assertEquals(evaluator("(cos a)", 1.57, 0.0), 0.0, 0.001);
        assertEquals(evaluator("(tan a)", 1.57, 0.0), 1255.76, 0.01);
        assertEquals(evaluator("(tanh a)", 1.57, 0.0), 0.917, 0.0001);

        assertEquals(evaluator("(ceil a)", 1.5, 0.0), 2.0, 0.0001);
        assertEquals(evaluator("(floor a)", 1.5, 0.0), 1.0, 0.0001);
        assertEquals(evaluator("(round a)", 1.57, 0.0), 2.0, 0.0001);
        assertEquals(evaluator("(sqrt a)", 2.0, 0.0), 1.4142, 0.0001);

        assertEquals(evaluator("(in a b c)", 1.0, 10.0), 0.0, 0);
        assertEquals(evaluator("(in a b c)", 20.0, 10.0), 1.0, 0);
        
        assertEquals(evaluator("(if (== a b) 10 -10)", 3.5, 3.5), 10, 0);
        assertEquals(evaluator("(if (== a b) 10 -10)", 4.2, 3.1), -10, 0);

        assertTrue(evaluator("(rand)", 0.0, 0.0) >= 0.0);
        assertTrue(evaluator("(rand)", 0.0, 0.0) < 1.0);
        assertTrue(evaluator("(rand-in a b)", 2.0, 3.0) >= 2.0);
        assertTrue(evaluator("(rand-in a b)", 2.0, 3.0) < 3.0);
    }

    private double evaluator(final String expr, final double a, final double b) {
        VariableRegistry variableRegistry = new VariableRegistry();
        Expr expression = Expression.parse(expr, variableRegistry);

        Map<String, Double> varMap = new HashMap<String, Double>();
        varMap.put("a", a);
        varMap.put("b", b);
        varMap.put("c", 100.0);
        variableRegistry.refresh(varMap);

        return expression.evaluate();
    }

}

