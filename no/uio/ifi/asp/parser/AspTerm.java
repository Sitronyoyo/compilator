package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspTerm extends AspSyntax {
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> operators = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    static AspTerm parse(Scanner s) {
        enterParser("term");
        AspTerm ac = new AspTerm(s.curLineNum());

        ac.factors.add(AspFactor.parse(s));
        while (s.isTermOpr()) {
            ac.operators.add(AspTermOpr.parse(s));

            ac.factors.add(AspFactor.parse(s));
        }
        leaveParser("term");
        return ac;
    }

    @Override
    void prettyPrint() {
        factors.get(0).prettyPrint();
        for (int i = 1; i < factors.size(); i++) {
            operators.get(i - 1).prettyPrint();
            factors.get(i).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // del 3
        RuntimeValue v = factors.get(0).eval(curScope);
        for (int i = 1; i < factors.size(); i++) {
            String tegn = operators.get(i - 1).tegn;
            switch (tegn) {
                case "+":
                    v = v.evalAdd(factors.get(i).eval(curScope), this);
                    break;
                case "-":
                    v = v.evalSubtract(factors.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + tegn + "!");
                    break;
            }
        }
        return v;
    }

}
