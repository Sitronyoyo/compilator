package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspPrimarySuffix extends AspSyntax {
    AspPrimarySuffix(int n) {
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");
        AspPrimarySuffix aps = null;
        switch (s.curToken().kind) {

            case leftParToken:
                aps = AspArguments.parse(s);
                break;
            case leftBracketToken:
                aps = AspSubscription.parse(s);
                break;

            default:
                parserError("Expected an primary suffix but found a " +
                        s.curToken().kind + "!", s.curLineNum());
                break;
        }
        leaveParser("primary suffix");
        return aps;
    }

    @Override
    void prettyPrint() {
        this.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
