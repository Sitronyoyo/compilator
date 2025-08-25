package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    String typeName() {
        return "float";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (floatValue == 0.0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return Math.round(floatValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    // factor prefix: - +
    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(+floatValue);
    }

    // term opr: + -
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue + v.getFloatValue("plus", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("minus", where));
        }
        runtimeError("Type error for -.", where);
        return null; // Required by the compiler!
    }

    // factor opr: * / % //
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(
                    floatValue * v.getFloatValue("multiply", where));
        }

        runtimeError("Type error for *.", where);
        return null; // Required by the compiler!
    }

    @Override // float/ (int or float) ->float
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            double result = (double) floatValue / v.getFloatValue("float divide", where);
            return new RuntimeFloatValue(result);
        }

        runtimeError("Type error for /.", where);
        return null; // Required by the compiler!
    }

    @Override // %
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {

        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) { // int %float = float
            double v2 = v.getFloatValue("floorMod", where);
            return new RuntimeFloatValue(floatValue - v2 * Math.floor(floatValue / v2));
        }

        runtimeError("Type error for %.", where);
        return null; // Required by the compiler!
    }

    @Override //
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {

        if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("float floorMod", where);
            return new RuntimeFloatValue(Math.floor(floatValue / v2));
        }

        runtimeError("Type error for //.", where);
        return null; // Required by the compiler!
    }

    @Override // ==
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("equal", where);
            return new RuntimeBoolValue(floatValue == v2);
        }

        if (v instanceof RuntimeNoneValue) { // float == none ->false
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler
    }

    @Override // !=
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) { // int != int, int != float
            double v2 = v.getFloatValue("not equal", where);
            return new RuntimeBoolValue(floatValue != v2);
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
            return new RuntimeBoolValue(floatValue < v2);
        }

        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    @Override // <=
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("LessEqual", where);
            return new RuntimeBoolValue(floatValue <= v2);
        }

        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    @Override // >=
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("greaterEqual", where);
            return new RuntimeBoolValue(floatValue >= v2);
        }

        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override // >
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue) {
            double v2 = v.getFloatValue("greater", where);
            return new RuntimeBoolValue(floatValue > v2);
        }

        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    @Override // not int
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not float", where));
    }

}
