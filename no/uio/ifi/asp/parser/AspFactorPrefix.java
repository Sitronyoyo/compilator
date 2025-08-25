package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorPrefix extends AspSyntax {
    String tegn;

    AspFactorPrefix(int n) {
        super(n);

    }

    static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");
        AspFactorPrefix co = new AspFactorPrefix(s.curLineNum());
        co.tegn = s.curToken().toString();
        skip(s, s.curToken().kind);
        leaveParser("factor prefix");
        return co;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tegn + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

}
