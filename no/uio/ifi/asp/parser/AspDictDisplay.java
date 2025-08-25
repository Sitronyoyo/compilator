package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspDictDisplay extends AspAtom {

    ArrayList<AspExpr> exprs = new ArrayList<>(); // verdi
    ArrayList<AspStringLiteral> strs = new ArrayList<>(); // key

    AspDictDisplay(int n) {
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("dict display");
        AspDictDisplay dd = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken); // {

        while (s.curToken().kind != rightBraceToken) {
            dd.strs.add(AspStringLiteral.parse(s)); // string literal
            skip(s, colonToken); // :
            dd.exprs.add(AspExpr.parse(s));// expr
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken);
                dd.strs.add(AspStringLiteral.parse(s)); // string literal
                skip(s, colonToken); // :
                dd.exprs.add(AspExpr.parse(s));// expr
            }
        }

        skip(s, rightBraceToken); // }

        leaveParser("dict display");
        return dd;
    }

    @Override
    void prettyPrint() {
        prettyWrite("{");

        for (int i = 0; i < strs.size(); i++) {
            strs.get(i).prettyPrint();
            prettyWrite(":");
            exprs.get(i).prettyPrint();
            if (i != strs.size() - 1) {
                prettyWrite(", ");
            }
        }
        prettyWrite("}");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<RuntimeValue> values = new ArrayList<>();
        for (int i = 0; i < strs.size(); i++) {
            RuntimeStringValue v1 = (RuntimeStringValue) strs.get(i).eval(curScope);
            keys.add(v1.toString());
            RuntimeValue v2 = exprs.get(i).eval(curScope);
            values.add(v2);
        }

        return new RuntimeDictValue(keys, values);
    }

}
