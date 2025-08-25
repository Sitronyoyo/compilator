package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorOpr> operators = new ArrayList<>();

    ArrayList<AspPrimary> py = new ArrayList<>();

    HashMap<Integer, AspFactorPrefix> hm = new HashMap<>();

    AspFactor(int n) {
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");
        AspFactor af = new AspFactor(s.curLineNum());

        int pos = 0;

        if (s.isFactorPrefix()) {
            AspFactorPrefix value = AspFactorPrefix.parse(s);
            af.hm.put(pos, value);
        }
        af.py.add(AspPrimary.parse(s));

        while (s.isFactorOpr()) {
            pos++;
            af.operators.add(AspFactorOpr.parse(s));
            if (s.isFactorPrefix()) {
                AspFactorPrefix value = AspFactorPrefix.parse(s);
                af.hm.put(pos, value);
            }
            af.py.add(AspPrimary.parse(s));

        }

        leaveParser("factor");
        return af;
    }

    @Override
    void prettyPrint() {
        if (hm.isEmpty()) {
            py.get(0).prettyPrint();
            for (int i = 1; i < py.size(); i++) {
                operators.get(i - 1).prettyPrint();
                py.get(i).prettyPrint();
            }
        } else {
            Set<Integer> keys = hm.keySet();
            if (keys.contains(0)) {
                hm.get(0).prettyPrint();
            }
            py.get(0).prettyPrint();
            for (int i = 1; i < py.size(); i++) {
                operators.get(i - 1).prettyPrint();
                if (keys.contains(i)) {
                    hm.get(i).prettyPrint();
                }
                py.get(i).prettyPrint();
            }
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // del 3
        RuntimeValue v = py.get(0).eval(curScope);

        if (!hm.isEmpty() && hm.containsKey(0)) {
            String prefix = hm.get(0).tegn;
            if (prefix.equals("+")) {
                v = v.evalPositive(this);
            } else if (prefix.equals("-")) {
                v = v.evalNegate(this);
            }
        }

        for (int i = 1; i < py.size(); i++) {
            RuntimeValue v2 = py.get(i).eval(curScope);
            if (hm.size() > 0 && hm.containsKey(i)) { // sette prefix
                String prefix = hm.get(i).tegn;
                if (prefix.equals("+")) {
                    v2 = v2.evalPositive(this);
                } else if (prefix.equals("-")) {
                    v2 = v2.evalNegate(this);
                }
            }

            String tegn = operators.get(i - 1).tegn;
            switch (tegn) {
                case "*":
                    v = v.evalMultiply(v2, this);
                    break;
                case "/":
                    v = v.evalDivide(v2, this);
                    break;
                case "%":
                    v = v.evalModulo(v2, this);
                    break;
                case "//":
                    v = v.evalIntDivide(v2, this);
                    break;
                default:
                    Main.panic("Illegal factor operator: " + tegn + "!");
                    break;
            }
        }
        return v;

    }
}
