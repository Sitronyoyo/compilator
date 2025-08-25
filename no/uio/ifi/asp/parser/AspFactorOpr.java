package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorOpr extends AspSyntax {
    String tegn;

    AspFactorOpr(int n) {
        super(n);

    }

    static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");
        AspFactorOpr co = new AspFactorOpr(s.curLineNum());
        co.tegn = s.curToken().toString();
        skip(s, s.curToken().kind);
        leaveParser("factor opr");
        return co;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + tegn + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }

}
