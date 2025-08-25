package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
    private String tall;

    AspFloatLiteral(int n) {
        super(n);
    }

    static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");
        AspFloatLiteral in = new AspFloatLiteral(s.curLineNum());
        in.tall = String.valueOf(s.curToken().floatLit);
        skip(s, floatToken);
        leaveParser("float literal");
        return in;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tall);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(Double.parseDouble(tall));
    }

}
