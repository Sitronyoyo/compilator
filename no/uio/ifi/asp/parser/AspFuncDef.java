package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {
    public AspSuite as = null;
    public ArrayList<AspName> names = new ArrayList<>();

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        // -- Must be changed in part 2:
        enterParser("func def");
        AspFuncDef fd = new AspFuncDef(s.curLineNum());
        skip(s, defToken);

        fd.names.add(AspName.parse(s));
        skip(s, leftParToken);

        if (s.curToken().kind != rightParToken) {
            fd.names.add(AspName.parse(s));
            while (s.curToken().kind == commaToken) {
                skip(s, commaToken);
                fd.names.add(AspName.parse(s));
            }
        }

        skip(s, rightParToken);
        skip(s, colonToken);
        fd.as = AspSuite.parse(s);

        leaveParser("func def");
        return fd;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("def ");
        names.get(0).prettyPrint();

        prettyWrite(" (");
        if (names.size() > 1) {
            names.get(1).prettyPrint();
            for (int i = 2; i < names.size(); i++) {
                prettyWrite(", ");
                names.get(i).prettyPrint();
            }
        }

        prettyWrite(")");
        prettyWrite(": ");
        as.prettyPrint();
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- del4
        String name = names.get(0).toString();
        curScope.registerGlobalName(name);
        trace("def " + name);
        curScope.assign(name, new RuntimeFunc(name, this, curScope));

        return null;
    }

}
