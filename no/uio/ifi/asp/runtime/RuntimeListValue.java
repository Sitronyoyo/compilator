package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    ArrayList<RuntimeValue> listValue;

    public RuntimeListValue(ArrayList<RuntimeValue> listValue) {
        this.listValue = listValue;
    }

    public ArrayList<RuntimeValue> getList() {
        return listValue;
    }

    @Override
    String typeName() {
        return "list";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (listValue.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < listValue.size(); i++) {
            str += listValue.get(i).showInfo();
            if (i < listValue.size() - 1) {
                str += ", ";
            }
        }
        str += "]";
        return str;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            ArrayList<RuntimeValue> list2 = new ArrayList<>();
            for (int i = 0; i < v.getIntValue("int", where); i++) {
                list2.addAll(listValue);
            }
            return new RuntimeListValue(list2);
        }
        runtimeError("'*' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override // [, ,][] ->element
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            int i = (int) v.getIntValue("int", where);
            if (i >= 0 && i < listValue.size()) {
                return listValue.get(i);
            } else {
                runtimeError("List index " + i + " out of range!", where);
            }
        }
        runtimeError("A list index must be an integer!", where);
        return null; // Required by the compiler!
    }

    @Override // ==
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) { // list == none ->false
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // !=
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) { // list == none ->false
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // not []
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not list", where));
    }

    @Override // del 4
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeIntValue) {
            int i = (int) inx.getIntValue("int", where);
            if (i >= 0 && i < listValue.size()) {
                listValue.set(i, val);
            } else {
                runtimeError("List index " + i + " out of range!", where);
            }
        }

    }

    @Override // del 4
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(listValue.size());
    }

}
