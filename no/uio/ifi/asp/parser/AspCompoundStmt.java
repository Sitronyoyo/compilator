package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspCompoundStmt extends AspStmt {

    AspCompoundStmt(int n) {
        super(n);
    }

    static AspCompoundStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("compound stmt");
        AspCompoundStmt compStmt = null;
        switch (s.curToken().kind) {
            case forToken:
                compStmt = AspForStmt.parse(s);
                break;
            case ifToken:
                compStmt = AspIfStmt.parse(s);
                break;
            case whileToken:
                compStmt = AspWhileStmt.parse(s);
                break;
            case defToken:
                compStmt = AspFuncDef.parse(s);
                break;
            default:
                parserError("Expected an expression atom but found a " +
                        s.curToken().kind + "!", s.curLineNum());
                break;
        }

        leaveParser("compound stmt");
        return compStmt;
    }

    @Override
    public void prettyPrint() {
        this.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return this.eval(curScope);
    }

}
