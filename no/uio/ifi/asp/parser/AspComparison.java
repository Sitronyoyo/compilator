package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> operators = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");
        AspComparison ac = new AspComparison(s.curLineNum());

        ac.terms.add(AspTerm.parse(s));
        while (s.isCompOpr()) {
            ac.operators.add(AspCompOpr.parse(s));
            ac.terms.add(AspTerm.parse(s));
        }
        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        terms.get(0).prettyPrint();
        for (int i = 1; i < terms.size(); i++) {
            operators.get(i - 1).prettyPrint();
            terms.get(i).prettyPrint();

        }
    }

    // 1 <= a == b < 10 tolkes (1 <= a) and (a == b) and (b < 10)
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // del 3
        ArrayList<RuntimeValue> list = new ArrayList<>();

        if (terms.size() == 1) {
            RuntimeValue v = terms.get(0).eval(curScope);
            list.add(v);
        } else {
            for (int i = 1; i < terms.size(); ++i) {
                String tegn = operators.get(i - 1).tegn;
                RuntimeValue v1 = terms.get(i - 1).eval(curScope);
                RuntimeValue v2 = terms.get(i).eval(curScope);
                RuntimeValue value = compareWith(v1, v2, tegn);
                list.add(value);
            }
        }

        RuntimeValue v = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (!v.getBoolValue("comparison operand", this))
                return v;
            v = list.get(i);
        }
        return v;
    }

    private RuntimeValue compareWith(RuntimeValue v1, RuntimeValue v2, String tegn) {
        switch (tegn) {
            case "<":
                return v1.evalLess(v2, this);

            case "<=":
                return v1.evalLessEqual(v2, this);

            case ">":
                return v1.evalGreater(v2, this);

            case ">=":
                return v1.evalGreaterEqual(v2, this);

            case "==":
                return v1.evalEqual(v2, this);

            case "!=":
                return v1.evalNotEqual(v2, this);

            default:
                Main.panic("Illegal comparison operator: " + tegn + "!");
                return null;
        }

    }
}