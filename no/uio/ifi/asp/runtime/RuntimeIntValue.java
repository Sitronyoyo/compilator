package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long intValue) {
        this.intValue = intValue;
    }

    @Override
    String typeName() {
        return "int";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (intValue == 0) {
            return false;
        }
        return true;

    }

    @Override
    public String showInfo() {
        return super.showInfo();
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) intValue;
    }

    // factor prefix: - +
    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-intValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(intValue);
    }

    // term opr: + -
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(getIntValue("int", where) + v.getIntValue("plus", where));
        }
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(getFloatValue("plus", where) + v.getFloatValue("plus", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue - v.getIntValue("minus", where));
        }
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(getFloatValue("minus", where) - v.getFloatValue("minus", where));
        }
        runtimeError("Type error for -.", where);
        return null; // Required by the compiler!
    }

    // factor opr: * / % //
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue * v.getIntValue("int ultiply", where));
        }
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(
                    getFloatValue("float multiply", where) * v.getFloatValue("float multiply", where));
        }

        runtimeError("Type error for *.", where);
        return null; // Required by the compiler!
    }

    @Override // int/ (int or float) ->float
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            double result = (double) intValue / v.getFloatValue("float divide", where);

            return new RuntimeFloatValue(result);
        }

        runtimeError("Type error for /.", where);
        return null; // Required by the compiler!
    }

    @Override // %
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // int%int =int
            return new RuntimeIntValue(Math.floorMod(intValue, v.getIntValue("int floorMod", where)));
        }
        if (v instanceof RuntimeFloatValue) { // int %float = float
            double v2 = v.getFloatValue("floot floorMod", where);
            return new RuntimeFloatValue(intValue - v2 * Math.floor(intValue / v2));
        }

        runtimeError("Type error for %.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // int // int =int
            return new RuntimeIntValue(Math.floorDiv(intValue, v.getIntValue("int floorMod", where)));
        }
        if (v instanceof RuntimeFloatValue) { // int //float = float
            double v2 = v.getFloatValue("float floorMod", where);
            return new RuntimeFloatValue(Math.floor(intValue / v2));
        }

        runtimeError("Type error for //.", where);
        return null; // Required by the compiler!
    }

    @Override // ==
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("equal", where);
            return new RuntimeBoolValue(intValue == v2);
        }

        if (v instanceof RuntimeNoneValue) { // int == none ->false
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // !=
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) { // int != int, int != float
            double v2 = v.getFloatValue("not equal", where);
            return new RuntimeBoolValue(intValue != v2);
        }
        if (v instanceof RuntimeNoneValue) { // int != none ->false
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null; // Required by the compiler
    }

    @Override // <
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("less", where);
            return new RuntimeBoolValue(intValue < v2);
        }

        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override // <=
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("LessEqual", where);
            return new RuntimeBoolValue(intValue <= v2);
        }

        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    @Override // >=
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("greaterEqual", where);
            return new RuntimeBoolValue(intValue >= v2);
        }

        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override // >
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("greater", where);
            return new RuntimeBoolValue(intValue > v2);
        }

        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    @Override // not int
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not int", where));
    }

}
