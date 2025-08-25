package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspBooleanLiteral extends AspAtom {

    private String str;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");
        AspBooleanLiteral in = new AspBooleanLiteral(s.curLineNum());
        in.str = s.curToken().kind.toString();
        s.readNextToken();
        leaveParser("boolean literal");
        return in;
    }

    @Override
    void prettyPrint() {
        prettyWrite(str);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(Boolean.parseBoolean(str));
    }
}