package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspSyntax;

// del 4
public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def; // ?
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(String name, AspFuncDef def, RuntimeScope defScope) {
        this.name = name;
        this.defScope = defScope;
        this.def = def;
    }

    public RuntimeFunc(String name) {
        this.name = name;
    }

    @Override
    String typeName() {
        return "func def";
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars,
            AspSyntax where) {

        try {
            // 1)Sjekk om korrekt antall parametre.
            if (def.names.size() - 1 == actPars.size()) {
                // 2) opprette et nytt skop
                RuntimeScope newScope = new RuntimeScope(defScope);

                // 3).Initiere parametrene, tilordning
                for (int i = 0; i < actPars.size(); i++) {
                    String id = def.names.get(i + 1).toString();
                    RuntimeValue v = actPars.get(i);
                    newScope.assign(id, v);
                }
                // 4) Utføre funksjonen/AspSuite
                def.as.eval(newScope);
            } else {
                runtimeError("Wrong number of parameters to " + name + "!", where);
            }

        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }

}
