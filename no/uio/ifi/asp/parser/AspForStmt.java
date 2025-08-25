package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspForStmt extends AspCompoundStmt {
    AspExpr ae = null;
    AspSuite as = null;
    AspName name = null;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("for stmt");
        AspForStmt fs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        fs.name = AspName.parse(s);
        skip(s, inToken);
        fs.ae = AspExpr.parse(s);
        skip(s, colonToken);
        fs.as = AspSuite.parse(s);

        leaveParser("for stmt");
        return fs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        ae.prettyPrint();
        prettyWrite(": ");
        as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- del4
        // 1)Evaluer 〈expr〉; sjekk at den er en liste
        RuntimeValue v = ae.eval(curScope);
        if (v instanceof RuntimeListValue) {
            ArrayList<RuntimeValue> list = ((RuntimeListValue) v).getList();

            // RuntimeValue nameV = name.eval(curScope); // name
            for (int i = 1; i < list.size() + 1; i++) {
                RuntimeValue value = list.get(i - 1);
                curScope.assign(name.toString(), value); // assign()-put(name, RuntimeValue)

                String str = value.showInfo();
                trace("for #" + i + ": " + name.toString() + " = " + str);

                // 2)Evaluer 〈suite〉.
                as.eval(curScope);
            }
        }
        return null;
    }

}
