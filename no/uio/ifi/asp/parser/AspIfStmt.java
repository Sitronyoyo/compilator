package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> exprs = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();
    boolean withElse = false;

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("if stmt");
        AspIfStmt is = new AspIfStmt(s.curLineNum());

        skip(s, ifToken); // if
        is.exprs.add(AspExpr.parse(s));
        skip(s, colonToken);
        is.suites.add(AspSuite.parse(s));

        while (s.curToken().kind == elifToken) { // elif
            skip(s, elifToken);
            is.exprs.add(AspExpr.parse(s));
            skip(s, colonToken);
            is.suites.add(AspSuite.parse(s));
        }

        if (s.curToken().kind == elseToken) { // else
            is.withElse = true;
            skip(s, elseToken);
            skip(s, colonToken);
            is.suites.add(AspSuite.parse(s));
        }

        leaveParser("if stmt");
        return is;
    }

    @Override
    public void prettyPrint() {
        for (int i = 0; i < exprs.size(); i++) {
            if (i == 0) {
                prettyWrite("if ");
            } else {
                prettyWrite("elif ");
            }

            exprs.get(i).prettyPrint();
            prettyWrite(": ");
            suites.get(i).prettyPrint();
        }

        if (exprs.size() != suites.size()) {
            prettyWrite("else: ");
            suites.get(suites.size() - 1).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- del4
        // 1) Evaluer alle 〈expr〉til du finner en som er sann.
        RuntimeValue value = null;
        for (int i = 0; i < exprs.size(); i++) {
            RuntimeValue v = exprs.get(i).eval(curScope);
            if (v.getBoolValue("if expr", this)) {
                // 2)Evaluer tilhørende 〈suite〉; så er vi ferdig.
                trace("if True alt #" + (i + 1) + ": ...");
                value = suites.get(i).eval(curScope);
                return value;
            }
        }
        // 3)Hvis ingen 〈expr〉 er sann, evaluer else-grenens〈suite〉 (om det er noen).
        if (withElse) {
            trace("else: ...");
            value = suites.get(suites.size() - 1).eval(curScope);
            return value;
        }
        return null;
    }
}
