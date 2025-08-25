package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspPassStmt extends AspSmallStmt {
    AspPassStmt(int n) {
        super(n);
    }

    static AspPassStmt parse(Scanner s) {

        enterParser("pass stmt");
        AspPassStmt aps = new AspPassStmt(s.curLineNum());
        s.readNextToken();
        leaveParser("pass stmt");
        return aps;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("pass ");

    }

    // del 4
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("pass");
        return null;
    }

}
