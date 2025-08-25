package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspSuite extends AspCompoundStmt {
    ArrayList<AspStmt> stmts = new ArrayList<>();
    AspSmallStmtList ssl = null;

    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");
        AspSuite as = new AspSuite(s.curLineNum());
        if (s.curToken().kind == newLineToken) {
            skip(s, newLineToken);
            skip(s, indentToken);
            while (s.curToken().kind != dedentToken) {
                as.stmts.add(AspStmt.parse(s));
            }
            skip(s, dedentToken);

        } else {
            as.ssl = AspSmallStmtList.parse(s);
        }

        leaveParser("suite");
        return as;
    }

    @Override
    public void prettyPrint() {
        // -- Must be changed in part 2:
        if (ssl != null) {
            ssl.prettyPrint();
        } else {
            prettyWriteLn();
            prettyIndent();
            for (int i = 0; i < stmts.size(); i++) {
                stmts.get(i).prettyPrint();
            }
            prettyDedent();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (ssl != null) {
            return ssl.eval(curScope);
        }
        for (AspStmt as : stmts) {

            as.eval(curScope);

        }
        return null;
    }

}
