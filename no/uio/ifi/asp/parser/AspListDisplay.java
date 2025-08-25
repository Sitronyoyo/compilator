package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspListDisplay extends AspAtom {

    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    static AspListDisplay parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("list display");
        AspListDisplay dd = new AspListDisplay(s.curLineNum());

        skip(s, leftBracketToken); // [

        while (s.curToken().kind != rightBracketToken) {
            dd.exprs.add(AspExpr.parse(s));// expr
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken); // ,
                dd.exprs.add(AspExpr.parse(s));// expr
            }
        }

        skip(s, rightBracketToken); // ]

        leaveParser("list display");
        return dd;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");

        for (int i = 0; i < exprs.size(); i++) {
            exprs.get(i).prettyPrint();
            if (i != exprs.size() - 1) {
                prettyWrite(", ");
            }

        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> values = new ArrayList<>();
        for (AspExpr x : exprs) {
            RuntimeValue v = x.eval(curScope);
            values.add(v);
        }
        return new RuntimeListValue(values);
    }

}
