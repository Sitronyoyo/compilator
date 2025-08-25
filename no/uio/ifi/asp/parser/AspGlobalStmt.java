package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspGlobalStmt extends AspSmallStmt {
    ArrayList<AspName> names = new ArrayList<>();

    AspGlobalStmt(int n) {
        super(n);
    }

    static AspGlobalStmt parse(Scanner s) {

        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
        skip(s, globalToken);
        ags.names.add(AspName.parse(s));
        while (s.curToken().kind == commaToken) {
            skip(s, commaToken);
            ags.names.add(AspName.parse(s));
        }

        leaveParser("global stmt");
        return ags;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("global ");
        for (int i = 0; i < names.size(); i++) {
            names.get(i).prettyPrint();
        }

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspName name : names) {
            curScope.registerGlobalName(name.toString());
        }
        return null;
    }

}
