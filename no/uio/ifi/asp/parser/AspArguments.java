package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {
    public ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");
        AspArguments aa = new AspArguments(s.curLineNum());

        skip(s, leftParToken); // skip "("
        if (s.curToken().kind != rightParToken) {
            aa.exprs.add(AspExpr.parse(s));
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken); // skip ","
                aa.exprs.add(AspExpr.parse(s));
            }
        }

        skip(s, rightParToken); // skip ")"

        leaveParser("arguments");
        return aa;

    }

    @Override
    void prettyPrint() {
        prettyWrite("(");
        if (!exprs.isEmpty()) {
            exprs.get(0).prettyPrint();
            for (int i = 1; i < exprs.size(); i++) {
                prettyWrite(", ");
                exprs.get(i).prettyPrint();
            }

        }
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- del 3 og 4:
        ArrayList<RuntimeValue> actPars = new ArrayList<>();

        for (int i = 0; i < exprs.size(); i++) {

            RuntimeValue v = exprs.get(i).eval(curScope);

            actPars.add(v);
        }
        return new RuntimeListValue(actPars);

    }

}
