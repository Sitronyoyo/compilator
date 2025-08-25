package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

public class AspSmallStmt extends AspSmallStmtList {
    AspSmallStmt(int n) {
        super(n);
    }

    static AspSmallStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("small stmt");
        AspSmallStmt smallStmt = null;
        switch (s.curToken().kind) {
            case nameToken:
                if (s.anyEqualToken()) {
                    smallStmt = AspAssignment.parse(s);
                } else {
                    smallStmt = AspExprStmt.parse(s);
                }
                break;
            case globalToken:
                smallStmt = AspGlobalStmt.parse(s);
                break;
            case passToken:
                smallStmt = AspPassStmt.parse(s);
                break;
            case returnToken:
                smallStmt = AspReturnStmt.parse(s);
                break;
            default:
                smallStmt = AspExprStmt.parse(s);
                break;
        }

        leaveParser("small stmt");
        return smallStmt;
    }

}
