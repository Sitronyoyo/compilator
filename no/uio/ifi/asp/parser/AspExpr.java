// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    // -- Must be changed in part 2:
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
        super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        // -- Must be changed in part 2:
        AspExpr ae = new AspExpr(s.curLineNum());
        ae.andTests.add(AspAndTest.parse(s));
        while (s.curToken().kind == orToken) {
            skip(s, orToken);
            ae.andTests.add(AspAndTest.parse(s));
        }

        leaveParser("expr");
        return ae;
    }

    @Override
    public void prettyPrint() {
        // -- Must be changed in part 2:
        andTests.get(0).prettyPrint();
        for (int i = 1; i < andTests.size(); i++) {
            prettyWrite(" or ");
            andTests.get(i).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 3: v1? v1:v2
        RuntimeValue v = andTests.get(0).eval(curScope);
        for (int i = 1; i < andTests.size(); ++i) {
            if (v.getBoolValue("expr operand", this))
                return v;
            v = andTests.get(i).eval(curScope);
        }
        return v;
    }
}
