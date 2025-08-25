package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    AspComparison com;
    boolean withNot = false;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {

        enterParser("not test");
        AspNotTest notTest = new AspNotTest(s.curLineNum());
        if (s.curToken().kind == notToken) {
            notTest.withNot = true;
            skip(s, notToken);
        }
        notTest.com = AspComparison.parse(s);
        leaveParser("not test");
        return notTest;
    }

    @Override
    void prettyPrint() {
        if (withNot) {
            prettyWrite("not ");
        }
        com.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // del 3
        RuntimeValue v = com.eval(curScope);
        if (withNot) {
            v = v.evalNot(this);
        }
        return v;

    }

}
