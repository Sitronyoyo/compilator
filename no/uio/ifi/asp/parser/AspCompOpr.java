package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

public class AspCompOpr extends AspComparison {
    String tegn;

    AspCompOpr(int n) {
        super(n);

    }

    static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");
        AspCompOpr co = new AspCompOpr(s.curLineNum());
        co.tegn = s.curToken().toString();
        skip(s, s.curToken().kind);
        leaveParser("comp opr");
        return co;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + tegn + " ");
    }

}
