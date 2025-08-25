package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspAndTest extends AspSyntax {
    ArrayList<AspNotTest> notTests = new ArrayList<>();;

    AspAndTest(int n) {
        super(n);
    }

    static AspAndTest parse(Scanner s) {
        enterParser("and test");
        AspAndTest andTest = new AspAndTest(s.curLineNum());
        andTest.notTests.add(AspNotTest.parse(s));
        while (s.curToken().kind == andToken) {
            skip(s, andToken);
            andTest.notTests.add(AspNotTest.parse(s));

        }
        leaveParser("and test");
        return andTest;
    }

    @Override
    void prettyPrint() {
        notTests.get(0).prettyPrint();
        for (int i = 1; i < notTests.size(); i++) {
            prettyWrite(" and ");
            notTests.get(i).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 3: v1? v2:v1
        RuntimeValue v = notTests.get(0).eval(curScope);
        for (int i = 1; i < notTests.size(); ++i) {
            if (!v.getBoolValue("and operand", this))
                return v;
            v = notTests.get(i).eval(curScope);
        }
        return v;
    }
}