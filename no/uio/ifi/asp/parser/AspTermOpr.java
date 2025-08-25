package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspTermOpr extends AspSyntax {
    String tegn;

    AspTermOpr(int n) {
        super(n);

    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");
        AspTermOpr co = new AspTermOpr(s.curLineNum());
        co.tegn = s.curToken().toString();
        skip(s, s.curToken().kind);
        leaveParser("term opr");
        return co;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + tegn + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
