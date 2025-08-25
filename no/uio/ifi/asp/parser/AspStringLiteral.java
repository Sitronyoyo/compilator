package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {

    private String str;

    AspStringLiteral(int n) {
        super(n);
    }

    static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");
        AspStringLiteral in = new AspStringLiteral(s.curLineNum());
        in.str = s.curToken().stringLit;
        skip(s, stringToken);
        leaveParser("string literal");
        return in;
    }

    @Override
    void prettyPrint() {
        prettyWrite("\"" + str + "\"");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(str);
    }
}
