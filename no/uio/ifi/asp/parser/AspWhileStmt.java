package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr ae = null;
    AspSuite as = null;

    AspWhileStmt(int n) {
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("while stmt");
        AspWhileStmt ws = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        ws.ae = AspExpr.parse(s);
        skip(s, colonToken);
        ws.as = AspSuite.parse(s);
        leaveParser("while stmt");

        return ws;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("while ");
        ae.prettyPrint();
        prettyWrite(": ");
        as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue t = ae.eval(curScope);
            if (!t.getBoolValue("while loop test", this))
                break;
            trace("while True: ...");
            as.eval(curScope);
        }
        trace("while False:");
        return null;
    }
}
