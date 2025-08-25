package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String str;

    public RuntimeStringValue(String str) {
        this.str = str;
    }

    @Override
    public String showInfo() {
        if (str.indexOf("\'") >= 0) {
            return '"' + str + '"';
        } else {
            return "'" + str + "'";
        }
    }

    @Override
    String typeName() {
        return "String";
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            long intValue = Integer.parseInt(str);
            return intValue;
        } catch (NumberFormatException e) {
            runtimeError("Type error: " + what + " is not a int!", where);
        }
        return 0;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        try {
            double intValue = Double.parseDouble(str);
            return intValue;
        } catch (NumberFormatException e) {
            runtimeError("Type error: " + what + " is not a float!", where);
        }
        return 0;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (str == "") {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override // string +string
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(str + v.toString());
        }

        runtimeError("'+' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override // string * int
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            String newStr = "";
            for (long i = 0; i < v.getIntValue("str multiply", where); i++) {
                newStr += str;
            }
            return new RuntimeStringValue(newStr);
        }
        runtimeError("'*' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override // ==
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) { // string == string
            return new RuntimeBoolValue(str.compareTo(v.toString()) == 0);
        }
        if (v instanceof RuntimeNoneValue) { // string == none ->false
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // !=
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) { // string != string
            return new RuntimeBoolValue(str.compareTo(v.toString()) != 0);
        }
        if (v instanceof RuntimeNoneValue) { // string != none -> true
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null; // Required by the compiler
    }

    @Override // string < string
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(str.compareTo(v.toString()) < 0);
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override // string <= string
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(str.compareTo(v.toString()) <= 0);
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override // string >= string
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(str.compareTo(v.toString()) >= 0);
        }
        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override // string > string
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(str.compareTo(v.toString()) > 0);
        }
        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    @Override // not string
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(str, where));
    }

    @Override // "abcdf"[0] -> "a"
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            int i = (int) v.getIntValue("sub int value", where);
            if (i >= 0 && i < str.length()) {
                String newStr = String.valueOf(str.charAt(i));
                return new RuntimeStringValue(newStr);
            }
        }
        runtimeError("Type error for [].", where);
        return null; // Required by the compiler

    }

    @Override // del 4
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(str.length());
    }

}
