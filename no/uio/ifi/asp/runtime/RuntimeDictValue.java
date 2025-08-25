package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import no.uio.ifi.asp.parser.*;

public class RuntimeDictValue extends RuntimeValue {
    ArrayList<String> keys; // key
    ArrayList<RuntimeValue> values; // verdi

    public RuntimeDictValue(ArrayList<String> keys, ArrayList<RuntimeValue> values) {
        this.keys = keys;
        this.values = values;
    }

    @Override
    String typeName() {
        return "dict";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (keys.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        String str = "{";
        for (int i = 0; i < keys.size(); i++) {
            String string = keys.get(i);
            RuntimeStringValue key = new RuntimeStringValue(string);
            str += key.showInfo() + ": " + values.get(i).showInfo();
            if (i < keys.size() - 1) {
                str += ", ";
            }
        }
        str += "}";
        return str;

    }

    @Override // {}[] ->element
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (keys.contains(v.toString())) {
                int pos = keys.indexOf(v.toString());
                return values.get(pos);
            } else {
                runtimeError("Dictionary key " + v.showInfo() + " undefined!", where);
            }

        }
        runtimeError("'subscription' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override // ==
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) { // dict == none ->false
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // !=
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) { // dict == none ->false
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // not {,,}
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not list", where));
    }

    @Override // del 4
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        // if (inx instanceof RuntimeStringValue) {
        // if (hm.containsKey(inx)) {
        // hm.put((RuntimeStringValue) inx, val);
        // }
        // }
        if (inx instanceof RuntimeStringValue) {
            if (keys.contains(inx.toString())) {
                int pos = keys.indexOf(inx.toString());
                values.set(pos, val);
            } else {
                runtimeError("Dictionary key " + inx.showInfo() + " undefined!", where);
            }

        }

    }

    @Override // del 4
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(keys.size());
    }
}
