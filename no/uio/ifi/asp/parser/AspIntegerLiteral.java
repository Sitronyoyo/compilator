package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom {
    private String tall;

    AspIntegerLiteral(int n) {
        super(n);
    }

    static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");
        AspIntegerLiteral in = new AspIntegerLiteral(s.curLineNum());
        in.tall = String.valueOf(s.curToken().integerLit);
        skip(s, integerToken);
        leaveParser("integer literal");
        return in;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tall);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(Long.parseLong(tall));
    }

}
