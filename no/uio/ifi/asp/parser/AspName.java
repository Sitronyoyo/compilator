package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
    private String name;

    AspName(int n) {
        super(n);
    }

    @Override
    public String toString() {
        return name;
    }

    static AspName parse(Scanner s) {
        enterParser("name");
        AspName an = new AspName(s.curLineNum());
        an.name = s.curToken().name;
        skip(s, nameToken);
        leaveParser("name");
        return an;
    }

    @Override
    void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }
}
