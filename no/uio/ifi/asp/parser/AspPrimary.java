package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    AspAtom atom;
    ArrayList<AspPrimarySuffix> suffixs = new ArrayList<>();

    AspPrimary(int n) {
        super(n);
    }

    static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary ap = new AspPrimary(s.curLineNum());

        ap.atom = AspAtom.parse(s);
        while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
            ap.suffixs.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        atom.prettyPrint();
        if (!suffixs.isEmpty()) {
            for (AspPrimarySuffix su : suffixs) {
                su.prettyPrint();
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // del 4
        RuntimeValue v = atom.eval(curScope);
        if (v instanceof RuntimeFunc && suffixs.size() == 1 && suffixs.get(0) instanceof AspArguments) {
            AspArguments aa = (AspArguments) suffixs.get(0);
            RuntimeListValue lv = (RuntimeListValue) aa.eval(curScope);
            ArrayList<RuntimeValue> actParams = lv.getList();

            // string for alle paramenter
            String str = "[";
            for (int i = 0; i < actParams.size(); i++) {
                str += actParams.get(i).showInfo();
                if (i < actParams.size() - 1) {
                    str += ", ";
                }
            }
            str += "]";

            trace("Call function " + atom.toString() + " with params " + str);
            return v.evalFuncCall(actParams, this);

        }

        // del 3
        if (!suffixs.isEmpty()) {
            // [, ,][tall]
            for (int i = 0; i < suffixs.size(); i++) {
                if (v instanceof RuntimeListValue || v instanceof RuntimeStringValue ||
                        v instanceof RuntimeDictValue) {
                    RuntimeValue v2 = suffixs.get(i).eval(curScope);
                    v = v.evalSubscription(v2, this);
                }
            }
        }
        return v;
    }

}
