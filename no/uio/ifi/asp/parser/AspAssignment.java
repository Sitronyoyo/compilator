package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {
    AspName name;
    AspExpr expr;
    ArrayList<AspSubscription> subs = new ArrayList<>();

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("assignment");
        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.name = AspName.parse(s);
        while (s.curToken().kind == leftBracketToken) {
            aa.subs.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aa.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        if (!subs.isEmpty()) {
            for (int i = 0; i < subs.size(); i++) {
                subs.get(i).prettyPrint();
            }
        }
        prettyWrite(" = ");
        expr.prettyPrint();

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope); // value - høyere

        // hvis name = value, call assign -> put(s_name, runtimeValue)
        if (subs.isEmpty()) {

            curScope.assign(name.toString(), v);

            trace(name.toString() + " = " + v.showInfo());

        } else if (subs.size() == 1) {
            // 1) Start med 〈name〉-et vokaler; kall den a.
            RuntimeValue a = name.eval(curScope); // find()

            // 2)Beregn så indeksen i sub.
            RuntimeValue index = subs.get(0).eval(curScope);
            trace(name.toString() + "[" + index.showInfo() + "] = " + v.showInfo());

            a.evalAssignElem(index, v, this);
            // curScope.assign(name.toString(), name.eval(curScope));

        } else {
            // 1) Start med 〈name〉-.
            // Slå opp alle 〈subscription〉-ene unntatt den siste i listen/tabellen p
            RuntimeValue id = name.eval(curScope); // name
            for (int i = 0; i < subs.size() - 1; i++) {
                RuntimeValue v2 = subs.get(i).eval(curScope);
                id = id.evalSubscription(v2, this);
            }

            // 2)sist sub
            RuntimeValue inx = subs.get(subs.size() - 1).eval(curScope);

            id.evalAssignElem(inx, v, this); // evalAssignElem(sistsubRuntimeV, exprRuntimeV, this)
            // curScope.assign(name.toString(), name.eval(curScope));
        }
        return null;
    }

}
