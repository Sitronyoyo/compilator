// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // -- Must be changed in part 4:
        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });
        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.print(actualParams.get(0).toString());
                Scanner sc = new Scanner(System.in);
                String verdi = sc.nextLine();
                return new RuntimeStringValue(verdi);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0)
                        System.out.print(" ");
                    System.out.print(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });
        // float()
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                return new RuntimeFloatValue(actualParams.get(0).getFloatValue("float", where));
            }
        });

        // int()
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                RuntimeValue v = actualParams.get(0);
                return new RuntimeIntValue(v.getIntValue("int", where));

            }
        });
        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                RuntimeValue v = actualParams.get(0);
                return new RuntimeStringValue(v.toString());

            }
        });
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                RuntimeValue left = actualParams.get(0);
                RuntimeValue right = actualParams.get(1);
                if (left instanceof RuntimeIntValue && right instanceof RuntimeIntValue) {
                    ArrayList<RuntimeValue> list = new ArrayList<>();
                    for (long i = left.getIntValue("int", where); i < right.getIntValue("int", where); i++) {
                        list.add(new RuntimeIntValue(i));
                    }
                    return new RuntimeListValue(list);

                } else {
                    runtimeError("Wrong parameters type to range", where);
                }
                return null;
            }
        });

    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
            int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
