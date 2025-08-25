// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspStmt extends AspSyntax {

    AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("stmt");
        AspStmt stmt = null;
        TokenKind k = s.curToken().kind;
        if (k == defToken || k == whileToken || k == ifToken || k == forToken) {
            stmt = AspCompoundStmt.parse(s);
        } else {
            stmt = AspSmallStmtList.parse(s);
        }

        leaveParser("stmt");
        return stmt;
    }

}
