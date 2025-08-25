package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("small stmt list");
        AspSmallStmtList ssl = new AspSmallStmtList(s.curLineNum());

        ssl.smallStmts.add(AspSmallStmt.parse(s));

        while (s.curToken().kind != newLineToken) {

            skip(s, semicolonToken);
            if (s.curToken().kind == newLineToken) {
                break;
            }
            ssl.smallStmts.add(AspSmallStmt.parse(s));
        }

        skip(s, newLineToken);

        leaveParser("small stmt list");
        return ssl;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < smallStmts.size(); i++) {
            smallStmts.get(i).prettyPrint();
            if (i < smallStmts.size() - 1) {
                prettyWrite("; ");
            }
        }
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspSmallStmt as : smallStmts) {
            as.eval(curScope);
        }
        return null;
    }

}
